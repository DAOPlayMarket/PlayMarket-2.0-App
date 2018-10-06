package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CryptoPriceReponse;
import com.blockchain.store.playmarket.data.entities.CurrentInfo;
import com.blockchain.store.playmarket.data.entities.ExchangeRate;
import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.InvestAddressResponse;
import com.blockchain.store.playmarket.data.entities.NonceResponce;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.TokenResponse;
import com.blockchain.store.playmarket.data.entities.UserReview;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public interface ServerApi {
    @GET("get-categories")
    Observable<ArrayList<Category>> getCagories();

    @GET("get-apps-ico")
    Observable<ArrayList<AppInfo>> getIcoApps();

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
    @POST("deploy")
    Observable<PurchaseAppResponse> deployTransaction(@Field("signedTransactionData") String transactionData);

    @FormUrlEncoded
    @POST("check-buy")
    Observable<Boolean> checkPurchase(@Field("idApp") String appId, @Field("address") String address);

    @GET("get-gas-price")
    Observable<String> getGasPrice();

    @FormUrlEncoded
    @POST("get-address-balance")
    Observable<String> getBalance(@Field("address") String address);

    @FormUrlEncoded
    @POST("get-info-for-tx")
    Observable<AccountInfoResponse> getAccountInfo(@Field("address") String address);

    @FormUrlEncoded
    @POST("search")
    Observable<ArrayList<App>> getSearchResult(@Field("query") String query);

    @FormUrlEncoded
    @POST("get-apps-by-id")
    Observable<ArrayList<App>> getAppsById(@Field("idAppArr") String arrayOfInt);

    @FormUrlEncoded
    @POST("get-apps-by-package-name")
    Observable<ArrayList<App>> getAppsByPackage(@Field("packageNameArr") String arrayOfString);

//    @FormUrlEncoded()
//    @POST("get-exchange-rate")
//    Call<ExchangeRate> getExchangeRate(@Field("currency") String currency);

    @FormUrlEncoded()
    @POST("192.168.88.244:3003/api/get-currency")
    Call<ExchangeRate> getExchangeRate(@Field("currency") String currency);

    @FormUrlEncoded()
    @POST("192.168.88.244:3003/api/get-crypto-price")
    Call<CryptoPriceReponse> getCryptoPrice(@Field("amount") String countOfPmcTokensWithDecimals);

    @FormUrlEncoded()
    @POST("get-reviews")
    Observable<ArrayList<UserReview>> getReviews(@Field("idApp") int appId);

    @FormUrlEncoded()
    @POST("get-balanceOf")
    Observable<ArrayList<IcoBalance>> getBalanceOf(@Field("addressPull") String arrayAddress, @Field("addressUser") String addressUser);

    @FormUrlEncoded()
    @POST("get-current-info")
    Observable<CurrentInfo> getCurrentInfo(@Field("address") String addressICO);

    @GET("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fmedium.com%2Ffeed%2F%40playmarket2.0&api_key=bxtxvdt126inutffjskughyxigwfdb1er03qhvsk")
    Observable<PlaymarketFeed> getPlaymarketNews();

    @GET("https://tokens.playmarket.io/")
    Observable<TokenResponse> getAllTokens();

    @GET("https://tokens.playmarket.io/")
    Observable<TokenResponse> getCustomToken(@Query("contract") String contract);
}
