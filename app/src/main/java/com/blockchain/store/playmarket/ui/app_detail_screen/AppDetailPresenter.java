package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;
import com.blockchain.store.playmarket.utilities.installer.ApkInstaller;
import com.blockchain.store.playmarket.utilities.net.APIUtils;

import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.*;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailPresenter implements Presenter, NotificationManagerCallbacks {
    private static final String TAG = "AppDetailPresenter";
    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void registerCallback(App app){
        NotificationManager.getManager().registerCallback(app, this);
    }

    @Override
    public void appDownloadClicked(App app) {

    }

    @Override
    public void checkAppLoadState(App app) {
        boolean applicationInstalled = new MyPackageManager().isApplicationInstalled(app.hash);
        Log.d(TAG, "checkAppLoadState: isApplicationInstalled " + applicationInstalled);
        if (applicationInstalled) {
            view.setButtonText("OPEN");
        } else {
            //todo add state
        }
        /*
        todo check states:
        1. app already installed. Check programatically
        2. app downloading right now. todo
        3. app installing. Checks onResume
        4. app download but not installed todo???
        * */
    }

    @Override
    public void onAppDownloadStarted() {
        view.setButtonText("Started");
        Log.d(TAG, "onAppDownloadStarted() called");
    }

    @Override
    public void onAppDownloadProgressChanged(int progress) {
        view.setButtonText(String.valueOf(progress));
        Log.d(TAG, "onAppDownloadProgressChanged() called with: progress = [" + progress + "]");
    }

    @Override
    public void onAppDownloadSuccessful() {
        view.setButtonText("Done");
        Log.d(TAG, "onAppDownloadSuccessful() called");
    }

    @Override
    public void onAppDownloadError() {
        view.setButtonText("Error");
        Log.d(TAG, "onAppDownloadError() called");
    }

    @Override
    public void onDestroy() {
        NotificationManager.getManager().removeCallback(this);
    }
}
