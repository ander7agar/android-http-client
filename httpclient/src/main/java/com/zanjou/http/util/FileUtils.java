package com.zanjou.http.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by ander on 13/06/16.
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        Log.e(TAG, "FILE URI = " + uri.toString());
        String scheme = uri.getScheme();
        String path = null;
        if (scheme.equalsIgnoreCase("content")) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {

                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndex("_data");
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (scheme.equalsIgnoreCase("file")) {
            path = uri.getPath();
        }

        Log.e(TAG, "FILE PATH = " + path);
        return path;
    }

    public static File file(String path) {
        return new File(path);
    }

    public static File file(Context context, Uri uri) throws URISyntaxException {
        return file(getPath(context, uri));
    }

    public static String getName(Context context, Uri uri) throws URISyntaxException {
        String path = getPath(context, uri);
        File f = new File(path);
        return f.getName();
    }

    public static String getContentType(File f) {
        String ext = getExtension(f);
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

        if (TextUtils.isEmpty(mimeType)) {
            mimeType = "*/*";
        }

        return mimeType;
    }

    public static String getExtension(String path) {
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            return path.substring(index+1, path.length());
        } else {
            return "";
        }
    }

    public static String getExtension (File file) {
        return getExtension(file == null ? "" : file.getAbsolutePath());
    }

    public static String toBase64(String path) throws IOException {
        return  toBase64(new File(path));
    }

    public static String toBase64(File file) {

        byte[] bytes;
        try {
            bytes = loadFile(file);
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);

            return new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public static File toFile(byte[] data, String path) throws IOException {
        return toFile(data, new File(path));
    }

    public static File toFile(byte[] data, File file) throws IOException {
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(data);
        fo.flush();
        fo.close();

        return file;
    }

}
