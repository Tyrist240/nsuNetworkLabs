package server;

import domain.ExecutionStatus;
import domain.Header;
import domain.ProtocolSerializer;
import exceptions.HeaderDeserializationException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.TimerTask;
import java.util.function.Supplier;

public class Handle implements Supplier<ExecutionStatus> {

    private Socket socket;

    private Server server;

    private long lastSize = 0;

    public Handle(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @SneakyThrows
    @Override
    public ExecutionStatus get() {
        InputStream inputStream;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Header header = getHeader(inputStream);
            processHeaderAndRecvFile(header, inputStream);
        } catch (IOException | HeaderDeserializationException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            if (outputStream != null) {
                outputStream.write(ExecutionStatus.CANCELLED.getOrdinal());
            }

            return ExecutionStatus.CANCELLED;
        } finally {
            socket.close();
        }

        return ExecutionStatus.SUCCESS;
    }

    private Header getHeader(InputStream inputStream) throws IOException, HeaderDeserializationException {
        byte[] headerSerialized = inputStream.readNBytes(ProtocolSerializer.HEADER_SIZE);
        return ProtocolSerializer.deserialize(headerSerialized);
    }

    private void processHeaderAndRecvFile(Header header, InputStream inputStream) throws IOException {
        String secureFilename = FilenameUtils.getName(header.getFilename());
        File targetFile = new File(Server.PATH_TO_DIR + secureFilename);

        OutputStream outputStream = socket.getOutputStream();
        if (targetFile.exists()) {
            outputStream.write(ExecutionStatus.CANCELLED.getOrdinal());
            throw new IllegalArgumentException("File already exists!");
        } else {
            outputStream.write(ExecutionStatus.READY_FOR_RECEIVE.getOrdinal());
        }
        outputStream.flush();

        TimerTask handleTimerTask = new TimerTask() {
            @Override
            public void run() {
                long curSize = FileUtils.sizeOf(targetFile);

                System.out.println(
                    "Current speed of uploading " + targetFile.getName() + ": " +
                        (curSize - lastSize) / 3072L + " kB/s"
                );
                lastSize = curSize;
            }
        };
        try {
            this.server.getTimer().schedule(handleTimerTask, 0, 3000L);

            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(targetFile.getAbsolutePath()));
            int currentRead;
            byte[] bufferArray = new byte[4096];
            while (FileUtils.sizeOf(targetFile) < header.getFileSize() &&
                (currentRead = inputStream.read(bufferArray)) != -1) {
                dataOutputStream.write(bufferArray, 0, currentRead);
            }

            System.out.println(
                "File has been delivered\n" +
                    "Filename size: " + targetFile.getName().length() + "\n" +
                    "Filename: " + targetFile.getName() + "\n" +
                    "File size: " + FileUtils.sizeOf(targetFile)
            );

            outputStream.write(ExecutionStatus.SUCCESS.getOrdinal());
            outputStream.flush();
        } finally {
            handleTimerTask.cancel();
        }
    }

}
