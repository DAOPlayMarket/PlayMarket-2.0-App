package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.content.LocationManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.net.APIUtils;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import java.io.IOException;
import java.util.ArrayList;

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

    private void connectToNearestNode(Location location) {
        Thread thread = new Thread(() -> {
            try {
                Log.d("Location", location.getLatitude() + "," + location.getLongitude());
                String[] nodes = NodeUtils.getNodesList(NodeUtils.NODES_DNS_SERVER);
                for (String node : nodes) {
                    Log.d("Node", node);
                }

                String nearestNodeIP = NodeUtils.getNearestNode(nodes, location.getLatitude(), location.getLatitude());
                Log.d("Node", nearestNodeIP);

                initApiUtils(nearestNodeIP);

                setContractAddress();
                String gasPrice = APIUtils.api.getGasPrice();
                Log.d(TAG, "requestUserLocation: " + gasPrice);
            } catch (IOException e) {
                e.printStackTrace();

                initApiUtils("000001");
                try {
                    setContractAddress();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        });

        thread.start();
    }

    protected void initApiUtils(String node) {
        new APIUtils(node);
    }

    protected void setContractAddress() throws IOException {
        CryptoUtils.CONTRACT_ADDRESS = APIUtils.api.getContractAddress();
    }

    @Override
    public void onLocationReady(Location location) {
        connectToNearestNode(location);

        locationManager.stopLocationServices();
//        view.onLocationReady(location);
//        connectToNearestNode(location);
    }
}
