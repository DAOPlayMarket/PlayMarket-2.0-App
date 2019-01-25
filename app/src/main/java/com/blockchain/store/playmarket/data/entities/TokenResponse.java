package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TokenResponse {
    public boolean success;
    @SerializedName("data")
    public ArrayList<DaoToken> tokens;
}
