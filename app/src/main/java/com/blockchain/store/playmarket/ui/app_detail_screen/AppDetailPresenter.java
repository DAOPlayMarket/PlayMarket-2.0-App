package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.util.Log;

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
    public void appDownloadClicked(App app) {

    }

    @Override
    public void checkAppLoadState(App app) {
        boolean applicationInstalled = new MyPackageManager().isApplicationInstalled(app.hash);
        Log.d(TAG, "checkAppLoadState: isApplicationInstalled " + applicationInstalled);
        if (applicationInstalled) {
            changeState(Constants.APP_STATE.STATE_INSTALLED);
        } else if (new MyPackageManager().isAppFileExists(app)) {
            changeState(Constants.APP_STATE.STATE_DOWNLOAD_NOT_INSTALLED);
        } else if (NotificationManager.getManager().isCallbackAlreadyRegistred(this)) {
            changeState(Constants.APP_STATE.STATE_DOWNLOADING);
        } else {
            changeState(Constants.APP_STATE.NOT_DOWNLOAD);
            NotificationManager.getManager().registerCallback(app, this);
        }
    }

    private void changeState(Constants.APP_STATE newState) {
        Log.d(TAG, "setButtonText() called with: appState = [" + newState + "]");
        switch (newState) {
            case STATE_DOWNLOADING:
                break;
            case STATE_DOWNLOAD_NOT_INSTALLED:
                view.setButtonText("install");
                break;
            case STATE_DOWNLOAD_ERROR:
                view.setButtonText("download");
                break;
            case STATE_INSTALLING:
                break;
            case STATE_INSTALLED:
                view.setButtonText("open");
                break;
            case STATE_INSTALL_FAIL:
                break;
            case NOT_DOWNLOAD:
                view.setButtonText("download");

        }
    }

    @Override
    public void onAppDownloadStarted() {
        Log.d(TAG, "onAppDownloadStarted() called");
    }

    @Override
    public void onAppDownloadProgressChanged(int progress) {
        view.setButtonText(String.valueOf(progress));
        Log.d(TAG, "onAppDownloadProgressChanged() called with: progress = [" + progress + "]");
    }

    @Override
    public void onAppDownloadSuccessful() {
        Log.d(TAG, "onAppDownloadSuccessful() called");
    }

    @Override
    public void onAppDownloadError() {
        Log.d(TAG, "onAppDownloadError() called");
    }

    @Override
    public void onDestroy() {
        NotificationManager.getManager().removeCallback(this);
    }
}
