package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class JobService extends android.app.job.JobService {
    private static final String TAG = "JobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        Request<?, EthGetTransactionReceipt> ethTransactionRequest = build.ethGetTransactionReceipt("0xa062245903f93135fdeb238bb42387691c395cda34ec68384b294db0f2ed75bf");
        /*
        *   try {
            Thread.sleep(3000);
            Log.d(TAG, "onStartJob: after sleep");
            jobFinished(jobParameters, false);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}
