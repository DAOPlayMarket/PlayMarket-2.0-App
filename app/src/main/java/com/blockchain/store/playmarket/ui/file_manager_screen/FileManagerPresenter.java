package com.blockchain.store.playmarket.ui.file_manager_screen;

import com.blockchain.store.playmarket.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class FileManagerPresenter implements FileManagerContract.Presenter {

    private FileManagerContract.View view;

    private FileUtils fileUtils;

    @Override
    public void init(FileManagerContract.View view) {
        this.view = view;
        fileUtils = new FileUtils();
    }

    @Override
    public ArrayList<File> getFolderList(String path, String type) {
        return fileUtils.getFileList(path, type);
    }

    @Override
    public void createFolderButtonPressed(String currentDirectory, String folderName) {
        fileUtils.createNewFolder(currentDirectory, folderName);
    }

   @Override
   public void confirmSaveButtonPressed(String currentDirectory) {
       Boolean success = fileUtils.saveJsonKeystoreFile(currentDirectory);
       view.showToast(success);
   }

    @Override
    public String getDataFromJsonKeystoreFile(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }

    @Override
    public String jsonKeystoreFileCheck(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }
}
