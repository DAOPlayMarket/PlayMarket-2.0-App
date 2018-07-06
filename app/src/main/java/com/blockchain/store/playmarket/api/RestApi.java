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
    public static final String CHANGELLY_ENDPOINT = "https://api.changelly.com";


    public static String SERVER_ENDPOINT = "https://192.168.11.186:3000";
    public static String BASE_URL = SERVER_ENDPOINT + "/api/";
    public static String ICON_URL = SERVER_ENDPOINT + "/data/";

    private static String DEBUG_SERVER_ENDPOINT = "https://192.168.11.186:3000";
    private static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    private static final String TAG = "RestApi";
    private static final String AVAILABILITY_REQUEST_NAME = "/api/availability";

    private static String PORT_SUFFIX = ":3000";
    private static String nodeUrl = "https://n";
    private static ServerApi restApi;
    private static ChangellyApi changellyApi;
    private ServerApi customApi;

    public static ServerApi getServerApi() {
        if (restApi == null) {
            setupWithRest();
        }
        return restApi;
    }

    public static ChangellyApi getChangellyApi() {
        if (changellyApi == null) {
            setupWithChangelly();
        }
        return changellyApi;
    }

    public ServerApi getCustomUrlApi(String url) {
        return setupWithCustomUrl(url);


    }


    private static void setupWithRest() {
        Log.d(TAG, "setupWithRest: " + BASE_URL);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(getSllSocketFactory()).build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ResultAdapterFactory()).setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        restApi = retrofit.create(ServerApi.class);
    }

    private static void setupWithChangelly() {
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(getSllSocketFactory())
                .addInterceptor(new ChangellyInterceptor())
                .build();
        Gson gson = new GsonBuilder()
                .setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CHANGELLY_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        changellyApi = retrofit.create(ChangellyApi.class);
    }

    public static void setServerEndpoint(String serverEndpoint) {
        SERVER_ENDPOINT = nodeUrl + serverEndpoint + PLAYMARKET_BASE_URL + PORT_SUFFIX;
        BASE_URL = SERVER_ENDPOINT + "/api/";
        ICON_URL = SERVER_ENDPOINT + "/data/";
        Log.d(TAG, "setServerEndpoint: " + SERVER_ENDPOINT);
    }

    public static void setDebugEndpoint() {
        SERVER_ENDPOINT = DEBUG_SERVER_ENDPOINT;
        BASE_URL = SERVER_ENDPOINT + "/api/";
        ICON_URL = SERVER_ENDPOINT + "/data/";
        Log.d(TAG, "setServerEndpoint: " + SERVER_ENDPOINT);
    }

    public static String getCheckUrlEndpointByNode(String nodeAddress) {
        String resultUrl = nodeUrl + nodeAddress + PLAYMARKET_BASE_URL + PORT_SUFFIX + "/";
        Log.d(TAG, "getCheckUrlEndpointByNode() called with: nodeAddress = [" + resultUrl + "]");
        return resultUrl;
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

    private ServerApi setupWithCustomUrl(String url) {
        Log.d(TAG, "setupWithRest: " + BASE_URL);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(getSllSocketFactory()).build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ResultAdapterFactory()).setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ServerApi.class);
    }

}
