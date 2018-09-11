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

    public static void schduleJob(Context context, String transactionHash) {
        ComponentName jobService = new ComponentName(context, JobService.class);
        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(jobId++, jobService);
        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
        exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(5));
        exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        exerciseJobBuilder.setRequiresDeviceIdle(false);
        exerciseJobBuilder.setRequiresCharging(false);
        exerciseJobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(5), JobInfo.BACKOFF_POLICY_LINEAR);


        addExtras(exerciseJobBuilder, transactionHash);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    private static void addExtras(JobInfo.Builder exerciseJobBuilder, String transactionHash) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Constants.JOB_HASH_EXTRA, transactionHash);
        exerciseJobBuilder.setExtras(bundle);
    }
}
