package com.blockchain.store.playmarket.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

class IpfsPeer {
    public String addr;
    public String Peer;
    public String Latency;
    public String Muxer;
    public int Direction;
    @SerializedName("Streams")
    public ArrayList<HashMap<String, String>> streams;
}
