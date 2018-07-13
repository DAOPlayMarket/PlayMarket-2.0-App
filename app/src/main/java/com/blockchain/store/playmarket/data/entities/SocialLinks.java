package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialLinks implements Parcelable {
    public String googlePlus;
    public String facebook;
    public String linkedin;
    public String instagram;
    public String vk;
    public String youtube;
    public String telegram;
    public String git;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.googlePlus);
        dest.writeString(this.facebook);
        dest.writeString(this.linkedin);
        dest.writeString(this.instagram);
        dest.writeString(this.vk);
        dest.writeString(this.youtube);
        dest.writeString(this.telegram);
        dest.writeString(this.git);
    }

    public SocialLinks() {
    }

    protected SocialLinks(Parcel in) {
        this.googlePlus = in.readString();
        this.facebook = in.readString();
        this.linkedin = in.readString();
        this.instagram = in.readString();
        this.vk = in.readString();
        this.youtube = in.readString();
        this.telegram = in.readString();
        this.git = in.readString();
    }

    public static final Parcelable.Creator<SocialLinks> CREATOR = new Parcelable.Creator<SocialLinks>() {
        @Override
        public SocialLinks createFromParcel(Parcel source) {
            return new SocialLinks(source);
        }

        @Override
        public SocialLinks[] newArray(int size) {
            return new SocialLinks[size];
        }
    };
}
