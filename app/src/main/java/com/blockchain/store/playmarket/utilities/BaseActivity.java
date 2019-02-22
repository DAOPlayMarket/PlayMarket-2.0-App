package com.blockchain.store.playmarket.utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blockchain.store.playmarket.services.IpfsDaemonService;
import com.orhanobut.hawk.Hawk;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public BaseActivity() {
        LocaleUtils.updateConfig(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocaleUtils.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocaleUtils.removeActivity(this);
        if (LocaleUtils.getActivities().isEmpty()) {
            Log.d(TAG, "onStop: ");
            if (Hawk.get(Constants.IPFS_SAFE_MODE, true))
                stopService(new Intent(this, IpfsDaemonService.class));
        }
    }
}
