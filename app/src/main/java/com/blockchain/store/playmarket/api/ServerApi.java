package com.blockchain.store.playmarket.api;

import android.util.Pair;

import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.InvestAddressResponse;
import com.blockchain.store.playmarket.data.entities.Node;
import com.blockchain.store.playmarket.data.entities.NonceResponce;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import okhttp3.ResponseBody;
import retrofit2.Call;
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

    @GET("get-apps-ico")
    Observable<ArrayList<App>> getIcoApps();

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
    @POST("transfer")
    Observable<PurchaseAppResponse> transferTheAmount(@Field("signedTransactionData") String transactionData);

    @FormUrlEncoded
    @POST("deploy")
    Observable<PurchaseAppResponse> deployTransaction(@Field("signedTransactionData") String transactionData, @Field("type") String transactionType);

    @FormUrlEncoded
    @POST("check-buy")
    Observable<Boolean> checkPurchase(@Field("idApp") String appId, @Field("address") String address);

    @FormUrlEncoded
    @GET("get-invest-address")
    Observable<InvestAddressResponse> getInvestAddress();

    @GET("get-gas-price")
    Observable<String> getGasPrice();

    @FormUrlEncoded
    @POST("get-address-balance")
    Observable<String> getBalance(@Field("address") String address);

    @FormUrlEncoded
    @POST("get-transaction-count")
    Observable<NonceResponce> getNonce(@Field("address") String address);

    @FormUrlEncoded
    @POST("get-info-for-tx")
    Observable<AccountInfoResponse> getAccountInfo(@Field("address") String address);

    @FormUrlEncoded
    @POST("search")
    Observable<ArrayList<App>> getSearchResult(@Field("query") String query);

    @GET("api/availability")
    Call<ResponseBody> checkAvailability();
}
