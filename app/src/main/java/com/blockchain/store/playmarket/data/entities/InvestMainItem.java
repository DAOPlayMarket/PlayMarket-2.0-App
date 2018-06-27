package com.blockchain.store.playmarket.data.entities;

public class InvestMainItem {
    public String name;
    public String description;
    public String earnedMin;
    public String earnedMax;
    public int stageCurrent;
    public int stageMax;
    public String totalTime;
    public String devAddr;
    public String iconUrl;

    public InvestMainItem(String name, String description, String earnedMin, String earnedMax, int stageCurrent, int stageMax, String totalTime, String devAddr, String iconUrl) {
        this.name = name;
        this.description = description;
        this.earnedMin = earnedMin;
        this.earnedMax = earnedMax;
        this.stageCurrent = stageCurrent;
        this.stageMax = stageMax;
        this.totalTime = totalTime;
        this.devAddr = devAddr;
        this.iconUrl = iconUrl;
    }





}
