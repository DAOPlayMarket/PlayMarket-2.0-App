package com.blockchain.store.playmarket.data.entities;

public class DaoToken {
    public String address;
    public long decimals;
    public long total;  // Total number of tokens transferred to PMFund

    public String name;
    public String symbol;

    public String fund;
    public String withdraw;
    public String daoBalance;


    public long getDaoBalance(){
        return Long.valueOf(daoBalance);
    }

    public long getWithdraw(){
        return Long.valueOf(withdraw);
    }
    public long getFund(){
        return Long.valueOf(fund);
    }

}
