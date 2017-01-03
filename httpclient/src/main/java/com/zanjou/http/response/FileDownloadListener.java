package com.zanjou.http.response;

import java.io.File;

/**
 * Created by ander on 6/09/16.
 */
public abstract class FileDownloadListener {

    static final String TAG = "FileDownloadListener";

    public abstract void onDownloadingFile(File file, long size, long downloaded);

    public void onDownloadStart() {

    }

    public void onDownloadCancel() {

    }

    public void onDownloadFinish() {

    }
}
