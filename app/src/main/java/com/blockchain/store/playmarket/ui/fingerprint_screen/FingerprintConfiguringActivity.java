package com.blockchain.store.playmarket.ui.fingerprint_screen;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class FingerprintConfiguringActivity extends AppCompatActivity implements FingerprintConfiguringContract.View {

    @BindView(R.id.confirm_password_layout) LinearLayout confirmPasswordLinearLayout;
    @BindView(R.id.account_password_textInputLayout) TextInputLayout accountPasswordTextInputLayout;
    @BindView(R.id.account_password_editText) EditText accountPasswordEditText;
    @BindView(R.id.confirm_password_button) Button confirmPasswordButton;
    @BindView(R.id.available_fingerprint_imageView) ImageView availableFingerprintImageView;

    @BindView(R.id.fingerprint_title_textView) TextView fingerprintTitleTextView;
    @BindView(R.id.fingerprint_info_textView) TextView fingerprintInfoTextView;
    @BindView(R.id.password_title_textView) TextView passwordTitleitleTextView;

    public enum StartArguments {
        StartedFromMenu,
        StartedFromNewAccount,
        StartedFromImportAccount
    }

    private static String START_ARGS = "start_args";
    private static String PASSWORD = "account_password";

    private StartArguments startArguments;
    private String accountPassword;
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
        presenter.disposeFingerprint();
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
                    confirmPasswordLinearLayout.setVisibility(View.GONE);
                    accountPassword = getIntent().getStringExtra(PASSWORD);
                    presenter.subscribeFingerprint(accountPassword);
                    break;
                case StartedFromNewAccount:
                    confirmPasswordLinearLayout.setVisibility(View.GONE);
                    accountPassword = getIntent().getStringExtra(PASSWORD);
                    presenter.subscribeFingerprint(accountPassword);
                    break;
                case StartedFromMenu:
                    confirmPasswordLinearLayout.setVisibility(View.VISIBLE);
                    fingerprintIsAvailable(false);
                    break;
            }
        }
    }

    @OnClick(R.id.confirm_password_button) void confirmPasswordButtonClicked(){
        String accountPassword = accountPasswordEditText.getText().toString();
        if (presenter.checkAccountPassword(accountPassword)){
            hidePasswordError();
            fingerprintIsAvailable(true);
            presenter.subscribeFingerprint(accountPassword);

        } else {
            showPasswordError(getResources().getString(R.string.wrong_password));
        }
    }

    private void fingerprintIsAvailable(boolean isAvailable){
        if (isAvailable){
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.green_color));
            fingerprintTitleTextView.setTextColor(getResources().getColor(R.color.black));
            fingerprintInfoTextView.setTextColor(getResources().getColor(R.color.black));
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.green_color));

            passwordTitleitleTextView.setEnabled(false);
            accountPasswordEditText.setEnabled(false);
            confirmPasswordButton.setEnabled(false);


        } else {
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.greyBackground));
            fingerprintTitleTextView.setTextColor(getResources().getColor(R.color.greyBackground));
            fingerprintInfoTextView.setTextColor(getResources().getColor(R.color.greyBackground));
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.greyBackground));

            passwordTitleitleTextView.setEnabled(true);
            accountPasswordEditText.setEnabled(true);
            confirmPasswordButton.setEnabled(true);

        }
    }

    private void showPasswordError(String errorText){
        accountPasswordTextInputLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(this, R.color.red_error_color));
        accountPasswordTextInputLayout.setError(errorText);
    }

    private void hidePasswordError(){
        accountPasswordTextInputLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(this, R.color.green_color));
        accountPasswordTextInputLayout.setError(null);
    }

    @OnTextChanged(value = R.id.account_password_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onPasswordChanged(Editable editable) {
        hidePasswordError();
    }

    @Override
    public void onBackPressed() {
    }
}
