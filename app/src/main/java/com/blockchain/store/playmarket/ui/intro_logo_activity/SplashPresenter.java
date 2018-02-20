package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.content.LocationManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.net.APIUtils;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import java.io.IOException;

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
    public void requestUserLocation(Context context) { // todo deprecated 1.2.18. Delete later
        locationManager.getLocation(context, this);
    }

    private void getNearestNode(Location location) {
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
        Log.d(TAG, "onNearestNodeFail() called with: throwable = [" + throwable + "]");
    }

    @Override
    public void onLocationReady(Location location) {
        locationManager.stopLocationServices();
        getNearestNode(location);

    }
}
