package com.blockchain.store.playmarket.ui.file_manager_screen;

import android.content.Context;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FileUtils;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import org.ethereum.geth.Account;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManagerPresenter implements FileManagerContract.Presenter {

    private FileManagerContract.View view;
    private Context context;

    private FileUtils fileUtils;

    @Override
    public void init(FileManagerContract.View view, Context context) {
        this.view = view;
        this.context = context;
        fileUtils = new FileUtils();
    }

    @Override
    public ArrayList<File> getFolderList(String path, String type) {
        return fileUtils.getFileList(path, type);
    }

    @Override
    public void createFolder(String currentDirectory, String folderName) {
        fileUtils.createNewFolder(currentDirectory, folderName);
    }

    @Override
    public void saveJsonAccountOnDevice(String currentDirectory) {
        Boolean success = fileUtils.saveJsonKeystoreFile(currentDirectory);
        if (success)
            ToastUtil.showToast(context.getResources().getString(R.string.success_save_message));
        else ToastUtil.showToast(context.getResources().getString(R.string.success_save_message));
    }

    @Override
    public String getDataFromJsonKeystoreFile(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }

    @Override
    public String jsonKeystoreFileCheck(File file, String type) {
        return fileUtils.readJsonKeystoreFile(file, type);
    }

    public boolean isAccountAlreadyImported(String importedAddress) {
        try {
            List<Account> accounts = AccountManager.getKeyManager().getAccounts();
            for (Account account : accounts) {
                if (account.getAddress().getHex().equalsIgnoreCase(importedAddress)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
