package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.PurchaseSDK.services.RemoteService;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;

import io.ethmobile.ethdroid.KeyManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferRepository {

    public Observable<PurchaseAppResponse> createTransaction(String transferAmountInWei, String recipientAddress,String password) {
        return RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(accountInfoResponse -> {
                    if (unlockAccount(password)) {
                        String transaction = generateTransaction(accountInfoResponse, transferAmountInWei, recipientAddress);
                        return RestApi.getServerApi().deployTransaction(transaction);
                    } else {
                        throw new IllegalArgumentException(RemoteService.WRONG_PASSWORD_ERROR);
                    }
                })
                .map(TransactionInteractor::mapWithJobSchedule)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }


    private boolean unlockAccount(String password) {
        KeyManager keyManager = Application.keyManager;
        try {
            Account account = keyManager.getAccounts().get(0);
            keyManager.unlockAccountDuring(account, password, 10);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateTransaction(AccountInfoResponse accountInfoResponse, String transferAmount, String address) {
        String rawTransaction;
        try {
            rawTransaction = CryptoUtils.generateTransferTransaction(accountInfoResponse.count, accountInfoResponse.gasPrice, transferAmount, address);
            return rawTransaction;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
