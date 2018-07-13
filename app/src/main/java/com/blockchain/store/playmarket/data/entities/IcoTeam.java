package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class IcoTeam implements Parcelable {
    public String name;
    public String photo;
    @SerializedName("descr")
    public String description;
    public SocialLinks social;


    public IcoTeam() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.description);
        dest.writeParcelable(this.social, flags);
    }

    protected IcoTeam(Parcel in) {
        this.name = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
        this.social = in.readParcelable(SocialLinks.class.getClassLoader());
    }

    public static final Creator<IcoTeam> CREATOR = new Creator<IcoTeam>() {
        @Override
        public IcoTeam createFromParcel(Parcel source) {
            return new IcoTeam(source);
        }

        @Override
        public IcoTeam[] newArray(int size) {
            return new IcoTeam[size];
        }
    };
}
