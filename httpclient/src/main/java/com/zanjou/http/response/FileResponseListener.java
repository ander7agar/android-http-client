package com.zanjou.http.response;

import android.support.annotation.NonNull;

import com.zanjou.http.request.ResponseData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ander on 6/09/16.
 */
public abstract class FileResponseListener extends BaseResponseListener {

    private static final String TAG = "FileResponseListener";

    private File file;

    public FileResponseListener(@NonNull File file) {
        this.file = file;
    }

    public FileResponseListener(@NonNull String path) {
        this(new File(path));
    }

    public File getFile() {
        return file;
    }

    @Override
    public void onErrorResponse(ResponseData responseData) {

    }

    @Override
    public void onOkResponse(ResponseData responseData) {
        try {
            if (file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(responseData.getData());
                fileOutputStream.flush();
                fileOutputStream.close();
            } else {
                throw new IOException("Cannot write file " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            onError(e, file);
        }
        onOkResponse(file);
    }

    public abstract void onOkResponse(File file);

    public abstract void onError(IOException exception, File file);
}
