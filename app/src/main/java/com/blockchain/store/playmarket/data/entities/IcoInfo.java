package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class IcoInfo implements Parcelable {
    @SerializedName("team")
    public ArrayList<IcoTeam> team;
    @SerializedName("advisors")
    public ArrayList<IcoTeam> advisors;
    public String youtubeID;
    public String tokenSold;
    public String usdRaised;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.team);
        dest.writeList(this.advisors);
        dest.writeString(this.youtubeID);
        dest.writeString(this.tokenSold);
        dest.writeString(this.usdRaised);
    }

    public IcoInfo() {
    }

    protected IcoInfo(Parcel in) {
        this.team = new ArrayList<IcoTeam>();
        in.readList(this.team, IcoTeam.class.getClassLoader());
        this.advisors = new ArrayList<IcoTeam>();
        in.readList(this.advisors, IcoTeam.class.getClassLoader());
        this.youtubeID = in.readString();
        this.tokenSold = in.readString();
        this.usdRaised = in.readString();
    }

    public static final Parcelable.Creator<IcoInfo> CREATOR = new Parcelable.Creator<IcoInfo>() {
        @Override
        public IcoInfo createFromParcel(Parcel source) {
            return new IcoInfo(source);
        }

        @Override
        public IcoInfo[] newArray(int size) {
            return new IcoInfo[size];
        }
    };
}
