package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.api.RestApi;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class AppInfo {
    @SerializedName("info")
    public App app;
    public String description;
    public PicturesResponse pictures;

    public String getImageUrl(String imageName) {
        try {
            return RestApi.ICON_URL + app.hashTag + "/" + app.hash + "/pictures/" + imageName;
        } catch (Exception e) {
            return "";
        }
    }

    public ArrayList<String> getImagePathList() {
        ArrayList<String> imagePathList = new ArrayList<>();
        for (String imageList : pictures.imageNameList) {
            imagePathList.add(getImageUrl(imageList));
        }
        return imagePathList;
    }
}
