package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.utilities.Constants;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class App implements Parcelable, NotificationImpl {
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
            iconUrl = iconUrl.replaceAll("\\\\", "/");
            return iconUrl;
        } catch (Exception e) {
            return "";
        }
    }

    public String getDownloadLink() {
        try {
            String downloadLink = RestApi.ICON_URL + hashTag + "/" + hash + "/" + files.apk;
            downloadLink = downloadLink.replaceAll("\\\\", "/");
            return downloadLink;
        } catch (Exception e) {
            return "";
        }
    }

    public ArrayList<String> getImages() {
        try {
            ArrayList<String> images = new ArrayList<>();
            for (String s : files.images.gallery) {
                s = s.replaceAll("\\\\", "/");
                images.add(RestApi.ICON_URL + hashTag + "/" + hash + "/" + s);
            }
            Log.d(TAG, "getImages: " + images);
            return images;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

//    public String getPrice() {
//        Long priceInteger = Long.parseLong(price);
//        ExchangeRate exchangeRate = Hawk.get(Constants.CURRENT_CURRENCY);
//        double totalPrice = priceInteger * Double.parseDouble(exchangeRate.rate);
//        return String.valueOf(totalPrice);
//    }


    public String getPrice() {
        String priceInEther = new EthereumPrice(price).inEther().toString();
        return priceInEther;
    }

    public String getImageByPath(String path) {
        String url = RestApi.ICON_URL + hashTag + "/" + hash + "/";
        return url;
    }

    public String getFileName() {
        return packageName + ".apk";
    }


    public App() {
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
        dest.writeParcelable(this.rating, flags);
        dest.writeParcelable(this.icoBalance, flags);
    }

    protected App(Parcel in) {
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
        this.rating = in.readParcelable(Rating.class.getClassLoader());
        this.icoBalance = in.readParcelable(IcoBalance.class.getClassLoader());
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

    @Override
    public int getId() {
        try {
            return Integer.parseInt(appId);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String getTitleName() {
        return nameApp;
    }
}
