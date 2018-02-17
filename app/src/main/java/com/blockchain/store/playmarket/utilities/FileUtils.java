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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

// Класс для работы с файлами "JSON Keystore File".
public class FileUtils {

    private static final String TAG = "LoginPromptActivity";
    private final String DIRECTORY = "/PlayMarket2.0/Accounts/";

    // Метода сохранения копии Json ключа на устройстве.
    public void saveJsonKeystoreFile(String URL) {
        // Создаём новый файл-каталог "/PlayMarket2.0/keys/" для хранения ключа.
        File folder = new File(Environment.getExternalStorageDirectory() + DIRECTORY);
        boolean success = true;
        // Проверяем существование каталога и если его не существует, то создаём.
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        // В случае успешного создания каталога или его существования, осуществляем сохранение ключа на устройство.
        if (success) {
            try {
                // Создадим поле для хранения пути к файлу в памяти приложения.
                final String pathToJsonFile = URL.replace("keystore:///", "");
                // Создадим новый файл, используя созданное выше поле.
                File jsonFile = new File(pathToJsonFile);
                // Читаем содержимое файла.
                FileInputStream inputStream = new FileInputStream(jsonFile);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                // Создадим поле для хранения содержимого файла.
                String jsonText = new String(bytes);

                // В каталоге "/PlayMarket2.0/keys/" создадим новый файл.
                File copyJsonFile = new File(Environment.getExternalStorageDirectory() + DIRECTORY, jsonFile.getName());
                FileOutputStream outputStream = new FileOutputStream(copyJsonFile);
                // Запишем в созданный файл данные из поля "jsonText".
                outputStream.write(jsonText.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод чтения данных из "JsonKeystoreFile".
    public String readJsonKeystoreFile(File file, String type){
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
        File folder = new File(Environment.getExternalStorageDirectory() + DIRECTORY);
        for(File file : folder.listFiles()){
            if (file.isFile() && readJsonKeystoreFile(file, "address") != null) jsonAccounts.add(file);
        }
        // Запрашиваем количество файлов хранящихся в каталоге.
        return jsonAccounts;
    }

    // Метод импортирования данных из JSON Keystore File.
    public void confirmImport(String fileString, String password) {
        try {
            Account account = Application.keyManager.getKeystore().importKey(fileString.getBytes(), password, password);
            Log.d(TAG, "confirmImport: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Реализация метода проверки Json файла аккаунта в папке "Accounts"
    public boolean checkJsonKeystoreFile() {
        // Устанавливаем начальный счётчик файлов на 0.
        int fileCount = 0;
        // В поле типа File указываем путь к локальному хранилищу (каталогу) ключей.
        File folder = new File(Environment.getExternalStorageDirectory() + DIRECTORY);
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
