package com.blockchain.store.playmarket.data.entities;

public class InvestMainItem {
    public String name;
    public String description;
    public String soldTokens;
    public String icoTotalSupply;
    public int stageCurrent;
    public int stageMax;
    public long totalTime;
    public String devAddr;
    public String adrIco;
    public String iconUrl;
    public String icoSymbol;
    public boolean isIcoAlreadyStarted;

    public InvestMainItem(String name, String description, String soldTokens, String icoTotalSupply, int stageCurrent, int stageMax, long totalTime, String devAddr, String adrIco, String iconUrl, String icoSymbol, boolean isIcoAlreadyStarted) {
        this.name = name;
        this.description = description;
        this.soldTokens = soldTokens;
        this.icoTotalSupply = icoTotalSupply;
        this.stageCurrent = stageCurrent;
        this.stageMax = stageMax;
        this.totalTime = totalTime;
        this.devAddr = devAddr;
        this.adrIco = adrIco;
        this.iconUrl = iconUrl;
        this.icoSymbol = icoSymbol;
        this.isIcoAlreadyStarted = isIcoAlreadyStarted;
    }


}
