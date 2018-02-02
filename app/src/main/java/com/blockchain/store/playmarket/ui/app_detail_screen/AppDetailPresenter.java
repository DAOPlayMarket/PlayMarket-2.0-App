package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.content.Context;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.*;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailPresenter implements Presenter, NotificationManagerCallbacks {
    private static final String TAG = "AppDetailPresenter";
    private Constants.APP_STATE appState = Constants.APP_STATE.STATE_UNKOWN;
    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void getDetailedInfo(App app) {
        RestApi.getServerApi().getAppInfo(app.catalogId, app.appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    view.setProgress(true);
                    view.showErrorView(false);
                })
                .doOnTerminate(() -> view.setProgress(false))
                .subscribe(this::onDetailedInfoReady, this::onDetailedInfoFailed);

    }

    private void onDetailedInfoReady(AppInfo appInfo) {
        view.onDetailedInfoReady(appInfo);
    }

    private void onDetailedInfoFailed(Throwable throwable) {
        view.onDetailedInfoFailed(throwable);
    }

    @Override
    public void onActionButtonClicked(App app) {
        Log.d(TAG, "onActionButtonClicked: " + appState);
        switch (appState) {
            case STATE_DOWNLOAD_ERROR:
            case STATE_NOT_DOWNLOAD:
                NotificationManager.getManager().registerCallback(app, this);
                new MyPackageManager().startDownloadApkService(app);
                changeState(Constants.APP_STATE.STATE_DOWNLOADING);
                break;
            case STATE_DOWNLOADED_NOT_INSTALLED:
            case STATE_INSTALL_FAIL:
                new MyPackageManager().installApkByApp(app);
                changeState(Constants.APP_STATE.STATE_INSTALLING);
                break;
            case STATE_INSTALLED:
                new MyPackageManager().openAppByPackage(app.hash);
                break;

            case STATE_INSTALLING:
            case STATE_DOWNLOAD_STARTED:
            case STATE_DOWNLOADING:
            case STATE_UNKOWN:
                // todo do nothing
                break;
        }
    }

    @Override
    public void loadButtonsState(App app) {
        boolean applicationInstalled = new MyPackageManager().isApplicationInstalled(app.hash);
        Log.d(TAG, "loadButtonsState: isApplicationInstalled " + applicationInstalled);
        if (applicationInstalled) {
            changeState(Constants.APP_STATE.STATE_INSTALLED);
        } else if (new MyPackageManager().isAppFileExists(app)) {
            changeState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
        } else if (NotificationManager.getManager().isCallbackAlreadyRegistred(this)) {
            NotificationManager.getManager().registerCallback(app, this);
            changeState(Constants.APP_STATE.STATE_DOWNLOADING);
        } else {
            changeState(Constants.APP_STATE.STATE_NOT_DOWNLOAD);

        }
    }

    private void changeState(Constants.APP_STATE newState) {
        appState = newState;
        Log.d(TAG, "setActionButtonText() called with: appState = [" + newState + "]");
        Context context = Application.getInstance().getApplicationContext();
        switch (newState) {
            case STATE_DOWNLOADING:
                break;
            case STATE_DOWNLOADED_NOT_INSTALLED:
                view.setActionButtonText(context.getString(R.string.btn_install));
                break;
            case STATE_DOWNLOAD_ERROR:
            case STATE_NOT_DOWNLOAD:
                view.setActionButtonText(context.getString(R.string.btn_download));
                break;
            case STATE_INSTALLING:
                break;
            case STATE_INSTALLED:
                view.setActionButtonText(context.getString(R.string.btn_open));
                break;
            case STATE_INSTALL_FAIL:
                break;

        }
        if (newState == Constants.APP_STATE.STATE_INSTALLED) {
            view.setDeleteButtonVisibility(true);
        } else {
            view.setDeleteButtonVisibility(false);
        }
    }

    @Override
    public void onAppDownloadStarted() {
        changeState(Constants.APP_STATE.STATE_DOWNLOAD_STARTED);
        Log.d(TAG, "onAppDownloadStarted() called");
    }

    @Override
    public void onAppDownloadProgressChanged(int progress) {
        changeState(Constants.APP_STATE.STATE_DOWNLOADING);
        view.setActionButtonText(String.valueOf(progress));
        Log.d(TAG, "onAppDownloadProgressChanged() called with: progress = [" + progress + "]");
    }

    @Override
    public void onAppDownloadSuccessful() {
        changeState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
        Log.d(TAG, "onAppDownloadSuccessful() called");
    }

    @Override
    public void onAppDownloadError() {
        changeState(Constants.APP_STATE.STATE_DOWNLOAD_ERROR);
        Log.d(TAG, "onAppDownloadError() called");
    }

    @Override
    public void onDestroy() {
        NotificationManager.getManager().removeCallback(this);
    }

    @Override
    public void onDeleteButtonClicked(App app) {
        new MyPackageManager().uninstallApkByApp(app);
    }
}
