package com.blockchain.store.playmarket.ui.new_user_welcome_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindView(R.id.address_text_view)
    TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_welcome);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            addressTextView.setText(getIntent().getStringExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA));
        }
    }


    /*
    Метод перехода на экран отправки почты "send_mail_dialog".
    Вызывается после нажатия на кнопку "save_mail_imageButton" пользователем.
    */
    @OnClick(R.id.save_mail_imageButton)
    void sendMail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "step.93.07@gmail.com");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Your key");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, addressTextView.getText());

        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @OnClick(R.id.local_save_imageButton)
    void localSaveKey(){

    }

    /*
    Метод перехода на главный экран приложения "MainMenuActivity".
    Вызывается после нажатия на кнопку "continue_button" пользователем.
    */
    @OnClick(R.id.continue_button)
    void goToMainActivity(){
        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(myIntent);
        finish();
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

}
