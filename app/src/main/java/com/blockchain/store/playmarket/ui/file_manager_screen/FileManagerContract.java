package com.blockchain.store.playmarket.ui.file_manager_screen;


import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class FileManagerContract {

    interface View{

        void showCreateFolderDialog(String folderName);
    }

    interface Presenter{

        void init(FileManagerContract.View view, Context context);

        ArrayList<File> getFolderList(String path, String type);

        void createFolder(String currentDirectory, String folderName);

        void saveJsonAccountOnDevice(String currentDirectory);

        String getDataFromJsonKeystoreFile(File file, String type);

        String jsonKeystoreFileCheck(File file, String type);

    }
}
