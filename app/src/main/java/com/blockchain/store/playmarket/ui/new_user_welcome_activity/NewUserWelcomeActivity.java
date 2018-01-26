package com.blockchain.store.playmarket.ui.new_user_welcome_activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ethmobile.ethdroid.KeyManager;

public class NewUserWelcomeActivity extends AppCompatActivity {
    private static final String TAG = "NewUserWelcomeActivity";

    @BindView(R.id.address_text_view) TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_welcome);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            addressTextView.setText(getIntent().getStringExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA));
        }
    }

    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.address_text_view)
    void copyAddressToClipBoardClicked() {
        ClipboardUtils.copyToClipboard(getApplicationContext(), addressTextView.getText().toString());
        showCopiedAlert();
    }

    public void copyKeyJsonToClipboard(String password) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (byte b : Application.keyManager.getKeystore().exportKey(Application.keyManager.getAccounts().get(0), password, password)) {
                baos.write(b);
            }
            ClipboardUtils.copyToClipboard(getApplicationContext(), baos.toString("UTF-8"));
            showBackupAlert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void promptForPassword(View view) {
        final Dialog d = new Dialog(this);

        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = d.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(v -> {
            copyKeyJsonToClipboard(passwordText.getText().toString());
            d.dismiss();
        });


        Button close_btn = d.findViewById(R.id.close_button);
        close_btn.setOnClickListener(v -> d.dismiss());
    }


    private void showCopiedAlert() {
        ToastUtil.showToast(R.string.address_copied);
    }

    private void showBackupAlert() {
        ToastUtil.showToast(R.string.wallet_backup_copied);
    }

    public void goToMainActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(myIntent);
        finish();
    }
}
