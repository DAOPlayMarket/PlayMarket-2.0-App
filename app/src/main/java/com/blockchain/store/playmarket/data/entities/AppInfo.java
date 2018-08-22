package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class AppInfo implements Parcelable {
    private static final String TAG = "App";

    @SerializedName("idApp")
    public String appId;
    @SerializedName("adrICO")
    public String adrICO;
    @SerializedName("price")
    public String price;
    @SerializedName("free")
    public boolean isFree;
    public String adrDev;
    public String hashTag;
    public String hash;
    @SerializedName("longDescr")
    public String description;
    public String privacyPolicy;
    public String urlApp;
    public boolean isForChildren;
    @SerializedName("advertising")
    public boolean isAdverising;
    public String ageRestrictions;
    public String email;
    public String youtubeID;
    @SerializedName("shortDescr")
    public String shortDescription;
    public String slogan;
    public String subCategory;
    @SerializedName("idCTG")
    public String catalogId;
    public String nameApp;

    public AppFiles files;

    @SerializedName("publish")
    public boolean isPublish;
    @SerializedName("icoRelease")
    public boolean isIco;
    public String icoUrl;
    public String locale;
    public boolean pP;
    public String icoSymbol;
    public String icoName;
    public String icoDecimals;
    public ArrayList<IcoStages> icoStages = new ArrayList<>();
    public String icoTotalSupply;
    public String icoStartDate;
    public String icoEndDate;
    public String icoHardCapUsd;
    public String hashICO;
    public String hashTagICO;
    public String version;
    public String packageName;
    public IcoInfo infoICO = null;
    public CurrentInfo currentInfo;
    public Rating rating;
    public IcoBalance icoBalance;

    public String getRating() {
        try {
            double rating = ((double) this.rating.ratingSum / this.rating.ratingCount);
            rating = Math.round(rating * 10.0) / 10.0;
            return String.valueOf(rating);
        } catch (Exception e) {
            return "";
        }
    }


    public String getIconUrl() {
        try {
            String iconUrl = RestApi.ICON_URL + hashTag + "/" + hash + "/" + files.images.logo;
            Log.d(TAG, "Icon Url: " + iconUrl);
            return iconUrl;
        } catch (Exception e) {
            return "";
        }
    }

    public String getDownloadLink() {
        try {
            String downloadLink = RestApi.ICON_URL + hashTag + "/" + hash + "/" + files.apk;
            Log.d(TAG, "getDownloadLink: " + downloadLink);
            return downloadLink;
        } catch (Exception e) {
            return "";
        }
    }

    public ArrayList<String> getImages() {
        try {
            ArrayList<String> images = new ArrayList<>();
            for (String s : files.images.gallery) {
                images.add(RestApi.ICON_URL + hashTag + "/" + hash + "/" + s);
            }
            Log.d(TAG, "getImages: " + images);
            return images;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String getImageByPath(String path) {
        String url = RestApi.ICON_URL + hashTag + "/" + hashICO + "/" + path;
        return url;
    }

    public String getFileName() {
        return hash + ".apk";
    }

    public long getUnixTimeToFirstStageEnding() {
        long currentTimeUnix = System.currentTimeMillis() / 1000;
        for (IcoStages stage : icoStages) {
            if (currentTimeUnix > Long.parseLong(stage.startDate) && currentTimeUnix < Long.parseLong(stage.time)) {
                return Long.parseLong(stage.time) - currentTimeUnix;
            }
        }
        return 0;
    }
    public int getCurrentStage() {
        long currentTimeUnix = System.currentTimeMillis() / 1000;
        for (int i = 0; i < icoStages.size(); i++) {
            IcoStages stage = icoStages.get(i);
            if (currentTimeUnix > Long.parseLong(stage.startDate) && currentTimeUnix < Long.parseLong(stage.time)) {
                return i;
            }
        }
        return 0;
    }

    public AppInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.adrICO);
        dest.writeString(this.price);
        dest.writeByte(this.isFree ? (byte) 1 : (byte) 0);
        dest.writeString(this.adrDev);
        dest.writeString(this.hashTag);
        dest.writeString(this.hash);
        dest.writeString(this.description);
        dest.writeString(this.privacyPolicy);
        dest.writeString(this.urlApp);
        dest.writeByte(this.isForChildren ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAdverising ? (byte) 1 : (byte) 0);
        dest.writeString(this.ageRestrictions);
        dest.writeString(this.email);
        dest.writeString(this.youtubeID);
        dest.writeString(this.shortDescription);
        dest.writeString(this.slogan);
        dest.writeString(this.subCategory);
        dest.writeString(this.catalogId);
        dest.writeString(this.nameApp);
        dest.writeParcelable(this.files, flags);
        dest.writeByte(this.isPublish ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIco ? (byte) 1 : (byte) 0);
        dest.writeString(this.icoUrl);
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
        dest.writeString(this.version);
        dest.writeString(this.packageName);
        dest.writeParcelable(this.infoICO, flags);
        dest.writeParcelable(this.currentInfo, flags);
        dest.writeParcelable(this.rating, flags);
        dest.writeParcelable(this.icoBalance, flags);
    }

    protected AppInfo(Parcel in) {
        this.appId = in.readString();
        this.adrICO = in.readString();
        this.price = in.readString();
        this.isFree = in.readByte() != 0;
        this.adrDev = in.readString();
        this.hashTag = in.readString();
        this.hash = in.readString();
        this.description = in.readString();
        this.privacyPolicy = in.readString();
        this.urlApp = in.readString();
        this.isForChildren = in.readByte() != 0;
        this.isAdverising = in.readByte() != 0;
        this.ageRestrictions = in.readString();
        this.email = in.readString();
        this.youtubeID = in.readString();
        this.shortDescription = in.readString();
        this.slogan = in.readString();
        this.subCategory = in.readString();
        this.catalogId = in.readString();
        this.nameApp = in.readString();
        this.files = in.readParcelable(AppFiles.class.getClassLoader());
        this.isPublish = in.readByte() != 0;
        this.isIco = in.readByte() != 0;
        this.icoUrl = in.readString();
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
        this.version = in.readString();
        this.packageName = in.readString();
        this.infoICO = in.readParcelable(IcoInfo.class.getClassLoader());
        this.currentInfo = in.readParcelable(CurrentInfo.class.getClassLoader());
        this.rating = in.readParcelable(Rating.class.getClassLoader());
        this.icoBalance = in.readParcelable(IcoBalance.class.getClassLoader());
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public App convertToApp(AppInfo appInfo) {
        App app = new App();
        app.appId = appInfo.appId;
        app.adrICO = appInfo.adrICO;
        app.price = appInfo.price;
        app.isFree = appInfo.isFree;
        app.adrDev = appInfo.adrDev;
        app.hashTag = appInfo.hashTag;
        app.hash = appInfo.hash;
        app.description = appInfo.description;
        app.privacyPolicy = appInfo.privacyPolicy;
        app.urlApp = appInfo.urlApp;
        app.isForChildren = appInfo.isForChildren;
        app.isAdverising = appInfo.isAdverising;
        app.ageRestrictions = appInfo.ageRestrictions;
        app.email = appInfo.email;
        app.youtubeID = appInfo.youtubeID;
        app.shortDescription = appInfo.shortDescription;
        app.slogan = appInfo.slogan;
        app.subCategory = appInfo.subCategory;
        app.catalogId = appInfo.catalogId;
        app.nameApp = appInfo.nameApp;
        app.files = appInfo.files;
        app.isPublish = appInfo.isPublish;
        app.isIco = appInfo.isIco;
        app.icoUrl = appInfo.icoUrl;
        app.locale = appInfo.locale;
        app.pP = appInfo.pP;
        app.icoSymbol = appInfo.icoSymbol;
        app.icoName = appInfo.icoName;
        app.icoDecimals = appInfo.icoDecimals;
        app.icoStages = appInfo.icoStages;
        app.icoTotalSupply = appInfo.icoTotalSupply;
        app.icoStartDate = appInfo.icoStartDate;
        app.icoEndDate = appInfo.icoEndDate;
        app.icoHardCapUsd = appInfo.icoHardCapUsd;
        app.hashICO = appInfo.hashICO;
        app.hashTagICO = appInfo.hashTagICO;
        app.version = appInfo.version;
        app.packageName = appInfo.packageName;
        app.infoICO = appInfo.infoICO;
        app.rating = appInfo.rating;
        app.icoBalance = appInfo.icoBalance;
        return app;
    }
}
