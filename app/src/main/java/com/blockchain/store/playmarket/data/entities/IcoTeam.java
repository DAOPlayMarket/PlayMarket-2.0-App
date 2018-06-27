package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class IcoTeam implements Parcelable {
    @SerializedName("n")
    public String name;
    @SerializedName("p")
    public String photo;
    @SerializedName("d")
    public String description;
    @SerializedName("c")
    public ArrayList<Map<String,String>> socialLinks;

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
        dest.writeList(this.socialLinks);
    }

    protected IcoTeam(Parcel in) {
        this.name = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
        this.socialLinks = new ArrayList<Map<String, String>>();

        in.readList(this.socialLinks, Map.class.getClassLoader());
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
