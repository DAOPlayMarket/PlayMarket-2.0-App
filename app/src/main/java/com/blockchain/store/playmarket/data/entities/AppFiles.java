package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class AppFiles implements Parcelable {
    public String apk;
    public AppImages images;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apk);
        dest.writeParcelable(this.images, flags);
    }

    public AppFiles() {
    }

    protected AppFiles(Parcel in) {
        this.apk = in.readString();
        this.images = in.readParcelable(AppImages.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppFiles> CREATOR = new Parcelable.Creator<AppFiles>() {
        @Override
        public AppFiles createFromParcel(Parcel source) {
            return new AppFiles(source);
        }

        @Override
        public AppFiles[] newArray(int size) {
            return new AppFiles[size];
        }
    };
}
