package com.blockchain.store.playmarket.ui.file_manager_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.FileManagerRecyclerViewAdapter;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileManagerActivity extends AppCompatActivity implements FileManagerContract.View, FileManagerRecyclerViewAdapter.FileManagerCallback, DialogManager.ConfirmImportDialogCallback, DialogManager.CreateFolderDialogCallback {

    public static final String START_FILE_MANAGER_TAG = "file_manager_parameter";
    private static final String IS_DIALOG_SHOWING = "is_dialog_showing";
    private static final String FOLDER_NAME = "folder_name";
    private static final String DIALOG_NAME = "dialog_name";
    private static final String JSON_DATA = "json_data";
    private static final String PASSWORD = "password";

    private final String basePath = Environment.getExternalStorageDirectory().getPath();
    private final String CURRENT_PATH_KEY = "current_path";
    private String fileManagerType;
    private String currentDirectory;
    private ArrayList<File> fileList = new ArrayList<>();
    private FileManagerRecyclerViewAdapter adapter;
    private FileManagerPresenter presenter;

    private DialogManager dialogManager;
    private DialogManager.DialogNames dialogName;
    private AlertDialog displayAlertDialog;

    private String jsonData;

    @BindView(R.id.folders_recyclerView) RecyclerView foldersRecyclerView;

    @BindView(R.id.path_textView) TextView pathTextView;

    @BindView(R.id.info_textView) TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        ButterKnife.bind(this);

        dialogManager = new DialogManager();

        presenter = new FileManagerPresenter();
        presenter.init(this, getApplicationContext());

        if (savedInstanceState != null) {
            currentDirectory = savedInstanceState.getString(CURRENT_PATH_KEY, basePath);
            if ((savedInstanceState.getBoolean(IS_DIALOG_SHOWING))
                    && (savedInstanceState.getSerializable(DIALOG_NAME) == DialogManager.DialogNames.CREATE_FOLDER_DIALOG)) {
                showCreateFolderDialog(savedInstanceState.getString(FOLDER_NAME));
            }
        } else {
            currentDirectory = basePath;
        }

        fileManagerType = getIntent().getStringExtra(START_FILE_MANAGER_TAG);

        if (fileManagerType.equals("folders")) infoTextView.setText(getResources().getText(R.string.chose_folder));
        if (fileManagerType.equals("all_files")) infoTextView.setText(getResources().getText(R.string.chose_file));

        pathTextView.setText(currentDirectory);
        fileList = presenter.getFolderList(currentDirectory, fileManagerType);
        setFoldersRecyclerView(fileList);
    }

    private void goToMainActivity() {
        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void setFoldersRecyclerView(ArrayList<File> foldersList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        foldersRecyclerView.setLayoutManager(layoutManager);
        foldersRecyclerView.setHasFixedSize(true);
        adapter = new FileManagerRecyclerViewAdapter(foldersList, this);
        adapter.setHasStableIds(true);
        foldersRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.back_imageButton)
    void stepBack() {
        if (currentDirectory != null && !currentDirectory.equals(basePath)) {
            File file = new File(currentDirectory);
            currentDirectory = file.getParentFile().getPath();
            pathTextView.setText(currentDirectory);
            fileList = presenter.getFolderList(currentDirectory, fileManagerType);
            setFoldersRecyclerView(fileList);
        }
    }

    @OnClick(R.id.close_manager_button)
    void closeFileManagerActivity() {
        finish();
    }


    @OnClick(R.id.create_folder_imageButton)
    void createFolder() {
        showCreateFolderDialog("");
    }

    @OnClick(R.id.confirm_button)
    void confirmButtonPressed() {

        if (fileManagerType.equals("folders")) {
            presenter.saveJsonAccountOnDevice(currentDirectory);
            finish();
        }

        if (fileManagerType.equals("all_files")) {
            int fileIndex = adapter.getSelectedItemIndex();
            if (fileIndex == -1) {
                Toast.makeText(this, "You need chose one of the accounts", Toast.LENGTH_SHORT).show();
            } else {
                if (presenter.jsonKeystoreFileCheck(fileList.get(fileIndex), "address") != null) {
                    File selectedUserJsonFile = fileList.get(fileIndex);
                    jsonData = presenter.getDataFromJsonKeystoreFile(selectedUserJsonFile, "all_data");
                    Intent intent = new Intent();
                    intent.putExtra("json_data", jsonData);
                    setResult(RESULT_OK, intent);
                    finish();
                } else ToastUtil.showToast("Wrong File");
            }
        }
    }

    @Override
    public void onClick() {
        currentDirectory = adapter.getSelectedPath();
        pathTextView.setText(currentDirectory);
        fileList = presenter.getFolderList(currentDirectory, fileManagerType);
        setFoldersRecyclerView(fileList);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentDirectory != null) outState.putString(CURRENT_PATH_KEY, currentDirectory);
        if (displayAlertDialog != null && displayAlertDialog.isShowing()) {
            outState.putBoolean(IS_DIALOG_SHOWING, true);
            outState.putSerializable(DIALOG_NAME, dialogName);
            if (dialogName == DialogManager.DialogNames.CONFIRM_IMPORT_DIALOG) {
                outState.putString(JSON_DATA, jsonData);
                outState.putString(PASSWORD, dialogManager.getPasswordText());
            }
            if (dialogName == DialogManager.DialogNames.CREATE_FOLDER_DIALOG) {
                outState.putString(FOLDER_NAME, dialogManager.getFolderNameText());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentDirectory != null && !currentDirectory.equals(basePath)) {
            File file = new File(currentDirectory);
            currentDirectory = file.getParentFile().getPath();
            pathTextView.setText(currentDirectory);
            fileList = presenter.getFolderList(currentDirectory, fileManagerType);
            setFoldersRecyclerView(fileList);
        } else finish();
    }

    @Override
    public void showCreateFolderDialog(String folderName) {
        displayAlertDialog = dialogManager.showCreateFolderDialog(this, folderName, this);
        dialogName = DialogManager.DialogNames.CREATE_FOLDER_DIALOG;
    }

    @Override
    public void onImportSuccessful() {
        goToMainActivity();
    }

    @Override
    public void createFolderClicked(String folderName) {
        presenter.createFolder(currentDirectory, folderName);
        fileList = presenter.getFolderList(currentDirectory, fileManagerType);
        setFoldersRecyclerView(fileList);
    }
}
