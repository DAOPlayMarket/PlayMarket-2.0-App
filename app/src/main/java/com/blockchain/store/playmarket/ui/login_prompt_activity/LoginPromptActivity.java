package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import org.ethereum.geth.Account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPromptActivity extends AppCompatActivity implements LoginPromptContract.View {

    private LoginPromptPresenter presenter;

    private static final int CHOSE_FILE_CODE = 99;
    private static final String TAG = "LoginPromptActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prompt);
        ButterKnife.bind(this);
        presenter = new LoginPromptPresenter();
        presenter.init(this);
        if (presenter.checkAccountJson()) showImportDialog();
        if (AccountManager.isHasUsers()) {
            goToMainActivity(null);
        }

    }

    @OnClick(R.id.import_user_button)
    void onUserBtnClicked() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Application"), CHOSE_FILE_CODE);
    }


    private void showImportSuccessfulAlert() {
        ToastUtil.showToast(R.string.import_successful);
    }

    private void showImportFailedAlert() {
        ToastUtil.showToast(R.string.import_failed);
    }

    @OnClick(R.id.NewUserButton)
    void onNewUserClicked() {
        promptForPasswordForNewAccount();
    }

    public void goToMainActivity(View view) {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }

    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(getApplicationContext(), NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
    }

    public void promptForPasswordForNewAccount() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = d.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(v -> {
            String address = makeNewAccount(passwordText.getText().toString());
            d.dismiss();
            openWelcomeActivity(address);
        });

        Button close_btn = d.findViewById(R.id.close_button);
        close_btn.setOnClickListener(v -> d.dismiss());
    }

    private void showDialogConfirmImport(String fileData) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = d.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(v -> {
            d.dismiss();
            confirmImport(fileData, passwordText.getText().toString());
        });

        Button close_btn = d.findViewById(R.id.close_button);
        close_btn.setOnClickListener(v -> d.dismiss());
    }

    private void confirmImport(String fileString, String password) {
        try {
            Account account = Application.keyManager.getKeystore().importKey(fileString.getBytes(), password, password);
            Log.d(TAG, "confirmImport: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String makeNewAccount(String password) {
        try {
            Account account = Application.keyManager.newAccount(password);
            Log.d(TAG, "makeNewAccount: " + account.getURL().toString());
            String address = account.getAddress().getHex();
            presenter.saveAccountJson(account.getURL());
            Log.d(TAG, address);
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHOSE_FILE_CODE) {
            if (data != null) {
                String path = data.getData().getPath();
                File file = new File(path);
                Log.d(TAG, "onActivityResult: " + file.toString());
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    StringBuilder builder = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }
                    Log.d(TAG, "onActivityResult: " + builder.toString());
                    showDialogConfirmImport(builder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
        Log.d(TAG, "onActivityResult: ");
    }

    @Override
    public void showImportDialog() {

    }
}
