package com.blockchain.store.playmarket.data.entities;

import android.location.Location;

public class Node {
    public String address;
    public Location location;

    public Node(String address, Location location) {
        this.address = address;
        this.location = location;
    }

    public Node() {
    }
}
