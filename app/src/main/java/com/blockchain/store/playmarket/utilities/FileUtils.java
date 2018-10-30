package com.blockchain.store.playmarket.utilities;


import android.os.Environment;
import android.util.Log;

import com.blockchain.store.playmarket.Application;

import org.ethereum.geth.Account;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class FileUtils {

    private static final String TAG = "LoginPromptActivityOld";
    private static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String DEFAULT_DIRECTORY_NAME = "/PlayMarket2.0/Accounts/";

    /**
     * Метод создания новой директории.
     */
    public String createNewFolder(String currentDirectory, String directoryName) {
        File newFolder = new File(currentDirectory + "/" + directoryName);
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }
        return newFolder.getPath();
    }

    public String createNewFolder() {
        return createNewFolder(DEFAULT_PATH, DEFAULT_DIRECTORY_NAME);
    }

    public boolean saveJsonKeystoreFile(String currentDirectory) {
        try {
            String jsonKeystoreFileURL = AccountManager.getAccount().getURL();
            final String pathToJsonFile = jsonKeystoreFileURL.replace("keystore:///", "");
            File jsonFile = new File(pathToJsonFile);
            FileInputStream inputStream = new FileInputStream(jsonFile);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String jsonText = new String(bytes);
            File copyJsonFile = new File(currentDirectory, jsonFile.getName());
            FileOutputStream outputStream = new FileOutputStream(copyJsonFile);
            outputStream.write(jsonText.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Метод чтения данных из "JsonKeystoreFile".
    public String readJsonKeystoreFile(File file, String type) {
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

    // Метод получения коллекции файлов "JSON Keystore File" из каталога "/PlayMarket2.0/Accounts/"
    public ArrayList<File> getJsonKeystoreFileList() {
        ArrayList<File> jsonAccounts = new ArrayList<>();
        // В поле типа File указываем путь к локальному хранилищу (каталогу) ключей.
        File folder = new File(DEFAULT_PATH + DEFAULT_DIRECTORY_NAME);
        for (File file : folder.listFiles()) {
            if (file.isFile() && readJsonKeystoreFile(file, "address") != null)
                jsonAccounts.add(file);
        }
        // Запрашиваем количество файлов хранящихся в каталоге.
        return jsonAccounts;
    }

    // Метод получения папок.
    public ArrayList<File> getFileList(String directoryPath, String type) {
        File directory = new File(directoryPath);
        ArrayList<File> allFilesList = new ArrayList<>();
        ArrayList<File> fileList = new ArrayList<>();

        allFilesList.addAll(Arrays.asList(directory.listFiles()));
        if (type.equals("all_files")) fileList = allFilesList;

        if (type.equals("folders")) {
            for (int i = 0; i < allFilesList.size(); i++) {
                if (allFilesList.get(i).isDirectory()) fileList.add(allFilesList.get(i));
            }
        }

        Collections.sort(fileList, (file, file2) -> {
            if (file.isDirectory() && file2.isFile())
                return -1;
            else if (file.isFile() && file2.isDirectory())
                return 1;
            else
                return file.getPath().toLowerCase().compareTo(file2.getPath().toLowerCase());
        });
        return fileList;
    }

    // Метод импортирования данных из JSON Keystore File.
    public boolean importJsonKeystoreFile(String fileString, String password) {
        try {
            Account account = AccountManager.getKeyStore().importKey(fileString.getBytes(), password, password);
            Log.d(TAG, "importJsonKeystoreFile: ");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Реализация метода проверки Json файла аккаунта в папке "Accounts"
    public boolean checkJsonKeystoreFile() {
        // Устанавливаем начальный счётчик файлов на 0.
        int fileCount = 0;
        // В поле типа File указываем путь к локальному хранилищу (каталогу) ключей.
        File folder = new File(DEFAULT_PATH + DEFAULT_DIRECTORY_NAME);
        // Запрашиваем количество файлов хранящихся в каталоге.
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileCount++;
                    break;
                }
            }
        }
        // Если количество файлов превышает 1, возвращаем true,
        return fileCount != 0;
    }
}
