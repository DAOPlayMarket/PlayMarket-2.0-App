package com.blockchain.store.playmarket.ui.login_screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen.PasswordPromptFragment;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class FingerprintConfiguringActivity extends AppCompatActivity {

    private static String START_ARGS = "start_args";
    private boolean isStartedFromMenu = false;
    private String accountPassword;
    private Disposable fingerprintDisposable = Disposables.empty();


    public static void startFromMenu(Activity activity, int resultCode) {
        Intent starter = new Intent(activity.getBaseContext(), FingerprintConfiguringActivity.class);
        starter.putExtra(START_ARGS, true);
        activity.startActivityForResult(starter, resultCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent() != null) {
            isStartedFromMenu = getIntent().getBooleanExtra(START_ARGS, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_configuring);

        ButterKnife.bind(this);
        accountPassword = getIntent().getStringExtra(PasswordPromptFragment.ACCOUNT_PASSWORD);
        encryptData();
    }

    @OnClick(R.id.dont_activate_button)
    void doNotActivateButtonClicked() {
        fingerprintDisposable.dispose();
        if (isStartedFromMenu) {
            finish();
            return;
        }
        finishFingerprintConfiguring();
    }

    private void encryptData() {
        fingerprintDisposable = RxFingerprint.encrypt(this, accountPassword)
                .subscribe(fingerprintEncryptionResult -> {
                    switch (fingerprintEncryptionResult.getResult()) {
                        case FAILED:
                            Toast.makeText(this, R.string.fingerprint_not_recognized, Toast.LENGTH_LONG).show();
                            break;
                        case HELP:
                            Toast.makeText(this, fingerprintEncryptionResult.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        case AUTHENTICATED:
                            String encrypted = fingerprintEncryptionResult.getEncrypted();
                            Hawk.put(Constants.ENCRYPTED_PASSWORD, encrypted);
                            Toast.makeText(this, R.string.fingerprint_success, Toast.LENGTH_LONG).show();

                            finishFingerprintConfiguring();
                            break;
                    }
                }, throwable -> {
                    //noinspection StatementWithEmptyBody
                    if (RxFingerprint.keyInvalidated(throwable)) {
                        // The keys you wanted to use are invalidated because the user has turned off his
                        // secure lock screen or changed the fingerprints stored on the device
                        // You have to re-encrypt the data to access it
                    }
                    Log.e("ERROR", "encrypt", throwable);
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    ToastUtil.showToast(throwable.getMessage());
                });
    }

    private void finishFingerprintConfiguring() {
        if (isStartedFromMenu) {
            finish();
            return;
        }
        boolean newAccount = getIntent().getBooleanExtra(PasswordPromptFragment.NEW_ACCOUNT, true);
        if (newAccount) {
            String account = AccountManager.getAddress().getHex();
            openWelcomeActivity(account);
        } else openMainActivity();
    }

    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(this, NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
        finish();
    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}
