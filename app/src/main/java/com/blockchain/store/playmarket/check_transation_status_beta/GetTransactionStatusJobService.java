package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.os.PersistableBundle;
import android.util.Log;

import com.blockchain.store.playmarket.data.entities.TransactionNotification;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class GetTransactionStatusJobService extends android.app.job.JobService {
    private static final String TAG = "TransactionJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        NotificationManager.getManager().registerNewNotification(getNotification(params));
        PersistableBundle extras = params.getExtras();
        String transactionHash = extras.getString(Constants.JOB_HASH_EXTRA);
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        build.ethGetTransactionReceipt(transactionHash).observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> onTransactionReady(result, params),
                        throwable -> onTransactionError(throwable, params));
        return true;
    }

    private void onTransactionReady(EthGetTransactionReceipt result, JobParameters params) {
        if (result.getTransactionReceipt() != null) {
            jobFinished(params, false);
            NotificationManager.getManager().downloadCompleteWithoutError(getNotification(params));
            TransactionPrefsUtil.updateModel(result.getTransactionReceipt());
        } else {
            jobFinished(params, true);
        }

    }

    private TransactionNotification getNotification(JobParameters params) {
        PersistableBundle extras = params.getExtras();
        int transactionType = extras.getInt(Constants.JOB_TRANSACTION_TYPE_ORDINAL);
        return new TransactionNotification(params.getJobId(), transactionType);
    }

    private void onTransactionError(Throwable throwable, JobParameters params) {
        jobFinished(params, true);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
