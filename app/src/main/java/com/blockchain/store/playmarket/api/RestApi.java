package com.blockchain.store.playmarket.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Crypton04 on 25.01.2018.
 */

class RestApi {
    private static final String TAG = "RestApi";
    private static final String BASE_URL = "http://example/";
    private static Api api;

    public static Api instance() {
        if (api == null) {
            setup();
        }
        return api;
    }

    private static void setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

}
