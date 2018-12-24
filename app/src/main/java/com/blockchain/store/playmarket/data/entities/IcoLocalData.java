package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.data.types.EthereumPrice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class IcoLocalData implements Parcelable {
    private static final String TAG = "IcoLocalData";
    private static final int tokensDecimals = 1_000_000_00;
    public ArrayList<String> price = new ArrayList<>();
    public ArrayList<String> earnedInPeriod = new ArrayList<>();
    public ArrayList<String> tokenEarnedInPeriod = new ArrayList<>();
    public String numberOfPeriods;
    public String durationOfPeriod;
    public String tokensInPeriod;
    public String startsAt;
    public BigInteger adverBudget;

    public double getAdverBudget() {
        return new EthereumPrice(adverBudget.toString()).inEther().doubleValue();
    }

    public int getCurrentPeriod() {
        long currentPeriod = Math.abs((System.currentTimeMillis() - getStartsAt()) / getDurationOfPeriod());
        return (int) currentPeriod;
    }

    public String getCompanyValue() {
        try {
            return new EthereumPrice(String.valueOf(Long.valueOf(price.get(getCurrentPeriod())) * 100_000)).inEther().toPlainString();
        } catch (Exception e) {
            return "0";
        }
    }

    public double getPrice(int position) {
        return getEarnedInPeriod(position) * 6 / 100_000;
    }

    public double getEarnedInPeriod(int period) {
        try {
            double longValue = new EthereumPrice(earnedInPeriod.get(period)).inEther().doubleValue();
            return longValue;
        } catch (Exception e) {
            return 0;
        }
    }

    public long getTokensEarnedInPeriod(int position) {
        try {
            return Long.parseLong(tokenEarnedInPeriod.get(position)) / tokensDecimals;
        } catch (Exception e) {
            return 0;
        }
    }

    public long getTimeToStartStage(int position) {
        long returnValue;
        if (position == 0) {
            returnValue = Math.abs(System.currentTimeMillis() - (getStartsAt() + getDurationOfPeriod()));
        } else if (position == getNumberOfPeriods()) {
            returnValue = Math.abs(System.currentTimeMillis() - (getStartsAt() + getDurationOfPeriod() * getNumberOfPeriods()));
        } else {
            returnValue = Math.abs(System.currentTimeMillis() - (getStartsAt() + getDurationOfPeriod() * position));
        }
        return returnValue;
    }

    public int getNumberOfPeriods() {
        return Integer.valueOf(numberOfPeriods);
    }

    public Long getDurationOfPeriod() {
        return Long.valueOf(durationOfPeriod) * 1000;
    }

    public Long getTokensInPeriod() {
        return Long.valueOf(tokensInPeriod) / tokensDecimals;
    }

    public Long getStartsAt() {
        return Long.valueOf(startsAt) * 1000;
    }

    public IcoLocalData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.price);
        dest.writeStringList(this.earnedInPeriod);
        dest.writeStringList(this.tokenEarnedInPeriod);
        dest.writeString(this.numberOfPeriods);
        dest.writeString(this.durationOfPeriod);
        dest.writeString(this.tokensInPeriod);
        dest.writeString(this.startsAt);
    }

    protected IcoLocalData(Parcel in) {
        this.price = in.createStringArrayList();
        this.earnedInPeriod = in.createStringArrayList();
        this.tokenEarnedInPeriod = in.createStringArrayList();
        this.numberOfPeriods = in.readString();
        this.durationOfPeriod = in.readString();
        this.tokensInPeriod = in.readString();
        this.startsAt = in.readString();
    }

    public static final Creator<IcoLocalData> CREATOR = new Creator<IcoLocalData>() {
        @Override
        public IcoLocalData createFromParcel(Parcel source) {
            return new IcoLocalData(source);
        }

        @Override
        public IcoLocalData[] newArray(int size) {
            return new IcoLocalData[size];
        }
    };
}

