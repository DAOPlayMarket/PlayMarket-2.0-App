package com.blockchain.store.playmarket.ui.login_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import com.blockchain.store.playmarket.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class LoginPromptPresenter implements LoginPromptContract.Presenter {

    private LoginPromptContract.View view;

    private FileUtils fileUtils;

    @Override
    public void init(LoginPromptContract.View view) {
        this.view = view;
        fileUtils = new FileUtils();
    }

    @Override
    public String getDataFromJsonKeystoreFile(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }

    @Override
    public ArrayList<File> getJsonKeystoreCollection() {
        return fileUtils.getJsonKeystoreFileList();
    }

    @Override
    public boolean checkJsonFileExists() {
        return fileUtils.checkJsonKeystoreFile();
    }

    @Override
    public void saveJsonDataOnViewModel(LoginViewModel loginViewModel, String jsonData) {
        loginViewModel.jsonData.setValue(jsonData);
    }
}
