package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;


class LoginPromptContract {

    interface View{

        void showImportUserDialog();

    }

    interface Presenter{

        void init(LoginPromptContract.View view, Context context);

        // Объявление метода проверки существования Json аккаунта на устройстве.
        boolean checkJsonKeystoreFile();

        void importJsonKeystoreFile(File file, String password);

        ArrayList<File> getJsonKeystoreFileList();

        // Объявление метода сохранения Json аккаунта на устройстве.
        void saveJsonKeystoreFile(String URL);

    }
}
