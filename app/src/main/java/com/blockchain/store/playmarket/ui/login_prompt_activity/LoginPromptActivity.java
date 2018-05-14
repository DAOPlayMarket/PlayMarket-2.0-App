package com.blockchain.store.playmarket.ui.login_prompt_activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.UserListAdapter;
import com.blockchain.store.playmarket.ui.file_manager_screen.FileManagerActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.orhanobut.hawk.Hawk;

import org.ethereum.geth.Account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPromptActivity extends AppCompatActivity implements LoginPromptContract.View {

    private LoginPromptPresenter presenter;
    private UserListAdapter adapter;

    private static final int CHOSE_FILE_CODE = 99;
    private static final String TAG = "LoginPromptActivity";

    private AlertDialog importDialog;
    private AlertDialog confirmImportDialog;
    private AlertDialog newUserDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prompt);
        ButterKnife.bind(this);
        presenter = new LoginPromptPresenter();
        presenter.init(this);
        if (AccountManager.isHasUsers()) {
            goToMainActivity(null);
        } else {
            if (presenter.checkJsonFileExists()) showImportUserDialog();
        }

    }

    @OnClick(R.id.import_user_button)
    void onUserBtnClicked() {
        Intent intent = new Intent(getApplicationContext(), FileManagerActivity.class);
        intent.putExtra(FileManagerActivity.START_FILE_MANAGER_TAG, "all_files");
        startActivityForResult(intent, 1);
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
        View view = getLayoutInflater().inflate(R.layout.password_prompt_dialog, null);

        newUserDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();


        final EditText passwordText = (EditText) view.findViewById(R.id.passwordText);
        Button continueButton = (Button) view.findViewById(R.id.continueButton);
        Button closeButton = (Button) view.findViewById(R.id.close_button);
        TextInputLayout passwordLayout = (TextInputLayout) view.findViewById(R.id.password_inputLayout);


        continueButton.setOnClickListener(v -> {
            if (passwordText.getText().toString().equals("")) {
                passwordLayout.setErrorEnabled(true);
                passwordLayout.setError(getResources().getString(R.string.empty_password));
            } else if (passwordText.getText().length() < 7) {
                passwordLayout.setErrorEnabled(true);
                passwordLayout.setError(getResources().getString(R.string.short_password));
            } else {
                String address = makeNewAccount(passwordText.getText().toString());
                newUserDialog.dismiss();
                openWelcomeActivity(address);
            }
        });

        closeButton.setOnClickListener(v -> newUserDialog.dismiss());

        newUserDialog.show();
    }

    @Override
    public void showDialogConfirmImport(String fileData) {
        View view = getLayoutInflater().inflate(R.layout.password_prompt_dialog, null);
        confirmImportDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        final EditText passwordText = (EditText) view.findViewById(R.id.passwordText);
        Button importButton = (Button) view.findViewById(R.id.continueButton);
        Button closeButton = (Button) view.findViewById(R.id.close_button);
        TextInputLayout passwordLayout = (TextInputLayout) view.findViewById(R.id.password_inputLayout);


        importButton.setOnClickListener(v -> {
            if (presenter.confirmImportButtonPressed(fileData, passwordText.getText().toString())) {
                goToMainActivity();
                ToastUtil.showToast(R.string.import_successful);
            } else {
                passwordLayout.setErrorEnabled(true);
                passwordLayout.setError(getResources().getString(R.string.wrong_password));
            }
        });

        closeButton.setOnClickListener(v -> confirmImportDialog.dismiss());
        confirmImportDialog.show();
    }

    protected String makeNewAccount(String password) {
        try {
            Account account = Application.keyManager.newAccount(password);
            Log.d(TAG, "makeNewAccount: " + account.getURL().toString());
            String address = account.getAddress().getHex();
            presenter.autoSaveJsonKeystoreFile();
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
    public void showImportUserDialog() {
        View view = getLayoutInflater().inflate(R.layout.import_user_dialog, null);

        importDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        RecyclerView userListRecyclerView = (RecyclerView) view.findViewById(R.id.json_keysore_files_recycler_view);
        Button importButton = (Button) view.findViewById(R.id.import_button);
        Button cancelButton = (Button) view.findViewById(R.id.close_manager_button);

        // Получаем список файлов "JsonKeystoreFileList" из каталога "/PlayMarket2.0/Accounts/".
        ArrayList<File> userList = presenter.getJsonKeystoreCollection();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        userListRecyclerView.setLayoutManager(layoutManager);
        // Объявляем и устанавливаем адаптер для "userListRecyclerView".
        adapter = new UserListAdapter(userList);
        userListRecyclerView.setAdapter(adapter);

        importButton.setOnClickListener(v -> {
            if (adapter.getSelectedItem() == -1) {
                Toast.makeText(this, "You need chose one of the accounts", Toast.LENGTH_SHORT).show();
            } else {
                File selectedUserJsonFile = userList.get(adapter.getSelectedItem());
                String jsonData = presenter.getDataFromJsonKeystoreFile(selectedUserJsonFile, "all_data");
                showDialogConfirmImport(jsonData);

            }
        });

        cancelButton.setOnClickListener(v -> importDialog.dismiss());

        importDialog.show();
    }

    @Override
    public void showToast(Boolean success) {
        if (success) ToastUtil.showToast(R.string.success_autosave_message);
        else ToastUtil.showToast(R.string.failed_autosave_message);
    }

    public void goToMainActivity() {
        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(myIntent);
        importDialog.dismiss();
        confirmImportDialog.dismiss();
        finish();
    }

}
