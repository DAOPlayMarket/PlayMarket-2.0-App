package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.BaseInfuraResponse;
import com.blockchain.store.playmarket.data.entities.InfuraUserBalanceBody;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public interface InfuraApi {
    @POST("./")
    Observable<BaseInfuraResponse> getUserBalance(@Body InfuraUserBalanceBody body);
}
