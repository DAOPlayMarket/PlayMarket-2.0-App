package com.blockchain.store.playmarket.ui.all_reviews_screen;

import android.util.Pair;

import com.blockchain.TransactionSender;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SendReviewTransactionModel;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.all_reviews_screen.AllReviewsContract.Presenter;
import static com.blockchain.store.playmarket.ui.all_reviews_screen.AllReviewsContract.View;

public class AllReviewPresenter implements Presenter {
    private View view;
    private App app;

    @Override
    public void init(View view, App app) {
        this.view = view;
        this.app = app;
    }

    public void onSendReviewClicked(String review, String vote) {
        sendReview(review, vote, "");
    }

    public void onSendReviewClicked(String review, String vote, String txIndex) {
        sendReview(review, vote, txIndex);
    }

    private void sendReview(String review, String vote, String txIndex) {
        SendReviewTransactionModel transactionModel = new SendReviewTransactionModel();
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(pair -> mapReviewCreationTransaction(pair, review, vote, txIndex))
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReviewSendSuccessfully, this::onReviewSendFailed);
    }

    private void onReviewSendSuccessfully(Object o) {
        view.onReviewSendSuccessfully();
    }

    private void onReviewSendFailed(Throwable throwable) {
        view.onReviewError(throwable);
    }

    private Observable<String> mapReviewCreationTransaction(Pair<AccountInfoResponse, String> accountInfo, String review, String vote, String txIndex) {
        Transaction rawTransaction =  CryptoUtils.generateSendReviewTransaction(
                    accountInfo.first.count,
                    new BigInt(Long.parseLong(accountInfo.second)),
                    app, vote, review, txIndex);
        return new TransactionSender().send(rawTransaction);

    }
}
