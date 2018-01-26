package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.api.RestApi;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class App {
    public String nameApp;
    public String hashTag;
    public String hash;
    @SerializedName("idCTG")
    public String catalogId;
    @SerializedName("idApp")
    public String appId;
    public String subCategory;
    public String value;
    @SerializedName("publish")
    public boolean isPublish;
    @SerializedName("free")
    public boolean isFree;
    @SerializedName("ico")
    public boolean isIco;
    public String icoUrl;

    public String getIconUrl() {
        return RestApi.ICON_URL + hashTag + "/" + hash + "/icon/icon.jpg";
    }
}
