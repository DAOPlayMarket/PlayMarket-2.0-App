package com.blockchain.store.playmarket.utilities;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileUtils {

    // Метод чтения данных из "JsonKeystoreFile".
    public static String readJsonKeystoreFile(File file, String type){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String jsonKeystoreFileData = bufferedReader.readLine();

            if (Objects.equals(type, "all_data")) return jsonKeystoreFileData;

            if (Objects.equals(type, "address")) {
                JSONObject jsonObject = new JSONObject(jsonKeystoreFileData);
                return jsonObject.getString("address");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
