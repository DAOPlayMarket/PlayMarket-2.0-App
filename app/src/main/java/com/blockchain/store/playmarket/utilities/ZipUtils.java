package com.blockchain.store.playmarket.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/* Source code from https://stackoverflow.com/questions/4504291/how-to-speed-up-unzipping-time-in-java-android */
public class ZipUtils {
    private static final String TAG = "ZipUtils";

    private File _zipFile;
    private String _location;

    public ZipUtils(File zipFile, String location) {
        _zipFile = zipFile;
        _location = location;
        _dirChecker("");
    }

    public void unzip() {
        try {
            FileInputStream fin = new FileInputStream(_zipFile);

            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());
                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }
                    bufout.close();
                    zin.closeEntry();
                    fout.close();
                }
            }
            zin.close();
            Log.d("Unzip", "Unzipping complete. path :  " + _location);
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
            Log.d("Unzip", "Unzipping failed");
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
