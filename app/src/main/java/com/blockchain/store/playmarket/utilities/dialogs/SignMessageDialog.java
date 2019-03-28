package com.blockchain.store.playmarket.utilities.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.constraint.Group;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.mtramin.rxfingerprint.RxFingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.EmptyDisposable;

public class SignMessageDialog extends AlertDialog {
    @BindView(R.id.message_body)
    TextView recipient_address;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.cancel_button)
    ImageView cancel_button;
    @BindView(R.id.usePassword_button)
    TextView usePassword_button;
    @BindView(R.id.fingerprintGroup)
    Group fingerprintGroup;
    @BindView(R.id.passwordGroup)
    Group passwordGroup;
    @BindView(R.id.confirm_button)
    Button confirm_button;
    @BindView(R.id.password_editText)
    TextInputEditText passwordEditText;
    @BindView(R.id.password_inputLayout)
    TextInputLayout passwordLayout;

    private Disposable fingerprintDisposable = EmptyDisposable.INSTANCE;
    private Context context = getContext();
    private String stringToSign;
    private DialogManager.DividendCallback callback;

    public SignMessageDialog(Context context, String stringToSign, DialogManager.DividendCallback callback) {
        super(context);
        this.stringToSign = stringToSign;
        this.callback = callback;
        init();
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.sign_message_dialog, null);
        ButterKnife.bind(this, view);
        setView(view);
        recipient_address.setText(stringToSign);
        initFingerprint();
    }

    private void initFingerprint() {

        confirm_button.setOnClickListener(v -> {
            boolean isUnlock = AccountManager.unlockKeystore(passwordEditText.getText().toString());
            if (isUnlock) {
                callback.onAccountUnlocked();
                this.dismiss();
            } else {
                passwordLayout.setError("Wrong password");
            }
        });

        if (FingerprintUtils.isFingerprintAvailibility(context)) {
            passwordGroup.setVisibility(View.GONE);
            fingerprintGroup.setVisibility(View.VISIBLE);
            fingerprintDisposable = RxFingerprint.decrypt(context, FingerprintUtils.getEncryptedPassword())
                    .subscribe(fingerprintDecryptionResult -> {
                        switch (fingerprintDecryptionResult.getResult()) {
                            case AUTHENTICATED:
                                boolean isUnlock = AccountManager.unlockKeystore(fingerprintDecryptionResult.getDecrypted());
                                if (isUnlock) {
                                    callback.onAccountUnlocked();
                                    this.dismiss();
                                }
                                fingerprintDisposable.dispose();
                        }
                    }, throwable -> Log.e("ERROR", "decrypt", throwable));
        } else {
            passwordGroup.setVisibility(View.VISIBLE);
            fingerprintGroup.setVisibility(View.GONE);
        }

        usePassword_button.setOnClickListener(v -> {
            fingerprintDisposable.dispose();
            passwordGroup.setVisibility(View.VISIBLE);
            fingerprintGroup.setVisibility(View.GONE);
        });

        cancel_button.setOnClickListener(v -> {
            if (FingerprintUtils.isFingerprintAvailibility(context))
                fingerprintDisposable.dispose();
            this.dismiss();
        });
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        fingerprintDisposable.dispose();
    }
}
