package com.blockchain.store.playmarket;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.blockchain.store.playmarket.data.content.AppsDispatcher;
import com.blockchain.store.playmarket.data.content.AppsManager;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.koushikdutta.async.http.AsyncSSLSocketMiddleware;
import com.koushikdutta.async.http.spdy.SpdyMiddleware;
import com.koushikdutta.ion.Ion;
import com.orhanobut.hawk.Hawk;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
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
        Hawk.init(this).build();
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
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
                // or the following:
                // HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                // return hv.verify("www.yourserver.com", session);
            }
        };
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
