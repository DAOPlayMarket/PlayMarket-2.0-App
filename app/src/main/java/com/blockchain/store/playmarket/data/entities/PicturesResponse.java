package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class PicturesResponse implements Parcelable {
    @SerializedName("gallery")
    public ArrayList<String> imageNameList;
    public String thumbnail;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.imageNameList);
        dest.writeString(this.thumbnail);
    }

    public PicturesResponse() {
    }

    protected PicturesResponse(Parcel in) {
        this.imageNameList = in.createStringArrayList();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<PicturesResponse> CREATOR = new Parcelable.Creator<PicturesResponse>() {
        @Override
        public PicturesResponse createFromParcel(Parcel source) {
            return new PicturesResponse(source);
        }

        @Override
        public PicturesResponse[] newArray(int size) {
            return new PicturesResponse[size];
        }
    };
}
