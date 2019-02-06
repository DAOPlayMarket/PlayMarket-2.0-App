package com.blockchain.store.playmarket.data.entities;

public class InstalledAppData {
    private String appId;
    private String node; // from what node app is downloaded

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
