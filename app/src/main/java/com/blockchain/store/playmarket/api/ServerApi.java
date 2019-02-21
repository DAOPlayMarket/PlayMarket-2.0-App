package com.blockchain.store.playmarket.api;

import com.blockchain.store.dao.data.IpfsData;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.CryptoPriceResponse;
import com.blockchain.store.playmarket.data.entities.ExchangeRate;
import com.blockchain.store.playmarket.data.entities.IcoInfoResponse;
import com.blockchain.store.playmarket.data.entities.IpfsConfigResponse;
import com.blockchain.store.playmarket.data.entities.IpfsPeersResponse;
import com.blockchain.store.playmarket.data.entities.IpfsPingResponse;
import com.blockchain.store.playmarket.data.entities.IpfsVersionResponse;
import com.blockchain.store.playmarket.data.entities.PexHistory;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.TokenResponse;
import com.blockchain.store.playmarket.data.entities.UserReview;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @Multipart
    @POST("https://ipfs.infura.io:5001/api/v0/add?")
    Observable<IpfsData> getFullDescriptionHash(@Part("file") String jsonData);

    @GET("https://ipfs.infura.io:5001/api/v0/cat?")
    Observable<String> getProposalDescriptions(@Query("arg") String multihash);

    @FormUrlEncoded
    @POST("deploy")
    Observable<PurchaseAppResponse> deployTransaction(@Field("signedTransactionData") String transactionData);

    @GET("get-gas-price")
    Observable<String> getGasPrice();

    @FormUrlEncoded

    @POST("get-address-balance")
    Observable<String> getBalance(@Field("address") String address);

    @FormUrlEncoded
    @POST("check-buy")
    Observable<Boolean> checkPurchase(@Field("idApp") String appId, @Field("address") String address);

    @FormUrlEncoded
    @POST("get-info-for-tx")
    Observable<AccountInfoResponse> getAccountInfo(@Field("address") String address); // todo peredelat`

    @FormUrlEncoded
    @POST("search")
    Observable<ArrayList<App>> getSearchResult(@Field("query") String query);

    @FormUrlEncoded
    @POST("get-apps-by-id")
    Observable<ArrayList<App>> getAppsById(@Field("idAppArr") String arrayOfInt);

    @FormUrlEncoded
    @POST("get-apps-by-package-name")
    Observable<ArrayList<App>> getAppsByPackage(@Field("packageNameArr") String arrayOfString);

    @FormUrlEncoded
    @POST("get-apps-by-package-name")
    Observable<ArrayList<AppInfo>> getAppsByPackageGetAsAppInfo(@Field("packageNameArr") String arrayOfString);

    @FormUrlEncoded()
    @POST("get-currency")
    Call<ExchangeRate> getExchangeRate(@Field("currency") String currency);

    @FormUrlEncoded()
    @POST("get-currency")
    Observable<ExchangeRate> getExchangeRateAsObservable(@Field("currency") String currency);

    @FormUrlEncoded()
    @POST("get-crypto-price")
    Observable<CryptoPriceResponse> getCryptoPrice(@Field("amount") String countOfPmcTokensWithDecimals);

    @FormUrlEncoded()
    @POST("get-reviews")
    Observable<ArrayList<UserReview>> getReviews(@Field("idApp") int appId);

    @FormUrlEncoded()
    @POST("get-ico-info")
    Observable<IcoInfoResponse> getIcoInfo(@Field("crowdsaleAddress") String addressICO, @Field("tokenAddress") String tokenAddress);

    @GET("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fmedium.com%2Ffeed%2F%40playmarket2.0&api_key=bxtxvdt126inutffjskughyxigwfdb1er03qhvsk")
    Observable<PlaymarketFeed> getPlaymarketNews();

    @GET("/")
    Observable<TokenResponse> getAllTokens();

    @GET("https://pex-balancer.playmarket.io/api/history?symbol=PMT&resolution=1D&from=1513577768&to=1544681828")
    Observable<PexHistory> getPexHistory();


    @FormUrlEncoded()
    @POST("https://m{node}.playmarket.io/api/app-install-event")
    Observable<ResponseBody> reportAppInstallEvent(@Path(value = "node") String node, @Field("idApp") String idApp, @Header("hash") String hash);

    @GET("http://127.0.0.1:5001/api/v0/ping")
    Observable<IpfsPingResponse> pingToIpfs(@Query("arg") String peerId);

    @GET("http://127.0.0.1:5001/api/v0/swarm/peers")
    Observable<IpfsPeersResponse> ipfsGetPeers();

    @GET("http://127.0.0.1:5001/api/v0/version")
    Observable<IpfsVersionResponse> ipfsGetVersion();

    @GET("http://127.0.0.1:5001/api/v0/config/show")
    Observable<IpfsConfigResponse> getPeersIdFromConfig();


//    @GET("https://pex-balancer.playmarket.io/api/history?symbol=PMT&resolution=1D&from=1513577768&to=1544681828")
//    Observable<ResponseBody> getPexHistory();


}
