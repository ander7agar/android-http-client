package com.zanjou.http.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteStream {

    private static final String TAG = "FileUtils";

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        byte[] bytes = toByteArray(is);

        is.close();
        return bytes;
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();

    }
}