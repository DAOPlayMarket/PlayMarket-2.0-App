package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.net.NodeUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by samsheff on 27/09/2017.
 */

public class PurchaseVerifierService extends IntentService {

    public static final long PURCHASE_CHECK_DELAY = 1000;//3600000;
    public static final String NODE_URL = "https://n";
    public static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    public static final String CHECK_PURCHASE_URL = "/v2/loadApp";
    public static String node = "";

    private KeyManager keyManager;

    public PurchaseVerifierService() {
        super("PurchaseVerifierService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.d("PMSDK", workIntent.getDataString());

        if (shouldCheckIfPurchased()) {
            setupKeyManager();

            String[] appData = workIntent.getDataString().split(":");
            String appID = appData[0];
            String catID = appData[1];
            String hashIpfs = appData[2];
            String packageName = appData[3];

            Log.d("PMSDK", "Will Verify Purchase");

            try {
                String nodeIp = getNearestNodes();
                node = NODE_URL + nodeIp + PLAYMARKET_BASE_URL;

                Log.d("PMSDK", node);
            } catch (IOException e) {
                e.printStackTrace();
            }

            checkPurchase(appID, catID, hashIpfs, packageName);
        } else {
            Log.d("PMSDK", "Will Not Verify Purchase");

        }
    }

    private void checkPurchase(String appID, String catID, String hashIpfs, String packageName) {
        try {
            URL url = new URL(makeCheckPurchaseUrl(keyManager.getAccounts().get(0).getAddress().getHex(), appID, catID, hashIpfs));
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("PMSDK", "App Unverified, Exiting");

                Intent i = new Intent();
                i.setClassName(packageName, "com.blockchain.store.playmarketsdk.services.NotPurchasedService");
                this.startService(i);

            } else {
                Log.d("PMSDK", "App Verified!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shouldCheckIfPurchased() {
        long installTime = 0;
        try {
            installTime = getInstallTime();
            long time = System.currentTimeMillis();
            return time > installTime + PURCHASE_CHECK_DELAY;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    public long getInstallTime() throws PackageManager.NameNotFoundException {
        PackageManager pm = this.getPackageManager();

        PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
        return packageInfo.firstInstallTime;
    }

    protected String getNearestNodes() throws IOException {
        ArrayList coords = NodeUtils.getCoordinates();
        Log.d("Location", coords.get(0).toString() + "," + coords.get(1).toString());

//        String[] nodes = NodeUtils.getNodesList(NodeUtils.NODES_DNS_SERVER);
//        for (String node : nodes) {
//            Log.d("Node", node);
//        }
//
//        String nearestNodeIP = NodeUtils.getNearestNode(nodes, (double) coords.get(0), (double) coords.get(1));
//        Log.d("Node", nearestNodeIP);

        return "";
    }

    public static String makeCheckPurchaseUrl(String address, String idApp, String idCat, String hashIPFS) {
        Log.d("NET", node + CHECK_PURCHASE_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS);
        return node + CHECK_PURCHASE_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS;
    }

    protected void setupKeyManager() {
        keyManager = CryptoUtils.setupKeyManager(getFilesDir().getAbsolutePath());
    }
}
