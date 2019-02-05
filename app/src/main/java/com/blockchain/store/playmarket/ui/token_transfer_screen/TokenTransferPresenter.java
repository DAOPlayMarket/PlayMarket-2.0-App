package com.blockchain.store.playmarket.ui.token_transfer_screen;

import android.util.Pair;

import com.blockchain.TransactionSender;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.token_transfer_screen.TokenTransferContract.Presenter;
import static com.blockchain.store.playmarket.ui.token_transfer_screen.TokenTransferContract.View;

public class TokenTransferPresenter implements Presenter {
    View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    public void sendTokenToUser(Long amount, String userAddress, boolean isOpenAsCryptoDuelToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    Transaction notSignedTx;
                    if (isOpenAsCryptoDuelToken) {
                        notSignedTx = CryptoUtils.generateCDLTSendTokenToUser(result, userAddress, String.valueOf(amount));
                    } else {
                        notSignedTx = CryptoUtils.generateDaoSendTokenToUser(result, userAddress, String.valueOf(amount));
                    }
                    return notSignedTx;
                })
                .flatMap(result -> new TransactionSender().send(result))
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.TRANSFER_TOKEN);
                    return result;
                })
//                .doOnSubscribe(() -> view.setProgressVisibility(true))
//                .doOnTerminate(() -> view.setProgressVisibility(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferFailed(Throwable error) {
        view.transferFailed(error);
    }

    private void transferSuccess(String response) {
        view.transferSuccess(response);
    }

    static Pair<Transaction, Transaction> pair;

    public void sendTokenToRepository(Long approvalWithoutDecimal, Long amount) {

        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    try {
                        Transaction transaction = null;
                        if (approvalWithoutDecimal >= amount && approvalWithoutDecimal != 0) {
                            transaction = CryptoUtils.generateDepositOnlyTokenToRepositoryTx(result, amount);
                        } else {
                            pair = CryptoUtils.generateDepositTokenToRepositoryTx(result, amount);
                            transaction = pair.first;
                        }
                        return transaction;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .flatMap(result -> new TransactionSender().send(result))
                .map(result -> {
                    if (pair == null) {
                        TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.SEND_INTO_REPOSITORY);
                    } else {
                        String rawSecondTransaction = CryptoUtils.getRawTransaction(pair.second);
                        TransactionInteractor.addToJobSchedule(result, pair.second.getHash().getHex(), rawSecondTransaction, Constants.TransactionTypes.SEND_INTO_REPOSITORY);
                    }
                    return result;
                })
//                .doOnSubscribe(() -> view.setProgressVisibility(true))
//                .doOnTerminate(() -> view.setProgressVisibility(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    public void withdraw(Long amount, boolean isOpenAsCryptoDuelToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    try {
                        Transaction tx;
                        if (isOpenAsCryptoDuelToken) {
                            tx = CryptoUtils.generateWithDrawCDLTTokens(result, amount);
                        } else {
                            tx = CryptoUtils.generateWithDrawPmtTokens(result, amount);
                        }

                        return tx;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .flatMap(result -> new TransactionSender().send(result))
                .map(result -> {
                            new TransactionInteractor().addToJobSchedule(result, Constants.TransactionTypes.WITHDRAW_TOKEN);
                            return result;
                        }
                )
//                .doOnSubscribe(() -> view.setProgressVisibility(true))
//                .doOnTerminate(() -> view.setProgressVisibility(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }
}
