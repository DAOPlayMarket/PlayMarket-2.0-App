package com.blockchain.store.playmarket.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public interface Api {
    @GET()
    Observable<ResponseBody> hello (@Url String url);
}
