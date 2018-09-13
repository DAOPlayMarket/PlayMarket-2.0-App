package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.blockchain.store.playmarket.data.entities.TransactionNotification;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class GetTransactionStatusJobService extends android.app.job.JobService {
    private static final String TAG = "JobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        NotificationManager.getManager().registerNewNotification(new TransactionNotification(params.getJobId()));
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
        Log.d(TAG, "onTransactionReady: " + params.getJobId());

        if (result.getTransactionReceipt() != null && result.getTransactionReceipt().getStatus().contains("1")) {
            NotificationManager.getManager().downloadCompleteWithoutError(new TransactionNotification(params.getJobId()));
            jobFinished(params, false);
        } else {
            jobFinished(params, true);
        }
    }

    private void onTransactionError(Throwable throwable, JobParameters params) {
        jobFinished(params, true);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
