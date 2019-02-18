package com.blockchain.store.playmarket.utilities.ipfs;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class IPFSDaemon {
    private static final String TAG = "IPFSDaemon";

    private Context context;
    private File getBinaryFile;
    private File getRepoPath;
    private File getVersionFile;

    public IPFSDaemon() {
        this.context = Application.getInstance().getApplicationContext();
        initFiles();
    }

    private void initFiles() {
        getBinaryFile = new File(context.getFilesDir(), "ipfsbin");
        getRepoPath = new File(context.getFilesDir(), ".ipfs_repo");
        getVersionFile = new File(context.getFilesDir(), ".ipfs_version");
    }

    private String getBinaryFileByAbi() {
        String abi = Build.CPU_ABI;
        if (abi.toLowerCase().startsWith("x86")) return "x86";
        if (abi.toLowerCase().startsWith("arm")) return "arm";
        return abi;
    }

    private String getDownloadLink() {
        if (getBinaryFileByAbi().equalsIgnoreCase("x86")) {
            return Constants.IPFS_BINARIES_PATH_X86;
        } else {
            return Constants.IPFS_BINARIES_PATH_ARM;
        }
    }

    private File getFile() {
        Context context = Application.getInstance().getApplicationContext();
        File file;
        if (BuildUtils.shouldUseContentUri()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(path, "ipfs");
        } else {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "ipfs");
        }
        return file;
    }

    public Process run(String cmd) {
        String[] env = new String[]{
                "IPFS_PATH=" + getRepoPath.getAbsoluteFile()
        };
        String command = getBinaryFile.getAbsolutePath() + " " + cmd;
        try {
            return Runtime.getRuntime().exec(command, env);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void initDaemon() {
        this.run("init");
    }

    public void runDaemon() {
        this.run("run");
    }

    public void downloadDaemon() {

        Ion.with(context)
                .load(getDownloadLink())
                .progress((downloaded, total) -> Log.d(TAG, "onProgress() called with: downloaded = [" + downloaded + "], total = [" + total + "]"))
                .write(getFile()).setCallback((e, result) -> {
            Log.d(TAG, "onCompleted() called with: e = [" + e + "], result = [" + result + "]");
            try {
                ZipFile zipFile = new ZipFile(result);
                zipFile.entries().as
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public boolean isDaemonDownloaded() {
        return false;

    }
}
