package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

public class IcoLocalData {
    public ArrayList<String> price = new ArrayList<>();
    public String numberOfPeriods;
    public String durationOfPeriod;
    public String tokensInPeriod;
    public String startsAt;

    public int getCurrentPeriod() {
        return (int) Math.abs((System.currentTimeMillis() - Long.valueOf(startsAt) * 1000) / Integer.valueOf(durationOfPeriod));
    }

    public String getCompanyValue() {
        return String.valueOf(Double.valueOf(price.get(0)) * 100000);
    }
}
