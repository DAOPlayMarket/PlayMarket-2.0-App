package com.blockchain.store.playmarket.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TokenResponse {
    public boolean success;
    @SerializedName("data")
    public ArrayList<Token> tokens;
}
