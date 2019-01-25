package com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen;

import android.app.KeyguardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FileUtils;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import org.ethereum.geth.Account;

public class PasswordPromptPresenter implements PasswordPromptContract.Presenter {

    private static final String TAG = "LoginPromptActivityOld";

    private PasswordPromptContract.View view;
    private Context context;

    private FileUtils fileUtils;

    @Override
    public void init(PasswordPromptContract.View view, Context context) {
        this.view = view;
        this.context = context;
        fileUtils = new FileUtils();
    }

    @Override
    public String createNewAccount(String accountPassword) {
        try {
            Account account = AccountManager.getKeyManager().newAccount(accountPassword);
            int size = AccountManager.getKeyManager().getAccounts().size();
            if (size > 1) {
                size--;
            }
            AccountManager.setCurrentUserPosition(size);
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
        try {
            int size = AccountManager.getKeyManager().getAccounts().size();
            if (size > 1) {
                size--;
            }
            AccountManager.setCurrentUserPosition(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUtils.importJsonKeystoreFile(fileString, password);
    }

    @Override
    public void autoSaveJsonKeystoreFile() {
        String newDirectoryPath = fileUtils.createNewFolder();
        Boolean success = fileUtils.saveJsonKeystoreFile(newDirectoryPath);
        if (success) ToastUtil.showToast(R.string.success_autosave_message);
        else ToastUtil.showToast(R.string.failed_autosave_message);
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

    public boolean checkPasswordForNewAccount(String accountPassword) {
        if (accountPassword.equals("")) {
            view.showPasswordError(context.getResources().getString(R.string.empty_password));
            return false;
        } else if (accountPassword.length() < 7) {
            view.showPasswordError(context.getResources().getString(R.string.short_password));
            return false;
        }
        return true;
    }


    public static boolean checkFingerprintCompatibility(@NonNull Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }
}
