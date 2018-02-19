package com.blockchain.store.playmarket.ui.new_user_welcome_activity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.FileAdapter;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import org.ethereum.geth.Account;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewUserWelcomeActivity extends AppCompatActivity implements NewUserWelcomeContract.View {
    private static final String TAG = "NewUserWelcomeActivity";
    private NewUserWelcomePresenter presenter;
    private static final int CHOSE_FILE_CODE = 99;

    private ArrayList<File> fileList = new ArrayList<File>();
    private final String basePath = Environment.getExternalStorageDirectory().getPath();
    private String currentPath = "";

    @BindView(R.id.address_text_view)
    TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_welcome);
        presenter = new NewUserWelcomePresenter();
        presenter.init(this, getApplicationContext());
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
    void localSaveKey(){
        presenter.saveKeyOnDevice(addressTextView.getText().toString());
    }


    //@OnClick(R.id.save_cloud_imageButton)
    //void localSaveJsonKeystoreFile(){
//
    //    fileList = getFiles(basePath);
//
    //    View dialogView = getLayoutInflater().inflate(R.layout.local_save_dialog, null);
    //    AlertDialog.Builder builder = new AlertDialog.Builder(NewUserWelcomeActivity.this);
//
    //    ListView folderListView = (ListView) dialogView.findViewById(R.id.folders_listView);
    //    Button backButton = (Button) dialogView.findViewById(R.id.back_button) ;
    //    folderListView.setAdapter(new FileAdapter(this, fileList));
    //    folderListView.setOnItemClickListener((adapterView, view, position, id) -> {
    //        final ArrayAdapter<File> adapter = (FileAdapter) folderListView.getAdapter();
    //        File file = adapter.getItem(position);
    //        if (file.isDirectory()) {
    //            currentPath = file.getPath();
    //            RebuildFiles(adapter);
    //        }
    //    });
//
    //    backButton.setOnClickListener(v -> {
    //        File file = new File(currentPath);
    //        File parentDirectory = file.getParentFile();
    //        if (parentDirectory != null) {
    //            currentPath = parentDirectory.getPath();
    //            RebuildFiles((FileAdapter) folderListView.getAdapter());
    //        }
    //    });
//
    //    builder.setView(dialogView);
    //    AlertDialog saveFileDialog = builder.create();
    //    saveFileDialog.show();
    //}

    private void RebuildFiles(ArrayAdapter<File> adapter) {
        try {
            fileList.clear();
            fileList.addAll(getFiles(currentPath));
            adapter.notifyDataSetChanged();
        }
        catch (NullPointerException e){
        Toast.makeText(getApplicationContext(), android.R.string.unknownName, Toast.LENGTH_SHORT).show();
    }

    }

    private ArrayList<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        ArrayList<File> folderList = new ArrayList<>();

        for (int i =0; i< directory.listFiles().length; i++){
            if (directory.listFiles()[i].isDirectory()) folderList.add(directory.listFiles()[i]);
        }

        return folderList;
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
