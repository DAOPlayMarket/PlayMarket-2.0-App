package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.api.RestApi;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class AppInfo {
    public App app;
    public String description;
    public PicturesResponse pictures;

    public String getThumbnailUrl() {
        try {
            String thumbnail = pictures.thumbnail;
            return RestApi.ICON_URL + app.hashTag + "/" + app.hash + "/pictures/" + thumbnail;
        } catch (Exception e) {
            return "";
        }

    }
}
