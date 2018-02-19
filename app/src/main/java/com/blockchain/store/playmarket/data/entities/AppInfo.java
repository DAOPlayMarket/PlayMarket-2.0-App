package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class AppInfo implements Parcelable {
    @SerializedName("info")
    public App app;
    public String description;
    public PicturesResponse pictures;

    public String getImageUrl(String imageName) {
        try {
            return RestApi.ICON_URL + app.hashTag + "/" + app.hash + "/pictures/" + imageName;
        } catch (Exception e) {
            return "";
        }
    }

    public String getFormattedPrice(){
        return new EthereumPrice(app.price).inEther().toString();
    }

    public ArrayList<String> getImagePathList() {
        ArrayList<String> imagePathList = new ArrayList<>();
        for (String imageList : pictures.imageNameList) {
            imagePathList.add(getImageUrl(imageList));
        }
        return imagePathList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.app, flags);
        dest.writeString(this.description);
        dest.writeParcelable(this.pictures, flags);
    }

    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        this.app = in.readParcelable(App.class.getClassLoader());
        this.description = in.readString();
        this.pictures = in.readParcelable(PicturesResponse.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppInfo> CREATOR = new Parcelable.Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
