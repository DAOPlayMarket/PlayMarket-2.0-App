package com.blockchain.store.playmarket.ui.fingerprint_screen;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class FingerprintConfiguringActivity extends AppCompatActivity implements FingerprintConfiguringContract.View {

    @BindView(R.id.confirm_password_layout) LinearLayout confirmPasswordLinearLayout;
    @BindView(R.id.account_password_textInputLayout) TextInputLayout accountPasswordTextInputLayout;
    @BindView(R.id.account_password_editText) EditText accountPasswordEditText;
    @BindView(R.id.confirm_password_button) Button confirmPasswordButton;
    @BindView(R.id.available_fingerprint_imageView) ImageView availableFingerprintImageView;

    public enum StartArguments {
        StartedFromMenu,
        StartedFromNewAccount,
        StartedFromImportAccount
    }

    private static String START_ARGS = "start_args";
    private static String PASSWORD = "account_password";

    private StartArguments startArguments;
    private String accountPassword;
    private Disposable fingerprintDisposable = Disposables.empty();
    private FingerprintConfiguringPresenter presenter;

    public static void start(Context context, StartArguments startArguments, String accountPassword) {
        Intent starter = new Intent(context, FingerprintConfiguringActivity.class);
        starter.putExtra(START_ARGS, startArguments);
        if (accountPassword != null) starter.putExtra(PASSWORD, accountPassword);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_configuring);
        ButterKnife.bind(this);

        presenter = new FingerprintConfiguringPresenter();
        presenter.init(this, getApplicationContext());

        getStartData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fingerprintDisposable.dispose();
    }


    @Override
    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(this, NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

    @Override
    public void closeFingerprintActivity() {
        switch (startArguments) {
            case StartedFromImportAccount:
                openMainActivity();
                break;
            case StartedFromNewAccount:
                String account = AccountManager.getAddress().getHex();
                openWelcomeActivity(account);
                break;
            case StartedFromMenu:
                finish();
                break;
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.dont_activate_button)
    void doNotActivateButtonClicked() {
        closeFingerprintActivity();
    }

    private void getStartData(){
        if (getIntent() != null) {
            startArguments = (StartArguments) getIntent().getSerializableExtra(START_ARGS);
            switch (startArguments) {
                case StartedFromImportAccount:
                    passwordLayoutVisible(false);
                    accountPassword = getIntent().getStringExtra(PASSWORD);
                    presenter.encryptAccountPassword(accountPassword);
                    break;
                case StartedFromNewAccount:
                    passwordLayoutVisible(false);
                    accountPassword = getIntent().getStringExtra(PASSWORD);
                    presenter.encryptAccountPassword(accountPassword);
                    break;
                case StartedFromMenu:
                    passwordLayoutVisible(true);
                    break;
            }
        }
    }

    private void passwordLayoutVisible(boolean isShow){
        if (isShow) {
            confirmPasswordLinearLayout.setVisibility(View.VISIBLE);
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.greyBackground));
        }
        else{
            confirmPasswordLinearLayout.setVisibility(View.GONE);
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.green_color));
        }
    }
}
