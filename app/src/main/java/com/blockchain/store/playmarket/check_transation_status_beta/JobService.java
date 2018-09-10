package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.os.PersistableBundle;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.Constants;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class JobService extends android.app.job.JobService {
    private static final String TAG = "JobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        PersistableBundle extras = params.getExtras();
        String transactionHash = extras.getString(Constants.JOB_HASH_EXTRA);
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        EthGetTransactionReceipt send = null;
        try {
            send = build.ethGetTransactionReceipt(transactionHash).send();
            Log.d(TAG, "onStartJob: transaction status :" + send.getTransactionReceipt().getStatus());
            if (send.getTransactionReceipt().getStatus().contains("1")) {
                jobFinished(params, false);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        jobFinished(params, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}
