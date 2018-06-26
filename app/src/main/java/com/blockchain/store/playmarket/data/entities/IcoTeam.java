package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

class IcoTeam implements Parcelable {
    @SerializedName("n")
    public String name;
    @SerializedName("p")
    public String photo;
    @SerializedName("d")
    public String description;
//    @SerializedName("c")
//    public SocialLinks socialLinks;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.description);
    }

    public IcoTeam() {
    }

    protected IcoTeam(Parcel in) {
        this.name = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<IcoTeam> CREATOR = new Parcelable.Creator<IcoTeam>() {
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
