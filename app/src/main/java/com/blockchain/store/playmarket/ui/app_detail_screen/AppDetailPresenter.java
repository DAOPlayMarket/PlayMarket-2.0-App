package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CheckPurchaseResponse;
import com.blockchain.store.playmarket.data.entities.InvestAddressResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.orhanobut.hawk.Hawk;

import org.ethereum.geth.BigInt;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.*;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailPresenter implements Presenter, NotificationManagerCallbacks {
    private static final String TAG = "AppDetailPresenter";
    private Constants.APP_STATE appState = Constants.APP_STATE.STATE_UNKOWN;
    private View view;
    private App app;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void getDetailedInfo(App app) {
        String accountAddress = AccountManager.getAddress().getHex();
        RestApi.getServerApi().getAppInfo(app.catalogId, app.appId)
                .zipWith(RestApi.getServerApi().checkPurchase(app.appId, accountAddress), (Func2<AppInfo, Boolean, Pair>) Pair::new)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    view.setProgress(true);
                    view.showErrorView(false);
                })
                .doOnTerminate(() -> view.setProgress(false))
                .subscribe(this::onDetailedInfoReady, this::onDetailedInfoFailed);

    }

    private void onDetailedInfoReady(Pair<AppInfo, Boolean> pair) {
        view.onCheckPurchaseReady(pair.second);
        view.onDetailedInfoReady(pair.first);
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
                addItemToLibrary(app);
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
                // do nothing
                break;
            case STATE_NOT_PURCHASED:
                view.showPurchaseDialog();

        }
    }

    private void addItemToLibrary(App app) {
        ArrayList<App> appList = new ArrayList<>();
        if (Hawk.contains(Constants.DOWNLOADED_APPS_LIST)) {
            appList = Hawk.get(Constants.DOWNLOADED_APPS_LIST);
            if (appList.contains(app)) return;
        }
        appList.add(app);
        Hawk.put(Constants.DOWNLOADED_APPS_LIST, appList);
    }

    @Override
    public void loadButtonsState(App app, boolean isUserPurchasedApp) {
        this.app = app;
        if (app.isFree || isUserPurchasedApp) {
            boolean applicationInstalled = new MyPackageManager().isApplicationInstalled(app.hash);
            if (applicationInstalled) {
                changeState(Constants.APP_STATE.STATE_INSTALLED);
            } else if (NotificationManager.getManager().isCallbackAlreadyRegistered(app, this)) {
                NotificationManager.getManager().registerCallback(app, this);
                changeState(Constants.APP_STATE.STATE_DOWNLOADING);
            } else if (new MyPackageManager().isAppFileExists(app)) {
                changeState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
            } else {
                changeState(Constants.APP_STATE.STATE_NOT_DOWNLOAD);
            }
        } else {
            changeState(Constants.APP_STATE.STATE_NOT_PURCHASED);
        }
    }

    private void changeState(Constants.APP_STATE newState) {
        appState = newState;
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
            case STATE_NOT_PURCHASED:
                view.setActionButtonText(new EthereumPrice(app.price).inEther().toString() + " ETH");
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
    }

    @Override
    public void onAppDownloadProgressChanged(int progress) {
        changeState(Constants.APP_STATE.STATE_DOWNLOADING);
        view.setActionButtonText(String.valueOf(progress) + " %");
    }

    @Override
    public void onAppDownloadSuccessful() {
        changeState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
    }

    @Override
    public void onAppDownloadError() {
        changeState(Constants.APP_STATE.STATE_DOWNLOAD_ERROR);
    }

    @Override
    public void onDestroy(App app) {
        NotificationManager.getManager().removeCallback(app, this);
    }

    @Override
    public void onDeleteButtonClicked(App app) {
        new MyPackageManager().uninstallApkByApp(app);
    }

    @Override
    public void onPurchasedClicked(AppInfo appInfo) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(this::mapAppBuyTransaction)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPurchaseSuccessful, this::onPurchaseError);
    }

    @Override
    public void getReviews() {
        view.onReviewsReady();
    }


    private Observable<PurchaseAppResponse> mapAppBuyTransaction(Pair<AccountInfoResponse, String> accountInfo) {

        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateAppBuyTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    app);
            Log.d(TAG, "handleAccountInfoResult: " + rawTransaction);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().purchaseApp(rawTransaction);

    }

    private void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse) {
        view.onPurchaseSuccessful(purchaseAppResponse);
    }


    private void onPurchaseError(Throwable throwable) {
        view.onPurchaseError(throwable);
    }
}
