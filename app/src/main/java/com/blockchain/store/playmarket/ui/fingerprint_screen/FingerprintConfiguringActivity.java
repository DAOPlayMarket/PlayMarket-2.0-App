package com.blockchain.store.playmarket.ui.fingerprint_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.blockchain.store.playmarket.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FingerprintConfiguringActivity extends AppCompatActivity implements FingerprintConfiguringContract.View {

    @BindView(R.id.confirm_password_layout) LinearLayout confirmPasswordLinearLayout;
    @BindView(R.id.account_password_textInputLayout) TextInputLayout accountPasswordTextInputLayout;
    @BindView(R.id.account_password_editText) EditText accountPasswordEditText;
    @BindView(R.id.confirm_password_button) Button confirmPasswordButton;
    @BindView(R.id.available_fingerprint_imageView) ImageView availableFingerprintImageView;

    @BindView(R.id.fingerprint_title_textView) TextView fingerprintTitleTextView;
    @BindView(R.id.fingerprint_info_textView) TextView fingerprintInfoTextView;
    @BindView(R.id.password_title_textView) TextView passwordTitleTextView;

    public static String PASSWORD = "account_password";
    public static String RESULT = "resultMessage";

    private String accountPassword;
    private FingerprintConfiguringPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_configuring);
        ButterKnife.bind(this);

        presenter = new FingerprintConfiguringPresenter();
        presenter.init(this, getApplicationContext());

        accountPassword = getIntent().getStringExtra(PASSWORD);

        if (accountPassword == null) {
            confirmPasswordLinearLayout.setVisibility(View.VISIBLE);
            fingerprintIsAvailable(false);
        } else {
            confirmPasswordLinearLayout.setVisibility(View.GONE);
            accountPassword = getIntent().getStringExtra(PASSWORD);
            presenter.subscribeFingerprint(accountPassword);
        }
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
    public void closeFingerprintActivity(String resultMessage) {
        Intent intent = new Intent();
        intent.putExtra(RESULT, resultMessage);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.dont_activate_button)
    void doNotActivateButtonClicked() {
        closeFingerprintActivity("Fingerprint activating is canceled");
    }

    @OnClick(R.id.confirm_password_button)
    void confirmPasswordButtonClicked() {
        String accountPassword = accountPasswordEditText.getText().toString();
        if (presenter.checkAccountPassword(accountPassword)) {
            hidePasswordError();
            fingerprintIsAvailable(true);
            presenter.subscribeFingerprint(accountPassword);

        } else {
            showPasswordError(getResources().getString(R.string.wrong_password));
        }
    }

    private void fingerprintIsAvailable(boolean isAvailable) {
        if (isAvailable) {
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.colorAccent));
            fingerprintTitleTextView.setTextColor(getResources().getColor(R.color.black));
            fingerprintInfoTextView.setTextColor(getResources().getColor(R.color.black));
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.colorAccent));

            passwordTitleTextView.setEnabled(false);
            accountPasswordEditText.setEnabled(false);
            confirmPasswordButton.setEnabled(false);


        } else {
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.greyBackground));
            fingerprintTitleTextView.setTextColor(getResources().getColor(R.color.greyBackground));
            fingerprintInfoTextView.setTextColor(getResources().getColor(R.color.greyBackground));
            availableFingerprintImageView.setColorFilter(getResources().getColor(R.color.greyBackground));

            passwordTitleTextView.setEnabled(true);
            accountPasswordEditText.setEnabled(true);
            confirmPasswordButton.setEnabled(true);

        }
    }

    private void showPasswordError(String errorText) {
        accountPasswordTextInputLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(this, R.color.red_error_color));
        accountPasswordTextInputLayout.setError(errorText);
    }

    private void hidePasswordError() {
        accountPasswordTextInputLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(this, R.color.colorAccent));
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
