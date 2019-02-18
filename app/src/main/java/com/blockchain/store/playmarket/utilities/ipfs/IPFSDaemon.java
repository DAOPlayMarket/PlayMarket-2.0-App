package com.blockchain.store.playmarket.utilities.ipfs;

import android.content.Context;
import android.os.Build;

import com.blockchain.store.playmarket.Application;

import java.io.File;
import java.io.IOException;

public class IPFSDaemon {
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

    }

    public boolean isDaemonDownloaded() {
        return false;

    }
}
