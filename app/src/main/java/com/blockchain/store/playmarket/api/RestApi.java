package com.blockchain.store.playmarket.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class RestApi {
    private static final String SERVER_ENDPOINT = "http://192.168.11.186:3000";
    private static final String SERVER_ENDPOINT_old = "http://31.211.80.204:3000";
    public static final String BASE_URL = SERVER_ENDPOINT + "/api/";
    public static final String ICON_URL = SERVER_ENDPOINT + "/data/";
    public static final String BASE_URL_INFURA = "https://rinkeby.infura.io/iYGysj5Sns7HV42MdiXi/";

    private static final String TAG = "RestApi";

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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
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
                .build();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                // try the request
//                Response response = chain.proceed(request);
//                if (!response.isSuccessful()) {
//                    Log.d(TAG, "intercept: request not successfull " + response.message());
//
//                }
//                return response;
//            }
//        });
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

}
