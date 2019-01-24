package com.blockchain.store.dao.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DaoToken implements Parcelable {
    @SerializedName("contract")
    public String address;
    public long decimals;
    public long total;  // Total number of tokens transferred to PMFund

    public String name;
    public String symbol;

    public boolean isWithdrawBlocked = false;

    public String fund = "0";
    public String withdraw = "0";
    public String balance = "0";
    public String daoBalance = "0";
    public String daoNotLockedBalance = "0";
    public String approval = "0";

    public int totalTokensLength = 0;
    public int tokenPositionInArray = 0;

    private long multiplier = 100_000L;
    private long TotalPMT = 30000000000L;

    public long getDaoBalance() {
        return Long.valueOf(daoBalance);
    }

    public long getNotLockedBalance() {
        return Long.valueOf(daoNotLockedBalance);
    }

    public String getNotLockedBalanceWithDecimals() {
        return String.valueOf(getNotLockedBalance() / Math.pow(10, decimals));
    }

    public Long getApprovalWithoutDecimal(){
        return (long)(Long.valueOf(approval));
    }
    public Long getApprovalWithDecimals() {
        return (long)(Double.parseDouble(approval) / Math.pow(10,decimals));
    }

    public long getWithdraw() {
        return Long.valueOf(withdraw);
    }

    public long getFund() {
        return Long.valueOf(fund);
    }

    public long countToken() {
        long value = 0;
        long balance = getDaoBalance();
        value = balance - getWithdraw();
        if (value > 0) {
            value = value * multiplier;
            value = value * 100 / TotalPMT;
            value = safePerc(total, value);
            value = value / (multiplier / 100);
        }

        value += getFund();

        return value;
    }

    public String getTokenCountWithDecimals() {
        return String.valueOf(countToken() / Math.pow(10, decimals));
    }

    public boolean isNeedSecondTx() {
        return countToken() != getFund();
    }

    public long totalCountToken() {
        return getFund() + countToken();
    }

    private long safePerc(long x, long y) {
        if (x == 0) {
            return 0;
        }
        long z = x * y;
        z = z / 10_000;
        return z;
    }

    public DaoToken generatePmToken() {
        DaoToken daoToken = new DaoToken();
        daoToken.name = "DAO PlayMarket 2.0";
        daoToken.symbol = "PMT";
        daoToken.address = "0xcf42d66172f8fdec2b42515f0f0289049e4011c2";
//        daoToken.address = "0xc1322d8ae3b0e2e437e0ae36388d0cfd2c02f1c9";
        daoToken.decimals = 4;
        return daoToken;
    }

    public double getBalanceWithDecimals() {
        if (Long.valueOf(balance) == 0) {
            return 0;
        } else {
            return (double) (Double.valueOf(balance) / Math.pow(10, decimals));
        }
    }

    public double getDaoBalanceWithDecimals() {
        if (Long.valueOf(daoBalance) == 0) {
            return 0;
        } else {
            return (double) (Double.valueOf(daoBalance) / Math.pow(10, decimals));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeLong(this.decimals);
        dest.writeLong(this.total);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.fund);
        dest.writeString(this.withdraw);
        dest.writeString(this.balance);
        dest.writeString(this.daoBalance);
        dest.writeInt(this.totalTokensLength);
        dest.writeInt(this.tokenPositionInArray);
        dest.writeLong(this.multiplier);
        dest.writeLong(this.TotalPMT);
    }

    public DaoToken() {
    }

    protected DaoToken(Parcel in) {
        this.address = in.readString();
        this.decimals = in.readLong();
        this.total = in.readLong();
        this.name = in.readString();
        this.symbol = in.readString();
        this.fund = in.readString();
        this.withdraw = in.readString();
        this.balance = in.readString();
        this.daoBalance = in.readString();
        this.totalTokensLength = in.readInt();
        this.tokenPositionInArray = in.readInt();
        this.multiplier = in.readLong();
        this.TotalPMT = in.readLong();
    }

    public static final Parcelable.Creator<DaoToken> CREATOR = new Parcelable.Creator<DaoToken>() {
        @Override
        public DaoToken createFromParcel(Parcel source) {
            return new DaoToken(source);
        }

        @Override
        public DaoToken[] newArray(int size) {
            return new DaoToken[size];
        }
    };
}
