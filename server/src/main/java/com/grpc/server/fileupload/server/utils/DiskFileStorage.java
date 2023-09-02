package com.grpc.server.fileupload.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author A.Sarang Kumar Tak
 * @youtubechannelname Dev Problems
 */
public class DiskFileStorage {

    private final ByteArrayOutputStream byteArrayOutputStream;

    public DiskFileStorage() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }


    public ByteArrayOutputStream getStream() {
        return this.byteArrayOutputStream;
    }

    public void write(String fileNameWithType) throws IOException {
        String DEFAULT_PATH = "output/";
        try (FileOutputStream fileOutputStream = new FileOutputStream(DEFAULT_PATH.concat(fileNameWithType))) {
            this.byteArrayOutputStream.writeTo(fileOutputStream);
        }
    }

    public void close() throws IOException {
        this.byteArrayOutputStream.close();
    }
}
