package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;


class LoginPromptContract {

    interface View {

        void showImportUserDialog();

        void showToast(Boolean success);

        void showDialogConfirmImport(String fileData);

    }

    interface Presenter {

        void init(LoginPromptContract.View view);

        boolean confirmImportButtonPressed(String fileString, String password);

        void autoSaveJsonKeystoreFile();

        String getDataFromJsonKeystoreFile(File file, String type);

        ArrayList<File> getJsonKeystoreCollection();

        boolean checkJsonFileExists();

    }
}
