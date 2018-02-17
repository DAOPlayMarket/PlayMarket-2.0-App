package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;


class LoginPromptContract {

    interface View{

        void showImportUserDialog();

        void showDialogConfirmImport(String fileData);

    }

    interface Presenter{

        void init(LoginPromptContract.View view, Context context);

        void confirmImportButtonPressed(String fileString, String password);

        boolean checkJsonFileExists();

        void autoSaveJsonKeystoreFile(String URL);

        String getDataFromJsonKeystoreFile(File file, String type);

        ArrayList<File> getJsonKeystoreCollection();

    }
}
