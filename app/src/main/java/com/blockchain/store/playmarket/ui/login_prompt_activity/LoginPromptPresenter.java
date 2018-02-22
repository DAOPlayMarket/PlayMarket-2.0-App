package com.blockchain.store.playmarket.ui.login_prompt_activity;

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
    public boolean confirmImportButtonPressed(String fileString, String password) {
        return fileUtils.confirmImport(fileString, password);
    }

    @Override
    public boolean checkJsonFileExists() {
        return fileUtils.checkJsonKeystoreFile();
    }

    @Override
    public void autoSaveJsonKeystoreFile() {
        String newDirectoryPath = fileUtils.createNewFolder();
        Boolean success = fileUtils.saveJsonKeystoreFile(newDirectoryPath);
        view.showToast(success);
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
