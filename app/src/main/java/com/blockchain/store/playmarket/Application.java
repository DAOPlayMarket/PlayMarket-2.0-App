package com.blockchain.store.playmarket;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.blockchain.store.playmarket.data.content.AppsDispatcher;
import com.blockchain.store.playmarket.data.content.AppsManager;
import com.blockchain.store.playmarket.data.entities.AppBuyTransactionModel;
import com.blockchain.store.playmarket.data.entities.InvestTransactionModel;
import com.blockchain.store.playmarket.data.entities.SendEthereumTransactionModel;
import com.blockchain.store.playmarket.data.entities.SendReviewTransactionModel;
import com.blockchain.store.playmarket.data.entities.SendTokenTransactionModel;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.http.AsyncSSLSocketMiddleware;
import com.koushikdutta.ion.Ion;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.Parser;

import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.ethmobile.ethdroid.KeyManager;
import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

import static com.blockchain.store.playmarket.api.RestApi.getSllSocketFactory;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class Application extends MultiDexApplication {
    private static final String TAG = "Application";

    public static KeyManager keyManager;
    private static AppsDispatcher appsDispatcher;
    private static AppsManager appsManager;
    private static Application instance;
    private static PinpointManager pinpointManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        MultiDex.install(this);
        ToastUtil.setContext(this);
        keyManager = KeyManager.newKeyManager(getFilesDir().getAbsolutePath());
        AccountManager.setKeyManager(keyManager);
        Hawk.init(this).setParser(new Parser() {
            @Override
            public <T> T fromJson(String content, Type type) throws Exception {
                if (TextUtils.isEmpty(content)) {
                    return null;
                }
                try {
                    JsonObject object = new Gson().fromJson(content, JsonObject.class);
                    Log.d(TAG, "fromJson: ");
                    if (object.has("TransactionType")) {
                        int viewType = object.get("TransactionType").getAsInt();

                        if (viewType == Constants.TransactionTypes.BUY_APP.ordinal()) {
                            return new Gson().fromJson(content, (Class<T>) AppBuyTransactionModel.class);
                        }
                        if (viewType == Constants.TransactionTypes.INVEST.ordinal()) {
                            return new Gson().fromJson(content, (Class<T>) InvestTransactionModel.class);
                        }
                        if (viewType == Constants.TransactionTypes.TRANSFER.ordinal()) {
                            return new Gson().fromJson(content, (Class<T>) SendEthereumTransactionModel.class);
                        }
                        if (viewType == Constants.TransactionTypes.TRANSFER_TOKEN.ordinal()) {
                            return new Gson().fromJson(content, (Class<T>) SendTokenTransactionModel.class);
                        }
                        if (viewType == Constants.TransactionTypes.SEND_REVIEW.ordinal()) {
                            return new Gson().fromJson(content, (Class<T>) SendReviewTransactionModel.class);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return new Gson().fromJson(content, type);
            }

            @Override
            public String toJson(Object body) {
                return new Gson().toJson(body);
            }
        }).build();

        setUntrustedManager();
        setUpAWS();
    }

    private void setUntrustedManager() {
        setUpFresco();
        setUpIon();

    }

    private void setUpIon() {
        setSelfSignedSSL(this);
    }

    private void setUpFresco() {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this,
                        new OkHttpClient.Builder()
                                .sslSocketFactory(getSllSocketFactory())
                                .hostnameVerifier((hostname, session) -> true)
                                .build()).build();
        Fresco.initialize(this, config);
    }

    public static AppsDispatcher getAppsDispatcher() {
        if (appsDispatcher == null) {
            appsDispatcher = new AppsDispatcher();
        }
        return appsDispatcher;
    }

    public static AppsManager getAppsManager() {
        if (appsManager == null) {
            appsManager = new AppsManager();
        }
        return appsManager;
    }

    public static Application getInstance() {
        return instance;
    }

    public static boolean isForeground() {
        return instance == null;
    }


    public void setSelfSignedSSL(Context mContext) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(null, null);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, null);
            AsyncSSLSocketMiddleware sslMiddleWare;
            sslMiddleWare = Ion.getDefault(mContext).getHttpClient().getSSLSocketMiddleware();
            sslMiddleWare.setTrustManagers(wrappedTrustManagers);
            sslMiddleWare.setHostnameVerifier(getHostnameVerifier());
            sslMiddleWare.setSSLContext(sslContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HostnameVerifier getHostnameVerifier() {
        return (hostname, session) -> true;
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0) {
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0) {
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }


    private void setUpAWS() {
        AWSMobileClient.getInstance().initialize(this).execute();
        PinpointConfiguration config = new PinpointConfiguration(
                this,
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());
        pinpointManager = new PinpointManager(config);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();

    }

    public static void stopAnalytic() {
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();
    }

}
