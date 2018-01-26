package com.blockchain.store.playmarket.api;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.Category;

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

public interface Api {
    @GET("get-categories")
    Observable<ArrayList<Category>> getCagories();

    @FormUrlEncoded
    @POST("get-app")
    Observable<AppInfo> getAppInfo(@Field("idCTG") String categoryId, @Field("idApp") String appId);

    @FormUrlEncoded
    @GET("get-categories")
    Observable<ArrayList<App>> getApps(@Field("idCTG") String categoryId,
                                       @Field("skip") int skip,
                                       @Field("count") int count,
                                       @Field("subCategory") int subCategoryId,
                                       @Field("newFirst") boolean isNewFirst);

}
