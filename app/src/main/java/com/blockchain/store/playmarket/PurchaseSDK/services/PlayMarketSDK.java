package com.blockchain.store.playmarket.PurchaseSDK.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.blockchain.store.playmarket.PurchaseSDK.entities.TransferObject;
import com.blockchain.store.playmarket.PurchaseSDK.repository.PlayMarketSdkTransactionFactory;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.repositories.BalanceRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.UnknownHostException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_ERROR;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_NAME;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.EXTRA_METHOD_RESULT;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.METHOD_CHECK_BUY;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.METHOD_CHECK_SUBSCRIPTION;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.METHOD_GET_ACCOUNT;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.METHOD_GET_BALANCE;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.METHOD_TRANSACTION;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.REMOTE_INTENT_NAME;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSACTION_RESULT_TXHASH;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSACTION_RESULT_URL;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_OBJECT_ID;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_PACKAGE_NAME;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_PASSWORD;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_PRICE;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_RECIPIENT_ADDRESS;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.TRANSFER_TRANSACTION_TYPE;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.UNKNOWN_HOST_EXCEPTION;
import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.USER_NOT_PROVIDED_ERROR;

public class PlayMarketSDK extends JobIntentService {
    private static final String TAG = "PlayMarketSDK";

    public PlayMarketSDK() {
        Log.d(TAG, "PlayMarketSDK: ");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id_2", "Playmarket SDK channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id_2");

            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(1, notificationBuilder.build());
        }
        sendTestIntent();
    }

    private void sendTestIntent() {
        Intent outerIntent = getOuterIntent(METHOD_GET_ACCOUNT);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, "test");
        sendBroadCast(outerIntent);
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return new Binder();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        start(intent);
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
        stopForeground(true);
    }

    private void onCheckBuyReady(Boolean isBought) {
        Intent outerIntent = getOuterIntent(METHOD_CHECK_BUY);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, isBought);
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onCheckBuyError(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_CHECK_BUY);
        outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onCheckSubscriptionReady(BigInteger timeElapsed) {
        Intent outerIntent = getOuterIntent(METHOD_CHECK_SUBSCRIPTION);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, timeElapsed);
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onCheckSubscriptionError(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_CHECK_SUBSCRIPTION);
        outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onTransactionFailed(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_TRANSACTION);
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, UNKNOWN_HOST_EXCEPTION);
        } else {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        }
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onUserBalanceReady(String balance) {
        Intent outerIntent = getOuterIntent(METHOD_GET_BALANCE);
        outerIntent.putExtra(EXTRA_METHOD_RESULT, new EthereumPrice(balance).inEther().toPlainString());
        sendBroadCast(outerIntent);
        stopForeground(true);
    }

    private void onUserBalanceError(Throwable throwable) {
        Intent outerIntent = getOuterIntent(METHOD_GET_BALANCE);
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, UNKNOWN_HOST_EXCEPTION);
        } else {
            outerIntent.putExtra(EXTRA_METHOD_ERROR, throwable.getMessage());
        }
        sendBroadCast(outerIntent);
        stopForeground(true);
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


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        try {
            start(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_REDELIVER_INTENT;
    }

    private void start(Intent intent) {
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
                        .subscribe(this::onTransactionCreate, this::onTransactionFailed);
                break;
            case METHOD_CHECK_BUY:
                PlayMarketSdkTransactionFactory.checkBuy(intent.getStringExtra(TRANSFER_PACKAGE_NAME), intent.getStringExtra(TRANSFER_OBJECT_ID))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(this::onCheckBuyReady, this::onCheckBuyError);
                break;
            case METHOD_CHECK_SUBSCRIPTION:
                PlayMarketSdkTransactionFactory.checkSubscription(intent.getStringExtra(TRANSFER_PACKAGE_NAME), intent.getStringExtra(TRANSFER_OBJECT_ID))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(this::onCheckSubscriptionReady, this::onCheckSubscriptionError);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
