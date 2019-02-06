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
    private static int TRANSACTION_STATUS_JOB_ID = 0;
    private static int AUTO_UPDATE_JOB_ID = 1000;


    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L;
    private static final long TEN_MINUTES = 600000;

    public static void scheduleCheckTransactionJobs(Context context, String transactionHash, String secondTransactionHash, String secondRawTransaction, Constants.TransactionTypes transactionType) {
        ComponentName jobService = new ComponentName(context, GetTransactionStatusJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(TRANSACTION_STATUS_JOB_ID++, jobService);
        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
        exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(5));
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);
        exerciseJobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(5), JobInfo.BACKOFF_POLICY_LINEAR);

        addExtras(exerciseJobBuilder, transactionHash, secondTransactionHash, secondRawTransaction, transactionType);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    public static void scheduleCheckTransactionJob(Context context, String transactionHash, Constants.TransactionTypes transactionType) {
        scheduleCheckTransactionJobs(context, transactionHash, null, null, transactionType);
    }

    public static void scheduleSecondTransactionJob(Context context, String transactionHash, int transactionOrdinal) {
        Constants.TransactionTypes type = Constants.TransactionTypes.GET_DIVIDENDS;

        if (transactionOrdinal == Constants.TransactionTypes.GET_DIVIDENDS.ordinal()) {
            type = Constants.TransactionTypes.GET_DIVIDENDS;
        }
        if (transactionOrdinal == Constants.TransactionTypes.SEND_INTO_REPOSITORY.ordinal()) {
            type = Constants.TransactionTypes.SEND_INTO_REPOSITORY;
        }
        if (transactionOrdinal == Constants.TransactionTypes.WITHDRAW_TOKEN.ordinal()) {
            type = Constants.TransactionTypes.WITHDRAW_TOKEN;
        }
        TRANSACTION_STATUS_JOB_ID--;
        scheduleCheckTransactionJobs(context, transactionHash, null, null, type);
    }


    public static void scheduleCheckUpdateJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (checkIfJobAlreadyRunning(jobScheduler)) {
            return;
        }
        ComponentName jobService = new ComponentName(context, CheckUpdateJobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(AUTO_UPDATE_JOB_ID, jobService)
                .setRequiresCharging(true)
                .setPeriodic(ONE_DAY_INTERVAL)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setBackoffCriteria(TimeUnit.MINUTES.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    private static boolean checkIfJobAlreadyRunning(JobScheduler jobScheduler) {
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == AUTO_UPDATE_JOB_ID)
                return true;
        }
        return false;
    }

    private static void addExtras(JobInfo.Builder exerciseJobBuilder, String transactionHash, Constants.TransactionTypes transactionType) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.JOB_HASH_EXTRA, transactionHash);
        if (transactionType != null) {
            bundle.putInt(Constants.JOB_TRANSACTION_TYPE_ORDINAL, transactionType.ordinal());
        }
        exerciseJobBuilder.setExtras(bundle);
    }


    private static void addExtras(JobInfo.Builder exerciseJobBuilder, String transactionHash, String secondTransactionHash, String secondRawTransaction, Constants.TransactionTypes transactionType) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.JOB_HASH_EXTRA, transactionHash);
        if (secondRawTransaction != null) {
            bundle.putString(Constants.JOB_SECOND_HASH_EXTRA, secondTransactionHash);
            bundle.putString(Constants.JOB_SECOND_RAW_TX, secondRawTransaction);
        }

        if (transactionType != null) {
            bundle.putInt(Constants.JOB_TRANSACTION_TYPE_ORDINAL, transactionType.ordinal());
        }
        exerciseJobBuilder.setExtras(bundle);
    }

    public static void scheduleAppInstallJobs(Context context, String appId, String node) {
        ComponentName jobService = new ComponentName(context, ReportAppInstallJob.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(TRANSACTION_STATUS_JOB_ID++, jobService);
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);
        exerciseJobBuilder.setBackoffCriteria(TimeUnit.HOURS.toMillis(1), JobInfo.BACKOFF_POLICY_LINEAR);

        addExtras(exerciseJobBuilder, appId,node);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    private static void addExtras(JobInfo.Builder exerciseJobBuilder, String appId, String node) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.JOB_APP_ID, appId);
        bundle.putString(Constants.JOB_APP_NODE, node);
        exerciseJobBuilder.setExtras(bundle);
    }

}
