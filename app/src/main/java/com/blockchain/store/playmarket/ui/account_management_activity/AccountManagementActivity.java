package com.blockchain.store.playmarket.ui.account_management_activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ethmobile.ethdroid.KeyManager;

public class AccountManagementActivity extends AppCompatActivity {
    private static final String TAG = "AccountManagementActivity";

    @BindView(R.id.address_text_view)
    TextView AddressTextView;
    @BindView(R.id.NewUserWelcomeTextView)
    TextView MainTextView;

    public CryptoUtils crypto;
    private KeyManager keyManager;
    private String datadir;
    private String etherAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_welcome);
        ButterKnife.bind(this);
        setupViewForAccountManagement();
        setDatadir();
        setupCryptoUtils();
        setupKeyManager();
    }

    @OnClick(R.id.address_text_view)
    void copyAddressToClipBoard() {
        ClipboardUtils.copyToClipboard(getApplicationContext(), etherAddress);
        showCopiedAlert();
    }

    protected void setupViewForAccountManagement() {
        MainTextView.setText(R.string.account_management_msg);
    }

    public void copyKeyJsonToClipboard(String password) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (byte b : keyManager.getKeystore().exportKey(keyManager.getAccounts().get(0), password, password)) {
                baos.write(b);
            }

            ClipboardUtils.copyToClipboard(getApplicationContext(), baos.toString("UTF-8"));
            showBackupAlert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void promptForPasswordForNewAccount() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = (EditText) d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = (TextView) d.findViewById(R.id.continueButton);
        addFundsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeNewAccount(passwordText.getText().toString());
                d.dismiss();
            }
        });


        Button close_btn = (Button) d.findViewById(R.id.continueButton);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    private void showCopiedAlert() {
        ToastUtil.showToast(R.string.address_copied);
    }

    private void showBackupAlert() {
        ToastUtil.showToast(R.string.wallet_backup_copied);
    }

    protected void makeNewAccount(String password) {
        try {
            keyManager.newAccount(password);
            etherAddress = keyManager.getAccounts().get(0).getAddress().getHex();
            Log.d(TAG, etherAddress);
            AddressTextView.setText(etherAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setupKeyManager() {
        try {
            keyManager = CryptoUtils.setupKeyManager(datadir);
            if (keyManager.getAccounts().isEmpty()) {
                promptForPasswordForNewAccount();

            } else {
                etherAddress = keyManager.getAccounts().get(0).getAddress().getHex();
                Log.d("Ether", etherAddress);
                AddressTextView.setText(etherAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setDatadir() {
        datadir = getFilesDir().getAbsolutePath();
    }

    protected void setupCryptoUtils() {
        crypto = new CryptoUtils();
    }

    public void goToMainActivity(View view) {
        finish();
    }
}
