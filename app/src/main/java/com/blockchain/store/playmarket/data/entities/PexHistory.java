package com.blockchain.store.playmarket.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PexHistory {
    @SerializedName("t")
    public ArrayList<Float> timeArray;
    @SerializedName("o")
    public ArrayList<Float> openArray;
    @SerializedName("h")
    public ArrayList<Float> highArray;
    @SerializedName("l")
    public ArrayList<Float> lowArray;
    @SerializedName("c")
    public ArrayList<Float> closeArray;
    @SerializedName("v")
    public ArrayList<Float> valueArray; // used for another chart
    @SerializedName("s")
    public String serverMessage;
}
