package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.repositories.BalanceRepository;
import com.blockchain.store.playmarket.repositories.TransferRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class RemoteService extends IntentService {
    private static final String TAG = "RemoteService";
    private static final String EXTRA_METHOD_NAME = "method_name";
    private static final String METHOD_GET_BALANCE = "method_get_balance";
    private static final String METHOD_GET_ACCOUNT = "method_get_account";
    private static final String METHOD_TRANSACTION = "method_get_transaction";
    private static final String EXTRA_METHOD_RESULT = "method_extra_result";
    private static final String EXTRA_METHOD_ERROR = "method_extra_error";
    private static final String REMOTE_INTENT_NAME = "RemoteService";
    private static final String USER_NOT_PROVIDED_ERROR = "user_not_provided_error";
    private static final String UNKNOWN_HOST_EXCEPTION = "unknown_host_exception";

    private static final String VALUE_TRANSFER_AMOUNT = "transfer_amount";
    private static final String VALUE_RECIPIENT_ADDRESS = "recipient_address";
    private static final String VALUE_PASSWORD = "user_password";

    public static final String WRONG_PASSWORD_ERROR = "password_wrong_error";

    public RemoteService() {
        super("RemoteService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.hasExtra(EXTRA_METHOD_NAME)) {
            if (AccountManager.getAccount() == null) {
                sendNoUserIntent(USER_NOT_PROVIDED_ERROR);
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
                    String transferAmount = intent.getStringExtra(VALUE_TRANSFER_AMOUNT);
                    if (transferAmount == null) {
                        sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_TRANSFER_AMOUNT);
                        return;
                    }
                    String recipientAddress = intent.getStringExtra(VALUE_RECIPIENT_ADDRESS);
                    if (recipientAddress == null) {
                        sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_RECIPIENT_ADDRESS);
                        return;
                    }
                    String password = intent.getStringExtra(VALUE_PASSWORD);
                    if (password == null) {
                        sendEmptyLineIntent(METHOD_TRANSACTION, VALUE_PASSWORD);
                        return;
                    }
                    new TransferRepository().createTransaction(transferAmount, recipientAddress, password)
                            .subscribe(this::onTransactionCreate, this::onTransactionFailed);
                    break;
            }
        }
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
