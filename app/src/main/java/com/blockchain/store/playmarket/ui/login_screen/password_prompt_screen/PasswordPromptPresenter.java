package com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen;

import android.app.KeyguardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.utilities.FileUtils;

import org.ethereum.geth.Account;

public class PasswordPromptPresenter implements PasswordPromptContract.Presenter {

    private static final String TAG = "LoginPromptActivityOld";

    private PasswordPromptContract.View view;

    private FileUtils fileUtils;

    @Override
    public void init(PasswordPromptContract.View view) {
        this.view = view;
        fileUtils = new FileUtils();
    }

    @Override
    public String createNewAccount(String password) {
        try {
            Account account = Application.keyManager.newAccount(password);
            Log.d(TAG, "makeNewAccount: " + account.getURL().toString());
            String address = account.getAddress().getHex();
            autoSaveJsonKeystoreFile();
            Log.d(TAG, address);
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean importAccount(String fileString, String password) {
        return fileUtils.importJsonKeystoreFile(fileString, password);
    }

    @Override
    public void autoSaveJsonKeystoreFile() {
        String newDirectoryPath = fileUtils.createNewFolder();
        Boolean success = fileUtils.saveJsonKeystoreFile(newDirectoryPath);
        view.showToast(success);
    }

    @Override
    public PasswordPromptContract.sensorState checkSensorState(Context context) {
        if (checkFingerprintCompatibility(context)) {

            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            assert keyguardManager != null;
            if (!keyguardManager.isKeyguardSecure()) {
                return PasswordPromptContract.sensorState.NOT_BLOCKED;
            }

            if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
                return PasswordPromptContract.sensorState.NO_FINGERPRINTS;
            }

            return PasswordPromptContract.sensorState.READY;

        } else {
            return PasswordPromptContract.sensorState.NOT_SUPPORTED;
        }
    }

    public static boolean checkFingerprintCompatibility(@NonNull Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }
}
