package client;

import domain.ExecutionStatus;
import domain.Header;
import domain.ProtocolSerializer;
import exceptions.HeaderSerializationException;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Client {

    private String filePath;

    private InetAddress serverAddress;

    private int serverPort;

    private Client(String filePath, String serverAddress, String serverPort) throws UnknownHostException {
        this.filePath = filePath;
        this.serverAddress = InetAddress.getByName(serverAddress);
        this.serverPort = Integer.parseInt(serverPort);

        this.start();
    }

    public static void main(String[] args) throws UnknownHostException {
        new Client(args[0], args[1], args[2]);
    }

    private void start() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                throw new IllegalArgumentException("This file doesn't exists!");
            }

            Header fileHeader = createHeader(targetFile);
            byte[] serializedHeader = ProtocolSerializer.serialize(fileHeader);

            sendFile(socket, serializedHeader, targetFile);
        } catch (IOException | HeaderSerializationException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendFile(Socket socket, byte[] serializedHeader, File targetFile) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(serializedHeader, 0, serializedHeader.length);

        InputStream inputStream = socket.getInputStream();
        ExecutionStatus status = ExecutionStatus.getByOrdinal(inputStream.read());
        if (Objects.equals(status, ExecutionStatus.CANCELLED)) {
            System.err.println("Sending failed!");
            throw new IllegalArgumentException();
        } else if (Objects.equals(status, ExecutionStatus.READY_FOR_RECEIVE)) {
            System.out.println("Server ready for receive!");
        } else {
            throw new IllegalArgumentException();
        }

        DataInputStream fileInputStream = new DataInputStream(new FileInputStream(targetFile.getAbsolutePath()));

        int currentRead;
        byte[] bufferArray = new byte[4096];
        while ((currentRead = fileInputStream.read(bufferArray)) != -1) {
            outputStream.write(bufferArray, 0, currentRead);
        }

        status = ExecutionStatus.getByOrdinal(inputStream.read());
        if (Objects.equals(status, ExecutionStatus.SUCCESS)) {
            System.err.println("Sending succeeded!");
        } else {
            throw new RuntimeException();
        }
    }

    private Header createHeader(File targetFile) {
        return new Header(
            filePath.getBytes(StandardCharsets.UTF_8).length,
            filePath,
            FileUtils.sizeOf(targetFile)
        );
    }

}
