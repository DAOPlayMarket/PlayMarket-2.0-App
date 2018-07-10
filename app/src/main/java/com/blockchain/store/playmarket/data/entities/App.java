package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.api.RestApi;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class App implements Parcelable {
    @SerializedName("idApp")
    public String appId;
    @SerializedName("value")
    public String price;

    public String nameApp;
    public String hashTag;
    public String hash;
    @SerializedName("idCTG")
    public String catalogId;
    public String subCategory;
    @SerializedName("publish")
    public boolean isPublish;
    @SerializedName("free")
    public boolean isFree;
    @SerializedName("icoRelease")
    public boolean isIco;
    public String icoUrl;
    public String adrDev;

    public String email;
    public String ageRestrictions;
    public String urlApp;
    public String privacyPolicy;
    public String locale;
    public boolean pP;
    public String icoSymbol;
    public String icoName;
    public String icoDecimals;
    public ArrayList<IcoStages> icoStages;
    public String icoTotalSupply;
    public String icoStartDate;
    public String icoEndDate;
    public String icoHardCapUsd;
    public String hashICO;
    public String hashTagICO;

    //total supply / 10^(ico decimals)
    // tokenSold
    public String getIconUrl() {
        return RestApi.ICON_URL + hashTag + "/" + hash + "/icon/icon.jpg";
    }

    public String getDownloadLink() {
        return RestApi.ICON_URL + hashTag + "/" + hash + "/app/app.apk";
    }

    public String getFileName() {
        return hash + ".apk";
    }


    public App() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameApp);
        dest.writeString(this.hashTag);
        dest.writeString(this.hash);
        dest.writeString(this.catalogId);
        dest.writeString(this.appId);
        dest.writeString(this.subCategory);
        dest.writeString(this.price);
        dest.writeByte(this.isPublish ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFree ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIco ? (byte) 1 : (byte) 0);
        dest.writeString(this.icoUrl);
        dest.writeString(this.adrDev);
        dest.writeString(this.email);
        dest.writeString(this.ageRestrictions);
        dest.writeString(this.urlApp);
        dest.writeString(this.privacyPolicy);
        dest.writeString(this.locale);
        dest.writeByte(this.pP ? (byte) 1 : (byte) 0);
        dest.writeString(this.icoSymbol);
        dest.writeString(this.icoName);
        dest.writeString(this.icoDecimals);
        dest.writeTypedList(this.icoStages);
        dest.writeString(this.icoTotalSupply);
        dest.writeString(this.icoStartDate);
        dest.writeString(this.icoEndDate);
        dest.writeString(this.icoHardCapUsd);
        dest.writeString(this.hashICO);
        dest.writeString(this.hashTagICO);
    }

    protected App(Parcel in) {
        this.nameApp = in.readString();
        this.hashTag = in.readString();
        this.hash = in.readString();
        this.catalogId = in.readString();
        this.appId = in.readString();
        this.subCategory = in.readString();
        this.price = in.readString();
        this.isPublish = in.readByte() != 0;
        this.isFree = in.readByte() != 0;
        this.isIco = in.readByte() != 0;
        this.icoUrl = in.readString();
        this.adrDev = in.readString();
        this.email = in.readString();
        this.ageRestrictions = in.readString();
        this.urlApp = in.readString();
        this.privacyPolicy = in.readString();
        this.locale = in.readString();
        this.pP = in.readByte() != 0;
        this.icoSymbol = in.readString();
        this.icoName = in.readString();
        this.icoDecimals = in.readString();
        this.icoStages = in.createTypedArrayList(IcoStages.CREATOR);
        this.icoTotalSupply = in.readString();
        this.icoStartDate = in.readString();
        this.icoEndDate = in.readString();
        this.icoHardCapUsd = in.readString();
        this.hashICO = in.readString();
        this.hashTagICO = in.readString();
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

//    public long getStageTimeRemaining(){
//        long totalTimeInUnix = Long.parseLong(icoStages.get(Integer.parseInt(currentStage) - 1).time);
//        long totalTimeFromUnix = ((totalTimeInUnix*1000)-System.currentTimeMillis());
//        return totalTimeFromUnix;
//    }
}
