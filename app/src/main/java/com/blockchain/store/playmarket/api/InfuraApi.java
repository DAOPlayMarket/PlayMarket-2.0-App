package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.BaseInfuraResponse;
import com.blockchain.store.playmarket.data.entities.InfuraBaseBody;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public interface InfuraApi {
    @POST("./")
    Observable<BaseInfuraResponse> getUserBalance(@Body InfuraBaseBody body);

    @POST("./")
    Observable<BaseInfuraResponse> getTransactionCount(@Body InfuraBaseBody body);

    @POST("./")
    Observable<BaseInfuraResponse> getGasPrice(@Body InfuraBaseBody body);
}
