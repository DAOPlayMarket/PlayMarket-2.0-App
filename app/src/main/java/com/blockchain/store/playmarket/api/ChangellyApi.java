package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.ChangellyCurrenciesResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public interface ChangellyApi {
    @POST("./")
    Observable<ChangellyCurrenciesResponse> getCurrenciesFull(@Body ChangellyBaseBody body);

    @POST("./")
    Observable<ChangellyMinimumAmountResponse> getMinimumAmount(@Body ChangellyBaseBody body);

    @POST("./")
    Observable<ChangellyCurrenciesResponse> getExchangeAmount(@Body ChangellyBaseBody body);
}
