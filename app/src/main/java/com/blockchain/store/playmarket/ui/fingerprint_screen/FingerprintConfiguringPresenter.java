package com.blockchain.store.playmarket.ui.fingerprint_screen;

import android.content.Context;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class FingerprintConfiguringPresenter implements FingerprintConfiguringContract.Presenter {

    private FingerprintConfiguringContract.View view;
    private Context context;

    private Disposable fingerprintDisposable = Disposables.empty();

    @Override
    public void init(FingerprintConfiguringContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void subscribeFingerprint(String accountPassword) {
        fingerprintDisposable = RxFingerprint.encrypt(context, accountPassword)
                .subscribe(fingerprintEncryptionResult -> {
                    switch (fingerprintEncryptionResult.getResult()) {
                        case FAILED:
                            view.showToast(context.getResources().getString(R.string.fingerprint_not_recognized));
                            break;
                        case HELP:
                            view.showToast(fingerprintEncryptionResult.getMessage());
                            break;
                        case AUTHENTICATED:
                            String encrypted = fingerprintEncryptionResult.getEncrypted();
                            Hawk.put(Constants.ENCRYPTED_PASSWORD, encrypted);
                            view.closeFingerprintActivity(context.getResources().getString(R.string.fingerprint_success));
                            break;
                    }
                }, throwable -> {
                    view.showToast(throwable.getMessage());
                    ToastUtil.showToast(throwable.getMessage());
                });
    }

    @Override
    public void disposeFingerprint() {
        fingerprintDisposable.dispose();
    }

    @Override
    public boolean checkAccountPassword(String accountPassword) {
        try {
            Application.keyManager.getKeystore().unlock(AccountManager.getAccount(), accountPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
