package domain;

import exceptions.HeaderDeserializationException;
import exceptions.HeaderSerializationException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ProtocolSerializer {

    private static final int FILENAME_MAX_SIZE = 4096;

    public static int HEADER_SIZE = FILENAME_MAX_SIZE + Integer.BYTES + Long.BYTES;

    private static final long TERRABYTE = 1024L * 1024 * 1024 * 1024;

    public static byte[] serialize(Header header) throws HeaderSerializationException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE);

        if (header.getFilenameSize() > 4096) {
            throw new HeaderSerializationException();
        }
        byteBuffer.putInt(header.getFilenameSize());

        byte[] insertionFilename = new byte[FILENAME_MAX_SIZE];
        byte[] filenameByteArray = header.getFilename().getBytes(StandardCharsets.UTF_8);
        Arrays.fill(insertionFilename, (byte) 0);
        System.arraycopy(filenameByteArray, 0, insertionFilename, 0, filenameByteArray.length);
        if (filenameByteArray.length != header.getFilenameSize()) {
            throw new HeaderSerializationException();
        }
        byteBuffer.put(insertionFilename);

        if (header.getFileSize() > TERRABYTE) {
            throw new HeaderSerializationException();
        }
        byteBuffer.putLong(header.getFileSize());

        return byteBuffer.array();
    }

    public static Header deserialize(byte[] serializedHeader) throws HeaderDeserializationException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(serializedHeader);

        int filenameSize = byteBuffer.getInt();
        if (filenameSize > FILENAME_MAX_SIZE) {
            throw new HeaderDeserializationException();
        }

        byte[] filenameByteArray = new byte[FILENAME_MAX_SIZE];
        for (int i = 0; i < FILENAME_MAX_SIZE; ++i) {
            filenameByteArray[i] = byteBuffer.get();
        }
        String filename = new String(filenameByteArray, 0, filenameSize, StandardCharsets.UTF_8);
        if (filename.getBytes(StandardCharsets.UTF_8).length != filenameSize) {
            throw new HeaderDeserializationException();
        }

        long fileSize = byteBuffer.getLong();
        if (fileSize > TERRABYTE) {
            throw new HeaderDeserializationException();
        }

        return new Header(filenameSize, filename, fileSize);
    }

}
