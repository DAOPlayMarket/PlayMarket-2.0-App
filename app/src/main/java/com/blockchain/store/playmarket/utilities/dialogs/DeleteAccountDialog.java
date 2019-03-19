package com.blockchain.store.playmarket.utilities.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.mtramin.rxfingerprint.RxFingerprint;

import org.ethereum.geth.Account;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

public class DeleteAccountDialog extends AlertDialog {
    private Context context;
    private EditText passwordField;
    private Disposable disposable = EmptyDisposable.INSTANCE;
    private Button cancel;
    private DeleteAccountCallback callback;
    private Account account;
    private Button continueButton;

    public DeleteAccountDialog(Context context, Account account) {
        super(context);
        this.context = context;
        this.account = account;
        init();
    }

    public void setCallback(DeleteAccountCallback callback) {
        this.callback = callback;
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.delete_account_dialog, null);
        setView(view);

        passwordField = view.findViewById(R.id.password_editText);
        cancel = view.findViewById(R.id.btn_later);
        continueButton = view.findViewById(R.id.delete_button);

        if (FingerprintUtils.isFingerprintAvailibility(context, account.getAddress().getHex())) {
            view.findViewById(R.id.fingerprint_holder).setVisibility(View.VISIBLE);
            enableFingerprint();
        }

        cancel.setOnClickListener(v -> dismiss());
        continueButton.setOnClickListener(v -> performDeleteAccount(passwordField.getText().toString()));
    }

    private void enableFingerprint() {
        disposable = RxFingerprint.decrypt(context, FingerprintUtils.getPasswordForAddress(account.getAddress().getHex()))
                .subscribe(fingerprintDecryptionResult -> {
                    switch (fingerprintDecryptionResult.getResult()) {
                        case FAILED:
                            passwordField.setError(context.getResources().getString(R.string.fingerprint_not_recognized));
                            passwordField.requestFocus();
                            break;
                        case AUTHENTICATED:
                            performDeleteAccount(fingerprintDecryptionResult.getDecrypted());
                            break;
                    }
                }, throwable -> Log.e("ERROR", "decrypt", throwable));
    }

    private void performDeleteAccount(String decrypted) {
        try {
            AccountManager.getKeyStore().deleteAccount(account, decrypted);
            AccountManager.setCurrentUserPosition(0);
            Toast.makeText(context, "Account " + account.getAddress().getHex() + " deleted", Toast.LENGTH_SHORT).show();
            callback.onAccountDeleted();
            this.dismiss();
        } catch (Exception e) {
            passwordField.setError(context.getString(R.string.wrong_password));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }


    public interface DeleteAccountCallback {
        void onAccountDeleted();
    }
}
