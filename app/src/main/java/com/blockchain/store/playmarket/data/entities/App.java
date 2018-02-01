package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.api.RestApi;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class App implements Parcelable {
    public String nameApp;
    public String hashTag;
    public String hash;
    @SerializedName("idCTG")
    public String catalogId;
    @SerializedName("idApp")
    public String appId;
    public String subCategory;
    public String value;
    @SerializedName("publish")
    public boolean isPublish;
    @SerializedName("free")
    public boolean isFree;
    @SerializedName("ico")
    public boolean isIco;
    public String icoUrl;

    public String getIconUrl() {
        return RestApi.ICON_URL + hashTag + "/" + hash + "/icon/icon.jpg";
    }

    public String getDownloadLink() {
        if (isFree) {
            return RestApi.ICON_URL + hashTag + "/" + hash + "/app/app.apk";
        } else {
            return "";
        }
    }

    public String getFileName() {
        return hash + ".apk";
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
        dest.writeString(this.value);
        dest.writeByte(this.isPublish ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFree ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIco ? (byte) 1 : (byte) 0);
        dest.writeString(this.icoUrl);
    }

    public App() {
    }

    protected App(Parcel in) {
        this.nameApp = in.readString();
        this.hashTag = in.readString();
        this.hash = in.readString();
        this.catalogId = in.readString();
        this.appId = in.readString();
        this.subCategory = in.readString();
        this.value = in.readString();
        this.isPublish = in.readByte() != 0;
        this.isFree = in.readByte() != 0;
        this.isIco = in.readByte() != 0;
        this.icoUrl = in.readString();
    }

    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {
        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };
}
