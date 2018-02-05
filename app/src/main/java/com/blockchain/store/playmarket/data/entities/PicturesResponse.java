package com.blockchain.store.playmarket.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class PicturesResponse {
    @SerializedName("gallery")
    public ArrayList<String> imageNameList;
    public String thumbnail;

}
