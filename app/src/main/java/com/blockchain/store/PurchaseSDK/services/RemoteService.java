package com.blockchain.store.PurchaseSDK.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blockchain.store.PurchaseSDK.repository.TestOBject;
import com.blockchain.store.PurchaseSDK.repository.TransferRepository;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.repositories.BalanceRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.net.ConnectException;
import java.net.UnknownHostException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_ERROR;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_NAME;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_RESULT;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_GET_ACCOUNT;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_GET_BALANCE;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_TRANSACTION;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.REMOTE_INTENT_NAME;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.UNKNOWN_HOST_EXCEPTION;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.USER_NOT_PROVIDED_ERROR;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.VALUE_PASSWORD;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.VALUE_RECIPIENT_ADDRESS;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.VALUE_TRANSFER_AMOUNT;

public class RemoteService extends IntentService {
    private static final String TAG = "RemoteService";


    public RemoteService() {
        super("RemoteService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isIntentForErrors(intent)) {
            return;
        }

        switch (intent.getStringExtra(EXTRA_METHOD_NAME)) {
            case METHOD_GET_ACCOUNT:
                sendUserAddress(AccountManager.getAccount().getAddress().getHex());
            case METHOD_GET_BALANCE:
                new BalanceRepository().getAccountBalance().subscribe(this::onUserBalanceReady, this::onUserBalanceError);
                sendUserAddress(AccountManager.getAccount().getAddress().getHex());
                break;
            case METHOD_TRANSACTION:
                if (!isHasAllParameters(intent)) {
                    return;
                }
                String transferAmount = intent.getStringExtra(VALUE_TRANSFER_AMOUNT);
                String recipientAddress = intent.getStringExtra(VALUE_RECIPIENT_ADDRESS);
                String password = intent.getStringExtra(VALUE_PASSWORD);

                break;
        }

        RestApi.getServerApi().getAppsByPackage(AccountManager.getAddress().getHex())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private boolean isHasAllParameters(Intent intent) {

        if (intent.hasExtra(VALUE_TRANSFER_AMOUNT)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_TRANSFER_AMOUNT);
            return false;
        }

        if (intent.hasExtra(VALUE_RECIPIENT_ADDRESS)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_RECIPIENT_ADDRESS);
            return false;
        }
        if (intent.hasExtra(VALUE_PASSWORD)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_PASSWORD);
            return false;
        }
        return true;
    }

    private boolean isIntentForErrors(Intent intent) {
        if (intent != null && intent.hasExtra(EXTRA_METHOD_NAME)) {
            checkHasAccount();
        }
        sendNoMethodName();

        return false;
    }

    private void sendNoMethodName() {

    }

    private boolean checkHasAccount() {
        if (AccountManager.getAccount() == null) {
            sendNoUserIntent(USER_NOT_PROVIDED_ERROR);
            return true;
        }
        return false;
    }

    private void sendUserAddress(String addressHex) {
        Intent outerIntent = getOuterIntent(METHOD_GET_ACCOUNT);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, addressHex);
        sendBroadCast(outerIntent);
    }

    private void onTransactionCreate(PurchaseAppResponse purchaseAppResponse) {
        Intent outerIntent = getOuterIntent(METHOD_TRANSACTION);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, purchaseAppResponse.link);
        sendBroadCast(outerIntent);
    }

    private void onTransactionFailed(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_TRANSACTION);
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, UNKNOWN_HOST_EXCEPTION);
        } else {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        }
        sendBroadCast(outerIntent);
    }

    private void onUserBalanceReady(String balance) {
        Intent outerIntent = getOuterIntent(METHOD_GET_BALANCE);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, balance);
        sendBroadCast(outerIntent);
    }

    private void onUserBalanceError(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_GET_BALANCE);
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, UNKNOWN_HOST_EXCEPTION);
        } else {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        }
        sendBroadCast(outerIntent);
    }

    private void sendNoUserIntent(String extraMethodName) {
        Intent outerIntent = getOuterIntent(extraMethodName);
        outerIntent.putExtra(EXTRA_METHOD_ERROR, USER_NOT_PROVIDED_ERROR);
        sendBroadCast(outerIntent);
    }

    private void sendEmptyLineIntent(String extraMethodName, String emptyParamName) {
        Intent outerIntent = getOuterIntent(extraMethodName);
        outerIntent.putExtra(EXTRA_METHOD_ERROR, emptyParamName + " not provided");
        sendBroadCast(outerIntent);
    }

    private Intent getOuterIntent(String methodName) {
        Intent outerIntent = new Intent(REMOTE_INTENT_NAME);
        outerIntent.putExtra(EXTRA_METHOD_NAME, methodName);
        return outerIntent;
    }

    private void sendBroadCast(Intent intent) {
        sendBroadcast(intent);
    }


}
