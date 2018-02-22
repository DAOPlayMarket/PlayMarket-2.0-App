package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.BalanceResponse;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.CheckPurchaseResponse;
import com.blockchain.store.playmarket.data.entities.GasPriceResponse;
import com.blockchain.store.playmarket.data.entities.InvestAddressResponse;
import com.blockchain.store.playmarket.data.entities.NonceResponce;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SearchResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public interface ServerApi {
    @GET("get-categories")
    Observable<ArrayList<Category>> getCagories();

    @FormUrlEncoded
    @POST("get-app")
    Observable<AppInfo> getAppInfo(@Field("idCTG") String categoryId, @Field("idApp") String appId);

    @FormUrlEncoded
    @POST("get-apps")
    Observable<ArrayList<App>> getApps(@Field("idCTG") String categoryId,
                                       @Field("skip") int skip,
                                       @Field("count") int count,
                                       @Field("subCategory") int subCategoryId,
                                       @Field("newFirst") boolean isNewFirst);

    @FormUrlEncoded
    @POST("buy-app")
    Observable<PurchaseAppResponse> purchaseApp(@Field("signedTransactionData") String transactionData);

    @FormUrlEncoded
    @POST("invest")
    Observable<PurchaseAppResponse> investApp(@Field("signedTransactionData") String transactionData);

    @FormUrlEncoded
    @POST("check-buy")
    Observable<CheckPurchaseResponse> checkPurchase(@Field("idApp") String appId, @Field("address") String address);

    @FormUrlEncoded
    @GET("get-invest-address")
    Observable<InvestAddressResponse> getInvestAddress();

    @FormUrlEncoded
    @POST("get-address-balance")
    Observable<BalanceResponse> getBalance(@Field("address") String address);

    @FormUrlEncoded
    @POST("get-transaction-count")
    Observable<NonceResponce> getNonce(@Field("address") String address);

    @FormUrlEncoded
    @POST("get-address-info")
    Observable<AccountInfoResponse> getAccountInfo(@Field("address") String address);

    @FormUrlEncoded
    @POST("search")
    Observable<ArrayList<App>> getSearchResult(@Field("query") String query);
}
