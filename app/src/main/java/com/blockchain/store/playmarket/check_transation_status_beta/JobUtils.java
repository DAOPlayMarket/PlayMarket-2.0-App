package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import com.blockchain.store.playmarket.utilities.Constants;

import java.util.concurrent.TimeUnit;

public class JobUtils {
    private static final String TAG = "JobUtils";
    private static int jobId = 0;

    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L;

    public static void scheduleCheckTransactionJob(Context context, String transactionHash, Constants.TransactionTypes transactionType) {
        ComponentName jobService = new ComponentName(context, GetTransactionStatusJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(jobId++, jobService);
        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
        exerciseJobBuilder.setOverrideDeadline(0);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);
        exerciseJobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(5), JobInfo.BACKOFF_POLICY_LINEAR);

        addExtras(exerciseJobBuilder, transactionHash, transactionType);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    public static void scheduleCheckUpdateJob(Context context) {
        ComponentName jobService = new ComponentName(context, CheckUpdateJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(jobId++, jobService);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(true);
        exerciseJobBuilder.setRequiresCharging(true);
        exerciseJobBuilder.setPeriodic(ONE_DAY_INTERVAL);
        exerciseJobBuilder.setBackoffCriteria(TimeUnit.HOURS.toMillis(1), JobInfo.BACKOFF_POLICY_EXPONENTIAL);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    private static void addExtras(JobInfo.Builder exerciseJobBuilder, String transactionHash, Constants.TransactionTypes transactionType) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.JOB_HASH_EXTRA, transactionHash);
        if (transactionType != null) {
            bundle.putInt(Constants.JOB_TRANSACTION_TYPE_ORDINAL, transactionType.ordinal());
        }
        exerciseJobBuilder.setExtras(bundle);
    }

}
