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

    @Override
    public void init(LoginPromptContract.View view, Context context) {
        this.view = view;
        this.context = context;
        fileUtils = new FileUtils();
    }

    @Override
    public void confirmImportButtonPressed(String fileString, String password) {
        fileUtils.confirmImport(fileString, password);
    }

    @Override
    public boolean checkJsonFileExists() {
        return fileUtils.checkJsonKeystoreFile();
    }

    @Override
    public void autoSaveJsonKeystoreFile(String URL) {
        fileUtils.saveJsonKeystoreFile(URL);
    }

    @Override
    public String getDataFromJsonKeystoreFile(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }

    @Override
    public ArrayList<File> getJsonKeystoreCollection() {
        return fileUtils.getJsonKeystoreFileList();
    }
}
