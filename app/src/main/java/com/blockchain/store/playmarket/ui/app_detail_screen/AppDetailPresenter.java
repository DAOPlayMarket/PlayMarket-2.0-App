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
import com.blockchain.store.playmarket.data.entities.IcoLocalData;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SendReviewTransactionModel;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.Presenter;
import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.View;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailPresenter implements Presenter, NotificationManagerCallbacks {
    private static final String TAG = "AppDetailPresenter";
    private Constants.APP_STATE appState = Constants.APP_STATE.STATE_UNKNOWN;
    private View view;
    private App app;
    private AppInfo appInfo;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void getDetailedInfo(App app) {
        String accountAddress = AccountManager.getAddress().getHex();

        Observable<AppInfo> pairObservable = RestApi.getServerApi().getAppInfo(app.catalogId, app.appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    view.setProgress(true);
                    view.showErrorView(false);
                })
                .doOnTerminate(() -> view.setProgress(false));
        if (!app.isFree()) {
            pairObservable.zipWith(RestApi.getServerApi().checkPurchase(app.appId, accountAddress), (Func2<AppInfo, Boolean, Pair>) Pair::new)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onDetailedInfoReady, this::onDetailedInfoFailed);
        } else {
            pairObservable.subscribe(this::onDetailedInfoReady, this::onDetailedInfoFailed);
        }
    }

    private void onDetailedInfoReady(AppInfo appInfo) {
        onDetailedInfoReady(new Pair<>(appInfo, false));
    }


    private void onDetailedInfoReady(Pair<AppInfo, Boolean> pair) {
        this.appInfo = pair.first;
        view.onCheckPurchaseReady(pair.second);
        view.onDetailedInfoReady(pair.first);

    }

    private void onDetailedInfoFailed(Throwable throwable) {

        view.onDetailedInfoFailed(throwable);
    }

    @Override
    public void onActionButtonClicked(App app) {
        switch (appState) {
            case STATE_DOWNLOAD_ERROR:
            case STATE_NOT_DOWNLOAD:
                NotificationManager.getManager().registerCallback(app, this);
                new MyPackageManager().startDownloadApkService(app);
                changeState(Constants.APP_STATE.STATE_DOWNLOADING);
                break;
            case STATE_HAS_UPDATE:
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
                new MyPackageManager().openAppByPackage(app.packageName);
                break;

            case STATE_INSTALLING:
            case STATE_DOWNLOAD_STARTED:
            case STATE_DOWNLOADING:
            case STATE_UNKNOWN:
                // do nothing
                break;
            case STATE_NOT_PURCHASED:
                view.showPurchaseDialog();
                break;
            case STATE_UPDATE_DOWNLOADED_NOT_INSTALLED:
                new MyPackageManager().installApkByApp(app);
                break;

        }
    }

    @Override
    public void loadButtonsState(App app, boolean isUserPurchasedApp) {
        this.app = app;
        if (app.isFree() || isUserPurchasedApp) {
            boolean applicationInstalled = new MyPackageManager().isApplicationInstalled(app.packageName);
            if (applicationInstalled) {
                changeState(new MyPackageManager().isHasUpdate(app));
            } else if (NotificationManager.getManager().isCallbackAlreadyRegistered(app)) {
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
            case STATE_HAS_UPDATE:
                view.setActionButtonText(context.getString(R.string.update));
                break;
            case STATE_INSTALLING:
                break;
            case STATE_INSTALLED:
                view.setActionButtonText(context.getString(R.string.btn_open));
                break;
            case STATE_INSTALL_FAIL:
                break;
            case STATE_NOT_PURCHASED:
                view.setActionButtonText(app.getPrice());
                break;
            case STATE_UPDATE_DOWNLOADED_NOT_INSTALLED:
                view.setActionButtonText("update from disk");
                break;


        }
        if (newState == Constants.APP_STATE.STATE_INSTALLED && !app.packageName.equalsIgnoreCase("com.blockchain.store.playmarket")) {

            view.setDeleteButtonVisibility(true);
        } else {
            view.setDeleteButtonVisibility(false);
        }
    }

    @Override
    public void onAppDownloadStarted(App app) {
        changeState(Constants.APP_STATE.STATE_DOWNLOAD_STARTED);
    }

    @Override
    public void onAppDownloadProgressChanged(App app, int progress) {
        changeState(Constants.APP_STATE.STATE_DOWNLOADING);
        view.setActionButtonText(String.valueOf(progress) + " %");
    }

    @Override
    public void onAppDownloadSuccessful(App app) {
        changeState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
    }

    @Override
    public void onAppDownloadError(App app, String message) {
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

    public void onSendReviewClicked(String review, String vote) {
        sendReview(review, vote, "");
    }

    public void onSendReviewClicked(String review, String vote, String txIndex) {
        sendReview(review, vote, txIndex);
    }

    private void sendReview(String review, String vote, String txIndex) {
        SendReviewTransactionModel transactionModel = new SendReviewTransactionModel();
        transactionModel.app = appInfo;
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(pair -> mapReviewCreationTransaction(pair, review, vote, txIndex))
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReviewSendSuccessfully, this::onReviewSendFailed);
    }

    private void onReviewSendSuccessfully(PurchaseAppResponse purchaseAppResponse) {
        view.onReviewSendSuccessfully();
    }

    private void onReviewSendFailed(Throwable throwable) {
        view.onPurchaseError(throwable);
    }

    @Override
    public void getReviews(String appId) {
        RestApi.getServerApi().getReviews(Integer.parseInt(appId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReviewReady, this::onReviewFailed);
    }

    private void onReviewReady(ArrayList<UserReview> userReviews) {
        sortUserReviews(userReviews);

    }

    private void sortUserReviews(ArrayList<UserReview> userReviews) {
        ArrayList<UserReview> flattedArrayOfReviews = new ArrayList<>();

        for (UserReview review : userReviews) {
            flattedArrayOfReviews.add(review);
            for (UserReview userReview : review.responses) {
                userReview.isReviewOnReview = true;
                flattedArrayOfReviews.add(userReview);
            }
        }
        view.onReviewsReady(flattedArrayOfReviews);

    }

    private void onReviewFailed(Throwable throwable) {
        Log.d(TAG, "onReviewFailed: ");
    }


    private Observable<PurchaseAppResponse> mapReviewCreationTransaction(Pair<AccountInfoResponse, String> accountInfo, String review, String vote, String txIndex) {

        String rawTransaction = "";
        try {
            rawTransaction = CryptoUtils.generateSendReviewTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    app, vote, review, txIndex);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);

    }

    public void loadCryptoDuelData() {
        TransactionRepository.getLocalIcoData(Constants.CRYPTO_DUEL_CONTRACT_CROWDSALE, Constants.CRYPTO_DUEL_CONTRACT_FOR_ADVER_BUDGET)
                .subscribe(this::onIcoDataReady, this::onIcoDataError);
    }

    private void onIcoDataReady(IcoLocalData icoLocalData) {
        view.onIcoDataReady(icoLocalData);
        Log.d(TAG, "onIcoDataReady: ");
    }

    private void onIcoDataError(Throwable throwable) {
        view.onIcoDataError(throwable);
        throwable.printStackTrace();
    }
}
