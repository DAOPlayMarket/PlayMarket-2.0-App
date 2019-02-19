package com.blockchain.store.playmarket.utilities.ipfs;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.IpfsData;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import ir.mahdi.mzip.zip.ZipArchive;
import kotlin.text.Charsets;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class IPFSDaemon {
    private static final String TAG = "IPFSDaemon";

    private Context context;
    private static File getBinaryFile;
    private static File getRepoPath;
    private static File getVersionFile;
    private static IPFSDaemon instance;
    private static Process ipfsProcess;
    private static boolean isRunning;
    private int progress = 0;
    private IpfsData ipfsData = new IpfsData(getDownloadLink());

    public static IPFSDaemon getInstance() {
        if (instance != null) return instance;
        return new IPFSDaemon();
    }

    public static Process getIpfsProcess() {
        return ipfsProcess;
    }

    public static void setIpfsProcess(Process ipfsProcess) {
        IPFSDaemon.ipfsProcess = ipfsProcess;
    }

    public IPFSDaemon() {
        this.context = Application.getInstance().getApplicationContext();
        initFiles();
    }

    private void initFiles() {
        getBinaryFile = new File(getFilePath(), "ipfsbin");
        getRepoPath = new File(getFilePath(), ".ipfs_repo");
        getVersionFile = new File(getFilePath(), ".ipfs_version");
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

    private File getFile(boolean isNeedZipFile) {
        File file;
        String fileName = isNeedZipFile ? "ipfs.zip" : "ipfs";
        file = new File(getFilePath(), fileName);
        return file;
    }

    private String getFilePath() {
        return context.getFilesDir().getPath();
    }

    public static Process run(String cmd) {
        String[] env = new String[]{
                "IPFS_PATH=" + getRepoPath.getAbsoluteFile()
        };
        String command = getBinaryFile.getAbsolutePath() + " " + cmd;
        try {
            ipfsProcess = Runtime.getRuntime().exec(command, env);

            InputStreamReader inputStreamReader = new InputStreamReader(ipfsProcess.getInputStream(), Charsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bufferedReader.lines().forEachOrdered(result -> Log.d(TAG, "run: " + result));
            } else {
                Log.d(TAG, "run: " + bufferedReader.readLine());
            }


            InputStreamReader inputStreamReader2 = new InputStreamReader(ipfsProcess.getErrorStream(), Charsets.UTF_8);
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bufferedReader2.lines().forEachOrdered(result -> Log.d(TAG, "error: " + result));
            } else {
                Log.d(TAG, "error: " + bufferedReader.readLine());
            }


            return ipfsProcess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void initDaemon() {
        ipfsProcess = this.run("init");
        NotificationManager.getManager().downloadCompleteWithoutError(ipfsData);
    }


    public void downloadDaemon() {
        NotificationManager.getManager().registerNewNotification(ipfsData);
        Ion.with(context)
                .load(getDownloadLink())
                .progress((downloaded, total) -> {
                    int tempProgress = (int) ((double) downloaded / total * 100);
                    if (tempProgress > progress) {
                        progress = tempProgress;
                        Log.d(TAG, "progress: downloaded: " + downloaded + ". Total: " + total + ". progress " + progress);
                        NotificationManager.getManager().updateProgress(ipfsData, progress);
                    }
                })
                .write(getFile(true)).setCallback((e, result) -> {

            ZipArchive.unzip(getFile(true).getAbsolutePath(), getFile(true).getParent(), "");

            try {

                BufferedSource arm = Okio.buffer(Okio.source(new File(getFilePath(), "ipfs")));
                BufferedSink buffer = Okio.buffer(Okio.sink(getBinaryFile));
                while (!arm.exhausted()) {
                    arm.read(buffer.buffer(), 1024);
                }
                arm.close();
                buffer.close();
            } catch (Exception e1) {
                NotificationManager.getManager().downloadCompleteWithError(ipfsData, e1);
                e1.printStackTrace();
            }
            getBinaryFile.setExecutable(true);
            initDaemon();
        });
    }

    public boolean isDaemonDownloaded() {
        File file = getFile(false);
        return file != null && file.isFile();
    }
}
