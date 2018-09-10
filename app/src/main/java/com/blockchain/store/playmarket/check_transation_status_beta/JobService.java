package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.os.PersistableBundle;
import android.util.Log;

import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class JobService extends android.app.job.JobService {
    private static final String TAG = "JobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
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
        if (result.getTransactionReceipt() != null && result.getTransactionReceipt().getStatus().contains("1")) {
            //todo remove notification
//            NotificationManager.getManager().registerNewNotification();
            jobFinished(params, false);
        } else {
            // todo create or update notification.
            jobFinished(params, true);
        }
    }

    private void onTransactionError(Throwable throwable, JobParameters params) {
        jobFinished(params, true);
        Log.d(TAG, "onTransactionError() called with: throwable = [" + throwable + "], params = [" + params + "]");
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}