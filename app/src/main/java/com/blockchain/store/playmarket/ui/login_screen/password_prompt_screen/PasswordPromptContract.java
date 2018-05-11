package com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen;

import android.content.Context;

public class PasswordPromptContract {

    public enum sensorState {
        NOT_SUPPORTED,
        NOT_BLOCKED,
        NO_FINGERPRINTS,
        READY
    }

    interface View {

        void showToast(Boolean success);
    }

    interface Presenter{

        void init (PasswordPromptContract.View view);

        String createNewAccount(String password);

        boolean importAccount(String fileString, String password);

        void autoSaveJsonKeystoreFile();

        sensorState checkSensorState(Context context);
    }
}
