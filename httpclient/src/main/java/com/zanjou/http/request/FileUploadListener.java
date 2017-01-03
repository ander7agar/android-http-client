package com.zanjou.http.request;

import java.io.File;

/**
 * Created by ander on 4/07/16.
 */
public abstract class FileUploadListener {


    public abstract void onUploadingFile(File file, long size, long uploaded);

    public void onUploadStart() {

    }

    public void onUploadCancel() {

    }

    public void onUploadFinish(File file) {

    }
}
