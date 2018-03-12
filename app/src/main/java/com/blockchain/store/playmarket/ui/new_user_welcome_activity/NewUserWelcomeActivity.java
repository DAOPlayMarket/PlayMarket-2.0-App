package com.blockchain.store.playmarket.ui.new_user_welcome_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.file_manager_screen.FileManagerActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewUserWelcomeActivity extends AppCompatActivity implements NewUserWelcomeContract.View {
    private static final String TAG = "NewUserWelcomeActivity";
    private static final String LAUNCHED_FROM_SETTINGS_PARAM = "launched_from_settings";
    private NewUserWelcomePresenter presenter;
    private static final int CHOSE_FILE_CODE = 99;

    private ArrayList<File> fileList = new ArrayList<File>();
    private String currentPath = "";

    @BindView(R.id.address_text_view) TextView addressTextView;
    @BindView(R.id.NewUserWelcomeTextView) TextView newUserWelcomeNext;
    @BindView(R.id.continue_button) Button continueButton;

    private boolean isLaunchedFromSettings;

    public static void start(Context context, boolean isLaucnhedFromSettings) {
        Intent starter = new Intent(context, NewUserWelcomeActivity.class);
        starter.putExtra(LAUNCHED_FROM_SETTINGS_PARAM, isLaucnhedFromSettings);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_welcome);
        presenter = new NewUserWelcomePresenter();
        presenter.init(this, getApplicationContext());
        ButterKnife.bind(this);
        if (getIntent() != null) {
            addressTextView.setText(getIntent().getStringExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA));
            isLaunchedFromSettings = getIntent().getBooleanExtra(Constants.WELCOME_ACTIVITY_IS_LUANCHED_FROM_SETTINGS_EXTRA, false);
        }
        if (isLaunchedFromSettings) {
            setViewFromSettings();
            addressTextView.setText(AccountManager.getAddress().getHex());
        }
    }

    private void setViewFromSettings() {
        continueButton.setText(R.string.back);
        newUserWelcomeNext.setVisibility(View.GONE);
    }

    /*
    Метод перехода на экран отправки почты "send_mail_dialog".
    Вызывается после нажатия на кнопку "save_mail_imageButton" пользователем.
    */
    @OnClick(R.id.save_mail_imageButton)
    void sendMail() {
        try {
            String jsonKeystoreFileURL = Application.keyManager.getAccounts().get(0).getURL();
            final String pathToJsonFile = jsonKeystoreFileURL.replace("keystore:///", "");
            // Создадим новый файл, используя созданное выше поле.
            File jsonKeystoreFile = new File(pathToJsonFile);

            FileInputStream inputStream = new FileInputStream(jsonKeystoreFile);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            // Создадим поле для хранения содержимого файла.
            String jsonText = new String(bytes);

            File copyJsonFile = File.createTempFile(jsonKeystoreFile.getName(), "", getApplicationContext().getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(copyJsonFile);

            // Запишем в созданный файл данные из поля "jsonText".
            outputStream.write(jsonText.getBytes());
            outputStream.close();

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", copyJsonFile.getAbsoluteFile());

            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "JSON Keystore File");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            copyJsonFile.deleteOnExit();
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.local_save_imageButton)
    void localSaveKey() {
        Intent intent = new Intent(getApplicationContext(), FileManagerActivity.class);
        intent.putExtra(FileManagerActivity.START_FILE_MANAGER_TAG, "folders");
        startActivityForResult(intent, 1);
    }

    private void RebuildFiles(ArrayAdapter<File> adapter) {
        try {
            fileList.clear();
            fileList.addAll(getFiles(currentPath));
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), android.R.string.unknownName, Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<File> getFiles(String directoryPath) {
        File directory = new File(directoryPath);
        ArrayList<File> folderList = new ArrayList<>();

        for (int i = 0; i < directory.listFiles().length; i++) {
            if (directory.listFiles()[i].isDirectory()) folderList.add(directory.listFiles()[i]);
        }

        return folderList;
    }

    /*
    Метод перехода на главный экран приложения "MainMenuActivity".
    Вызывается после нажатия на кнопку "continue_button" пользователем.
    */
    @OnClick(R.id.continue_button)
    void goToMainActivity() {
        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isLaunchedFromSettings) {
            super.onBackPressed();
        }
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
