package com.blockchain.store.playmarket.ui.new_user_welcome_activity;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewUserWelcomePresenter implements NewUserWelcomeContract.Presenter {

    private NewUserWelcomeContract.View view;
    private Context context;

    @Override
    public void init(NewUserWelcomeContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    // Реализация метода сохранения копии ключа на устройстве.
    @Override
    public void saveKeyOnDevice(String accountKey) {
        final String directory = "/PlayMarket2.0/keys/";
        final String fileName = "account_key.txt";
        File folder = new File(Environment.getExternalStorageDirectory() + directory);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + directory, fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(accountKey.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Override
    public void saveKeyOnEmail() {

    }

    @Override
    public void saveKeyOnCloud() {

    }
}
