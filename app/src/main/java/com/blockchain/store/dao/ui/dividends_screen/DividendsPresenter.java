package com.blockchain.store.dao.ui.dividends_screen;

import android.util.Pair;

import com.blockchain.TransactionSender;
import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.ContractReader;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class DividendsPresenter implements DividendsContract.Presenter {

    private DividendsContract.View view;

    @Override
    public void init(DividendsContract.View view) {
        this.view = view;
    }

    @Override
    public void getDividendsTokens() {
        PublishSubject<DaoToken> publishSubject = PublishSubject.create();
        publishSubject.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(view::onTokenNext, view::onTokensError, view::onTokensComplete);
        ContractReader.getDividendsTokens(publishSubject);
    }

    @Override
    public void runSingleTx(DaoToken daoToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    Transaction tx = CryptoUtils.generateDaoWithdraw(result, daoToken);
                    return tx;
                })
                .flatMap(result -> new TransactionSender().send(result))
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.GET_DIVIDENDS);
                    return result;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::receive, view::transferFailed);
    }

    @Override
    public void runDoubleTx(DaoToken daoToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    try {
                        Pair<Transaction, Transaction> stringStringPair = CryptoUtils.generateDaoTransferTransactions(result, daoToken);
                        return new Pair<>(stringStringPair.first, stringStringPair.second);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .flatMap(result -> new TransactionSender().send(result.first), Pair::new)
                .map(result -> {
                    String rawSecondTransaction = CryptoUtils.getRawTransaction(result.first.second);
                    TransactionInteractor.addToJobSchedule(result.second, result.first.second.getHash().getHex(), rawSecondTransaction, Constants.TransactionTypes.GET_DIVIDENDS);
                    return result.second;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::receive, view::transferFailed);
    }

}
