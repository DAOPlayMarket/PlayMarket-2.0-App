package com.blockchain.store.playmarket.ui.file_manager_screen;


import java.io.File;
import java.util.ArrayList;

public class FileManagerContract {

    interface View{

        void showToast(Boolean success);

        void showCreateFolderDialog(String folderName);
    }

    interface Presenter{

        void init(FileManagerContract.View view);

        ArrayList<File> getFolderList(String path, String type);

        void createFolderButtonPressed(String currentDirectory, String folderName);

        void confirmSaveButtonPressed(String currentDirectory);

        String getDataFromJsonKeystoreFile(File file, String type);

        String jsonKeystoreFileCheck(File file, String type);

    }
}
