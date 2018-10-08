package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.hawk.Hawk;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class RestApi {
    public static final String BASE_URL_INFURA_RINKEBY = "https://rinkeby.infura.io/iYGysj5Sns7HV42MdiXi/";
    public static final String BASE_URL_INFURA_MAINNET_ = "https://mainnet.infura.io/iYGysj5Sns7HV42MdiXi/";
    public static final String BASE_URL_INFURA = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? BASE_URL_INFURA_MAINNET_ : BASE_URL_INFURA_RINKEBY);

    public static final String CHANGELLY_ENDPOINT = "https://api.changelly.com";

    public static String SERVER_ENDPOINT_WITHOUT_POST;
    public static String SERVER_ENDPOINT = "";
    public static String BASE_URL = SERVER_ENDPOINT + "/api/";
    public static String ICON_URL = SERVER_ENDPOINT + "/data/";

    private static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    private static final String TAG = "RestApi";

    private static String NODE_PREFIX_TESTNET = "https://t";
    private static String NODE_PREFIX_MAINNET = "https://m";
    private static String NODE_PREFIX = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? NODE_PREFIX_MAINNET : NODE_PREFIX_TESTNET);
    private static ServerApi restApi;
    private static ChangellyApi changellyApi;
    private static ServerApi xmlApi;


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

    public static ServerApi getXmlApi() {
        if (xmlApi == null) {
            setupWithXML();
        }
        return xmlApi;
    }

    public ServerApi getCustomUrlApi(String url) {
        return setupWithCustomUrl(url);
    }


    private static void setupWithRest() {
        checkIfBaseUrlIsEmptyAndSetup(SERVER_ENDPOINT);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .build();

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

    private static void checkIfBaseUrlIsEmptyAndSetup(String baseUrl) {
        if (baseUrl.isEmpty()) {
            setUpBaseUrl();
        }
    }

    private static void setUpBaseUrl() {
        String lastKnownBaseUrl = Hawk.get(Constants.BASE_URL, "");
        setServerEndpoint(lastKnownBaseUrl);
    }

    private static void setupWithChangelly() {
        OkHttpClient client = new OkHttpClient.Builder()
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
        Hawk.put(Constants.BASE_URL, serverEndpoint);

        SERVER_ENDPOINT = NODE_PREFIX + serverEndpoint + PLAYMARKET_BASE_URL;
        SERVER_ENDPOINT_WITHOUT_POST = NODE_PREFIX + serverEndpoint + PLAYMARKET_BASE_URL;
        BASE_URL = SERVER_ENDPOINT + "/api/";
        ICON_URL = SERVER_ENDPOINT + "/data/";
    }

    public static String getCheckUrlEndpointByNode(String nodeAddress) {
        String resultUrl = NODE_PREFIX + nodeAddress + PLAYMARKET_BASE_URL + "/api/";
        return resultUrl;
    }


    private ServerApi setupWithCustomUrl(String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .build();

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

    private static ServerApi setupWithXML() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .build();


        Gson gson = new GsonBuilder()
                .setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        xmlApi = retrofit.create(ServerApi.class);
        return xmlApi;
    }

}
