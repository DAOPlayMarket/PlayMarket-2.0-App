package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.content.LocationManager;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrenciesResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FileUtils;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Transaction;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.Utils;
import rx.Observable;
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
        view.setStatusText(R.string.search_for_node_fail);
        view.onNearestNodeFailed(throwable);
    }

    @Override
    public void onLocationReady(Location location) {
        locationManager.stopLocationServices();
        if (BuildConfig.DEBUG) {
            view.onLocationReady();
        } else {
            getNearestNode(location);
        }
    }
}
