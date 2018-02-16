package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.FileUtils;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginPromptPresenter implements LoginPromptContract.Presenter {

    private LoginPromptContract.View view;
    private Context context;

    private FileUtils fileUtils;

    private final String DIRECTORY = "/PlayMarket2.0/Accounts/";

    @Override
    public void init(LoginPromptContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    // Реализация метода проверки Json файла аккаунта в папке "Accounts"
    @Override
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

    @Override
    public void importJsonKeystoreFile(File file, String password) {

    }

    @Override
    public ArrayList<File> getJsonKeystoreFileList() {
        ArrayList<File> jsonAccounts = new ArrayList<>();
        // В поле типа File указываем путь к локальному хранилищу (каталогу) ключей.
        File folder = new File(Environment.getExternalStorageDirectory() + DIRECTORY);
        for(File file : folder.listFiles()){
            if (file.isFile() && CryptoUtils.readJsonKeystoreFile(file, "accounts") != null) jsonAccounts.add(file);
        }
        // Запрашиваем количество файлов хранящихся в каталоге.
        return jsonAccounts;
    }

    // Реализация метода сохранения копии Json ключа на устройстве.
    @Override
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
}
