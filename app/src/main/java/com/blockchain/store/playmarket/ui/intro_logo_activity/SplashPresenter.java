package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.content.LocationManager;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrenciesResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashPresenter implements SplashContracts.Presenter, LocationManager.LocationManagerCallback {
    private static final String TAG = "SplashPresenter";
    private SplashContracts.View view;
    private LocationManager locationManager;

    @Override
    public void init(SplashContracts.View view) {
        this.view = view;
        locationManager = new LocationManager();
    }

    @Override
    public void requestUserLocation(Context context) {
        view.setStatusText(R.string.network_status_location_search);
        if (isEmulator()) {
            view.onLocationReady();
        } else {
            locationManager.getLocation(context, this);
        }

    }


    private static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private void getNearestNode(Location location) {
        view.setStatusText(R.string.network_status_node_search);
        new NodeUtils().getNearestNode(location)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNearestNodeFound, this::onNearestNodeFail);
    }

    private void onNearestNodeFound(String nearestNodeIp) {
        Log.d(TAG, "onNearestNodeFound() called with: nearestNodeIp = [" + nearestNodeIp + "]");
        RestApi.setServerEndpoint(nearestNodeIp);
        view.onLocationReady();
    }

    private void onNearestNodeFail(Throwable throwable) {
        view.setStatusText("Search for the nearest node fail: " + throwable.getMessage());
    }

    @Override
    public void onLocationReady(Location location) {
//        testChangelly();
        locationManager.stopLocationServices();
////        getNearestNode(location);
        view.onLocationReady();

    }


}
