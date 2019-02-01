package com.blockchain.store.playmarket.ui.token_transfer_screen;

import android.util.Pair;

import com.blockchain.TransactionSender;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import rx.Observable;
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
                .flatMap(result->new TransactionSender().send(result))
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.TRANSFER_TOKEN);
                    return result;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferFailed(Throwable error) {

    }

    private void transferSuccess(String response) {

    }

    static Pair<Transaction,Transaction> pair;
    public void sendTokenToRepository(Long approvalWithoutDecimal, Long amount) {

        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    try {
                        Transaction transaction=null;
                        if (approvalWithoutDecimal >= amount && approvalWithoutDecimal != 0) {
                            transaction = CryptoUtils.generateDepositOnlyTokenToRepositoryTx(result, amount);
                            TransactionInteractor.addToJobSchedule(transaction.getHash().getHex(), Constants.TransactionTypes.SEND_INTO_REPOSITORY);
                        } else {
                            pair = CryptoUtils.generateDepositTokenToRepositoryTx(result, amount);
                            String rawSecondTransaction = CryptoUtils.getRawTransaction(pair.second);
                            TransactionInteractor.addToJobSchedule(pair.first.getHash().getHex(), pair.second.getHash().getHex(), rawSecondTransaction, Constants.TransactionTypes.SEND_INTO_REPOSITORY);
                        }
                        return transaction;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .flatMap(result->new TransactionSender().send(result))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }
}
