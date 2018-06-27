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
    public IcoInfo infoICO = null;

    public String getImageUrl(String imageName) {
        try {
            return RestApi.ICON_URL + app.hashTag + "/" + app.hash + "/pictures/" + imageName;
        } catch (Exception e) {
            return "";
        }
    }

    public String getIcoAdvisorsUrl(String imageName) {
        return RestApi.ICON_URL + app.hashTagICO + "/" + app.hashICO + "/images/advisors/" + imageName;
    }

    public String getIcoTeamUrl(String imageName) {
        return RestApi.ICON_URL + app.hashTagICO + "/" + app.hashICO + "/images/team/" + imageName;
    }

    public ArrayList<String> getIcoScreenShotsUrl() {
        ArrayList<String> images = new ArrayList<>();
        for (String picture : infoICO.pictures) {
            images.add(RestApi.ICON_URL + app.hashTagICO + "/" + app.hashICO + "/images/pictures/" + picture);
        }

        return images;
    }

    public String getIcoIcon() {
        return RestApi.ICON_URL + app.hashTagICO + "/" + app.hashICO + "/images/icon/" + infoICO.iconUrl;
    }

    public String getFormattedPrice() {
        return new EthereumPrice(app.price).inEther().toString();
    }

    public ArrayList<String> getImagePathList() {
        ArrayList<String> imagePathList = new ArrayList<>();
        for (String imageList : pictures.imageNameList) {
            imagePathList.add(getImageUrl(imageList));
        }
        return imagePathList;
    }

    public AppInfo() {
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
        dest.writeParcelable(this.infoICO, flags);
    }

    protected AppInfo(Parcel in) {
        this.app = in.readParcelable(App.class.getClassLoader());
        this.description = in.readString();
        this.pictures = in.readParcelable(PicturesResponse.class.getClassLoader());
        this.infoICO = in.readParcelable(IcoInfo.class.getClassLoader());
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
}
