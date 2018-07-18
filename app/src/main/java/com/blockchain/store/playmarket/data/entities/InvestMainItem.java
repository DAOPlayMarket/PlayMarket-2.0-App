package com.blockchain.store.playmarket.data.entities;

public class InvestMainItem {
    public String name;
    public String description;
    public String earnedMin;
    public String earnedMax;
    public int stageCurrent;
    public int stageMax;
    public long totalTime;
    public String devAddr;
    public String adrIco;
    public String iconUrl;
    public String icoSymbol;

    public InvestMainItem(String name, String description, String earnedMin, String earnedMax, int stageCurrent, int stageMax, long totalTime, String devAddr, String adrIco, String iconUrl, String icoSymbol) {
        this.name = name;
        this.description = description;
        this.earnedMin = earnedMin;
        this.earnedMax = earnedMax;
        this.stageCurrent = stageCurrent;
        this.stageMax = stageMax;
        this.totalTime = totalTime;
        this.devAddr = devAddr;
        this.adrIco = adrIco;
        this.iconUrl = iconUrl;
        this.icoSymbol = icoSymbol;
    }





}
