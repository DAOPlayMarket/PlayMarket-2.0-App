package com.blockchain.store.playmarket.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class RestApi {
    public static final String BASE_URL_INFURA = "https://rinkeby.infura.io/iYGysj5Sns7HV42MdiXi/";

    public static String SERVER_ENDPOINT = "http://192.168.11.186:3000";
    public static String BASE_URL = SERVER_ENDPOINT + "/api/";
    public static String ICON_URL = SERVER_ENDPOINT + "/data/";

    private static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    private static final String TAG = "RestApi";

    private static String PORT_SUFFIX = ":3000";
    private static String nodeUrl = "https://n";
    private static ServerApi restApi;
    private static InfuraApi infuraApi;

    public static ServerApi getServerApi() {
        if (restApi == null) {
            setupWithRest();
        }
        return restApi;
    }

    public static InfuraApi getInfuraApi() {
        if (infuraApi == null) {
            setupWithInfura();
        }
        return infuraApi;
    }

    private static void setupWithRest() {
        Log.d(TAG, "setupWithRest: " + BASE_URL);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .sslSocketFactory(getSllSocketFactory()).build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ResultAdapterFactory()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        restApi = retrofit.create(ServerApi.class);
    }

    private static void setupWithInfura() {
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(getSllSocketFactory())
                .build();
        Gson gson = new GsonBuilder()
                .setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_INFURA)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        infuraApi = retrofit.create(InfuraApi.class);
    }

    public static void setServerEndpoint(String serverEndpoint) {
        SERVER_ENDPOINT = nodeUrl + serverEndpoint + PLAYMARKET_BASE_URL + PORT_SUFFIX;
        BASE_URL = SERVER_ENDPOINT + "/api/";
        ICON_URL = SERVER_ENDPOINT + "/data/";
        Log.d(TAG, "setServerEndpoint: " + SERVER_ENDPOINT);
    }

    public static SSLSocketFactory getSllSocketFactory() {
        // Install the all-trusting trust manager
        final SSLContext sslContext;

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            return null;
        }
    }
}
