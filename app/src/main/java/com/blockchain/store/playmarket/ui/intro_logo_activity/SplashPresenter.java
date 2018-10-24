package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.content.LocationManager;
import com.blockchain.store.playmarket.data.entities.Node;
import com.blockchain.store.playmarket.fabric.EventsHelper;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Future;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashPresenter implements SplashContracts.Presenter, LocationManager.LocationManagerCallback {
    private static final String TAG = "SplashPresenter";
    private SplashContracts.View view;
    private LocationManager locationManager;
    private Subscription nearestNodeSubscription;

    @Override
    public void init(SplashContracts.View view) {
        this.view = view;
        locationManager = new LocationManager();
    }

    @Override
    public void requestUserLocation(Context context) {
        view.setStatusText(R.string.network_status_location_search);
        if (isEmulator()) {
            Location location = new Location("");
            location.setLongitude(37.22358);
            location.setLatitude(45.2376);
            getNearestNode(location);
            //view.onLocationReady();
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
        nearestNodeSubscription = new NodeUtils().getNearestNode(location)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNearestNodeFound, this::onNearestNodeFail);

    }

    private void onNearestNodeFound(Node node) {
        Log.d(TAG, "onNearestNodeFound() called with: node = [" + node + "]");
        if (!nearestNodeSubscription.isUnsubscribed()) {
            nearestNodeSubscription.unsubscribe();
        }
        RestApi.setServerEndpoint(node.address);
        view.onLocationReady();
    }

    private void onNearestNodeFail(Throwable throwable) {
        EventsHelper.logExceptions(throwable);
        throwable.getStackTrace().toString();


        Writer writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        String error = writer.toString();

        view.setStatusText(R.string.search_for_node_fail,  error);
        view.onNearestNodeFailed(throwable);
    }

    @Override
    public void onLocationReady(Location location) {
        locationManager.stopLocationServices();
        if (BuildConfig.DEBUG) {
//            view.onLocationReady();
            getNearestNode(location);
        } else {
            getNearestNode(location);
        }
    }

}
