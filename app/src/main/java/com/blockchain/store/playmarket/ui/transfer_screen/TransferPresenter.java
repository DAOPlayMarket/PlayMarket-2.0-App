package com.blockchain.store.playmarket.ui.transfer_screen;

import android.content.Context;
import android.util.Pair;

import com.blockchain.TransactionSender;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppBuyTransactionModel;
import com.blockchain.store.playmarket.data.entities.CryptoPriceResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SendEthereumTransactionModel;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferPresenter implements TransferContract.Presenter {
    private static final String TAG = "TransferPresenter";
    private TransferContract.View view;
    private Context context;

    @Override
    public void init(TransferContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public boolean passwordCheck(String password) {
        try {
            AccountManager.getKeyManager().getKeystore().unlock(AccountManager.getAccount(), password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createTransaction(String transferAmount, String recipientAddress) {
        SendEthereumTransactionModel transactionModel = new SendEthereumTransactionModel();
        transactionModel.priceInWei = transferAmount;
        transactionModel.addressTo = recipientAddress;

        Observable<AccountInfoResponse> accountInfoResponseObservable = RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex());
        accountInfoResponseObservable
                .flatMap(accountInfoResponse -> {
                    Transaction transaction = generateTransaction(accountInfoResponse, transferAmount, recipientAddress);
                    return new TransactionSender().send(transaction);
                })

                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }


    private Transaction generateTransaction(AccountInfoResponse accountInfoResponse, String transferAmount, String address) {
        return CryptoUtils.generateTransferTransaction(accountInfoResponse.count, accountInfoResponse.getGasPrice(), transferAmount, address);

    }


    public void createBuyTransaction(App app) {
        AppBuyTransactionModel transactionModel = new AppBuyTransactionModel();
        transactionModel.boughtApp = app;
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getCryptoPrice(app.getCountOfPmcWithDecimals()), Pair::new)
                .flatMap(result -> mapAppBuyTransaction(result, app))
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(Object o) {
        view.closeTransferActivity();
    }

    private void transferFailed(Throwable throwable) {
        view.showToast(throwable.getMessage());
    }

    private Observable<String> mapAppBuyTransaction(Pair<AccountInfoResponse, CryptoPriceResponse> accountInfo, App app) {
        Transaction tx = CryptoUtils.generateAppBuyTransaction(
                accountInfo.first.count,
                new BigInt(Long.parseLong(accountInfo.first.getGasPrice())),
                app, accountInfo.first.adrNode, accountInfo.second);
        return new TransactionSender().send(tx);

    }

    public void createTransferTokenTransaction(String transferAmount, String recipientAddress, String icoAddress, TransactionModel transactionModel) {

        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .flatMap(result -> mapTokenTransfer(result, transferAmount, recipientAddress, icoAddress))
                .map(result -> TransactionInteractor.mapWithJobService(result, transactionModel))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);

    }

    private Observable<String> mapTokenTransfer(Pair<AccountInfoResponse, String> accountInfo, String transferAmount, String recipientAddress, String icoAddress) {
        Transaction rawTransaction = CryptoUtils.generateSendTokenTransaction(
                accountInfo.first.count,
                new BigInt(Long.parseLong(accountInfo.second)),
                transferAmount,
                recipientAddress,
                icoAddress);
        return new TransactionSender().send(rawTransaction);
    }
}
