package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import org.ethereum.geth.Account;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPromptActivity extends AppCompatActivity {
    private static final String TAG = "LoginPromptActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prompt);
        ButterKnife.bind(this);

        try {
            if (!Application.keyManager.getAccounts().isEmpty())
                LoadNewUserWelcomeActivity(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }


    @OnClick(R.id.ImportUserButton)
    void onUserBtnClicked() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_prompt_dialog);
        final EditText passwordText = dialog.findViewById(R.id.passwordText);
        dialog.show();

        Button addFundsBtn = dialog.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(v -> {
            try {
                ClipboardUtils.importKeyFromClipboard(getApplicationContext(), Application.keyManager.getKeystore(), passwordText.getText().toString());
                showImportSuccessfulAlert();
                dialog.dismiss();
                goToMainActivity(null);
            } catch (Exception e) {
                showImportFailedAlert();
                e.printStackTrace();
            }
        });

        Button close_btn = dialog.findViewById(R.id.close_button);
        close_btn.setOnClickListener(v -> dialog.dismiss());
    }


    private void showImportSuccessfulAlert() {
        ToastUtil.showToast(R.string.import_successful);
    }

    private void showImportFailedAlert() {
        ToastUtil.showToast(R.string.import_failed);
    }

    public void LoadNewUserWelcomeActivity(View view) {
        promptForPasswordForNewAccount();
    }

    public void goToMainActivity(View view) {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
    }

    public void openWelcomeActivity() {
        startActivity(new Intent(getApplicationContext(), NewUserWelcomeActivity.class));
    }

    public void promptForPasswordForNewAccount() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = d.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(v -> {
            makeNewAccount(passwordText.getText().toString());
            d.dismiss();
            openWelcomeActivity();
        });

        Button close_btn = d.findViewById(R.id.close_button);
        close_btn.setOnClickListener(v -> d.dismiss());
    }

    protected String makeNewAccount(String password) {
        try {
            Account account = Application.keyManager.newAccount(password);
            String address = account.getAddress().getHex();
            Log.d(TAG, address);
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
