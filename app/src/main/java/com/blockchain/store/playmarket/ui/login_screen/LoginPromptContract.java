package com.blockchain.store.playmarket.ui.login_screen;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class LoginPromptContract {

    interface View{

        void showImportUserDialog();

    }

    interface Presenter{

        void init(LoginPromptContract.View view);

        String getDataFromJsonKeystoreFile(File file, String type);

        ArrayList<File> getJsonKeystoreCollection();

        boolean checkJsonFileExists();

        void saveJsonDataOnViewModel(LoginViewModel loginViewModel, String jsonData);
    }
}
