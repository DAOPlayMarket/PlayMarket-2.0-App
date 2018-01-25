package com.blockchain.store.playmarket.utilities.installer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.net.HttpDownloadUtility;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samsheff on 23/08/2017.
 */

public class ApkInstaller extends AsyncTask<String,Void,Void> {
    private Context context;
    public void setContext(Context contextf){
        context = contextf;
    }

    public boolean successful = true;
    public boolean isDownloading = true;

    public static String APK_MIME_TYPE = "application/vnd.android.package-archive";
    public static String APK_FILENAME = "app.apk";

    @Override
    protected Void doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);

            File outputFile;
            String filePath;
            Uri contentUri;

            if (BuildUtils.shouldUseContentUri()) {
                File path = context.getFilesDir();
                outputFile = new File(path, "app.apk");
            } else {
                outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app.apk");
            }

//            File file = new File(filePath);
//            file.mkdirs();
//            outputFile = new File(file, APK_FILENAME);

            if(outputFile.exists()){
                outputFile.delete();
            }

            HttpDownloadUtility downloader = new HttpDownloadUtility();
            try {
                if (!downloader.downloadFile(arg0[0], outputFile)) {
                    downloader.downloadFile(arg0[1], outputFile);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (BuildUtils.shouldUseContentUri()) {
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.blockchain.store.playmarket.fileprovider", outputFile), APK_MIME_TYPE);
            } else {
                intent.setDataAndType(Uri.parse("file://" + outputFile), APK_MIME_TYPE);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);


        } catch (Exception e) {
            Log.e("APK", "Install error! " + e.getMessage());
        }

        isDownloading = false;

        return null;
    }

    private Uri getUriForApk(File file) {
        if (BuildUtils.shouldUseContentUri()) {
            return Uri.parse("content://" + file);
        } else {
            return Uri.parse("file://" + file);
        }
    }
}
