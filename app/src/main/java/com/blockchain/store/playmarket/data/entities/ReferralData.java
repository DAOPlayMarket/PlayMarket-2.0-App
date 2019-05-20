package com.blockchain.store.playmarket.data.entities;

public class ReferralData {
    public String packageName = "";
    public String referralCode = "";
    public String referralName = "";

    public ReferralData(String packageName, String referralCode) {
        this.packageName = packageName;
        this.referralCode = referralCode;
    }
}
