package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.util.Log;

import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.net.APIUtils;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashPresenter implements SplashContracts.Presenter {
    private SplashContracts.View view;

    @Override
    public void init(SplashContracts.View view) {
        this.view = view;

    }

    @Override
    public void getNearestNodes() {
        Thread thread = new Thread(() -> {
            try {
                ArrayList coords = NodeUtils.getCoordinates();
                Log.d("Location", coords.get(0).toString() + "," + coords.get(1).toString());

                String[] nodes = NodeUtils.getNodesList(NodeUtils.NODES_DNS_SERVER);
                for (String node : nodes) {
                    Log.d("Node", node);
                }

                String nearestNodeIP = NodeUtils.getNearestNode(nodes, (double) coords.get(0), (double) coords.get(1));
                Log.d("Node", nearestNodeIP);

                initApiUtils(nearestNodeIP);

                setContractAddress();
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
}
