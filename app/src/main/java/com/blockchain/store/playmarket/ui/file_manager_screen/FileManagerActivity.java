package com.blockchain.store.playmarket.ui.file_manager_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
    private static final String IS_FOLDER_DIALOG_SHOWING = "create_folder_dialog_is_open";
    private static final String FOLDER_NAME = "folder_name";

    private final String basePath = Environment.getExternalStorageDirectory().getPath();
    private final String CURRENT_PATH_KEY = "current_path";
    private String fileManagerType;
    private String currentDirectory;
    private ArrayList<File> fileList = new ArrayList<>();
    private FileManagerRecyclerViewAdapter adapter;
    private FileManagerPresenter presenter;

    @BindView(R.id.folders_recyclerView)
    RecyclerView foldersRecyclerView;

    @BindView(R.id.path_textView)
    TextView pathTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        ButterKnife.bind(this);

        presenter = new FileManagerPresenter();
        presenter.init(this);

        if (savedInstanceState != null) {
            currentDirectory = savedInstanceState.getString(CURRENT_PATH_KEY, basePath);
            if (savedInstanceState.getBoolean(IS_FOLDER_DIALOG_SHOWING)) {
                showCreateFolderDialog(savedInstanceState.getString(FOLDER_NAME));
            }
        } else {
            currentDirectory = basePath;
        }

        fileManagerType = getIntent().getStringExtra(START_FILE_MANAGER_TAG);

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
            presenter.confirmSaveButtonPressed(currentDirectory);
            finish();
        }

        if (fileManagerType.equals("all_files")) {
            int fileIndex = adapter.getSelectedItemIndex();
            if (fileIndex == -1) {
                Toast.makeText(this, "You need chose one of the accounts", Toast.LENGTH_SHORT).show();
            } else {
                if (presenter.jsonKeystoreFileCheck(fileList.get(fileIndex), "address") != null) {
                    File selectedUserJsonFile = fileList.get(fileIndex);
                    String jsonData = presenter.getDataFromJsonKeystoreFile(selectedUserJsonFile, "all_data");
                    showConfirmImportDialog(jsonData);
                } else ToastUtil.showToast("This file is not JSON");
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
        if (DialogManager.createFolderDialog != null && DialogManager.createFolderDialog.isShowing()){
            outState.putBoolean(IS_FOLDER_DIALOG_SHOWING, true);
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
    public void showToast(Boolean success) {
        if (success) ToastUtil.showToast(R.string.success_save_message);
        else ToastUtil.showToast(R.string.failed_save_message);
    }


    @Override
    public void showCreateFolderDialog(String folderName) {
        new DialogManager().showCreateFolderDialog(this, folderName, this);
    }

    @Override
    public void showConfirmImportDialog(String jsonData) {
        new DialogManager().showConfirmImportDialog(this, jsonData, this);
    }


    @Override
    public void onImportSuccessful() {
        goToMainActivity();
    }

    @Override
    public void createFolderClicked(String folderName) {
        presenter.createFolderButtonPressed(currentDirectory, folderName);
        fileList = presenter.getFolderList(currentDirectory, fileManagerType);
        setFoldersRecyclerView(fileList);
    }
}
