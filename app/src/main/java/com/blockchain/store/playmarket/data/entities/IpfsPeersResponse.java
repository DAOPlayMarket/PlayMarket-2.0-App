package com.blockchain.store.playmarket.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IpfsPeersResponse {
    @SerializedName("Peers")
    public ArrayList<IpfsPeer> peers;
}
