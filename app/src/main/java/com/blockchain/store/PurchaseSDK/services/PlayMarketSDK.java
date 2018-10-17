package com.blockchain.store.PurchaseSDK.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.blockchain.store.PurchaseSDK.entities.TransferObject;
import com.blockchain.store.PurchaseSDK.repository.PlayMarketSdkTransactionFactory;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.repositories.BalanceRepository;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.net.ConnectException;
import java.net.UnknownHostException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_ERROR;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_NAME;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_RESULT;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_CHECK_BUY;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_CHECK_SUBSCRIPTION;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_GET_ACCOUNT;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_GET_BALANCE;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.METHOD_TRANSACTION;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.REMOTE_INTENT_NAME;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSACTION_RESULT_TXHASH;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSACTION_RESULT_URL;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_OBJECT_ID;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_PACKAGE_NAME;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_PASSWORD;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_PRICE;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_RECIPIENT_ADDRESS;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.TRANSFER_TRANSACTION_TYPE;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.UNKNOWN_HOST_EXCEPTION;
import static com.blockchain.store.PurchaseSDK.services.RemoteConstants.USER_NOT_PROVIDED_ERROR;

public class PlayMarketSDK extends IntentService {
    private static final String TAG = "PlayMarketSDK";


    public PlayMarketSDK() {
        super("PlayMarketSDK");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        if (!isIntentForErrors(intent)) {
//            return;
//        }

        switch (intent.getStringExtra(EXTRA_METHOD_NAME)) {
            case METHOD_GET_ACCOUNT:
                sendUserAddress(AccountManager.getAccount().getAddress().getHex());
            case METHOD_GET_BALANCE:
                new BalanceRepository().getAccountBalance().subscribe(this::onUserBalanceReady, this::onUserBalanceError);
                sendUserAddress(AccountManager.getAccount().getAddress().getHex());
                break;
            case METHOD_TRANSACTION:
//                if (!isHasAllParameters(intent)) {
//                    return;
//                }

                String transferPrice = intent.getStringExtra(TRANSFER_PRICE);
                String packageName = intent.getStringExtra(TRANSFER_PACKAGE_NAME);
                String password = intent.getStringExtra(TRANSFER_PASSWORD);
                String objectId = intent.getStringExtra(TRANSFER_OBJECT_ID);
                int transactionType = intent.getIntExtra(TRANSFER_TRANSACTION_TYPE, -1);


                TransferObject transferObject = new TransferObject();
                transferObject.setTransactionType(transactionType);
                transferObject.setTransferPrice(transferPrice);
                transferObject.setPackageName(packageName);
                transferObject.setPassword(password);
                transferObject.setObjectId(objectId);
                transferObject.setUserAddress(AccountManager.getAccount().getAddress().getHex());

                PlayMarketSdkTransactionFactory.get(transferObject)
                        .subscribe(this::onTransactionCreate, this::onUserBalanceError);
                break;
            case METHOD_CHECK_BUY:
                intent.getStringExtra(TRANSFER_OBJECT_ID);
                TransactionRepository.getCheckBuy(0, "0")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(this::onOk, this::onFail);
                break;
            case METHOD_CHECK_SUBSCRIPTION:
                TransactionRepository.getSubscriptionTime(50, "1")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(this::onOk, this::onFail);
                break;
        }


    }

    private boolean isHasAllParameters(Intent intent) {

        if (intent.hasExtra(TRANSFER_PRICE)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, TRANSFER_PRICE);
            return false;
        }

        if (intent.hasExtra(TRANSFER_RECIPIENT_ADDRESS)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, TRANSFER_RECIPIENT_ADDRESS);
            return false;
        }
        if (intent.hasExtra(TRANSFER_PASSWORD)) {
            sendEmptyLineIntent(METHOD_TRANSACTION, TRANSFER_PASSWORD);
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
        outerIntent.putExtra(TRANSACTION_RESULT_URL, purchaseAppResponse.link);
        outerIntent.putExtra(TRANSACTION_RESULT_TXHASH, purchaseAppResponse.hash);
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
        outerIntent.putExtra(EXTRA_METHOD_RESULT, new EthereumPrice(balance).inEther().toPlainString());
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
