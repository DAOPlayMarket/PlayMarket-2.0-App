package com.blockchain.store.playmarket.utilities.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by samsheff on 15/09/2017.
 */

public class ZipUtils {

    public static ZipUtils zip;

    private ZipFile zipFile;

    public ZipUtils(File file) throws IOException {
        zipFile = new ZipFile(file);

        zip = this;
    }

    public JSONObject getAppInfo() throws IOException, JSONException {
        ZipEntry appInfoEntry = zipFile.getEntry("appinfo.json");
        InputStream appInfoStream = zipFile.getInputStream(appInfoEntry);

        String appInfoText = "";
        if ( appInfoStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(appInfoStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            appInfoStream.close();
            appInfoText = stringBuilder.toString();
        }

        return new JSONObject(appInfoText);
    }

}
