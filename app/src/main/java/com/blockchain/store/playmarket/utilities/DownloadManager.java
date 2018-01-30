package com.blockchain.store.playmarket.utilities;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class DownloadManager extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadManager(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        File file;
        if (BuildUtils.shouldUseContentUri()) {
            File path = getApplicationContext().getFilesDir();
            file = new File(path, "app.apk");
        } else {
            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app.apk");
        }
        if (file.exists()) {
            file.delete();
        }
// show notification
        Ion.with(getBaseContext())
                .load("")
                .progress((downloaded, total) -> {
                    //update notification
                    // send localBroadCast with percent;
                }).write(file).setCallback(new FutureCallback<File>() {
            @Override
            public void onCompleted(Exception e, File result) {

            }
        });

    }
}
