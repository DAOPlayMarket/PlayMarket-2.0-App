package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

class IcoFiles implements Parcelable {
//    public IcoDocs docs;
    public IcoImages images;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.images, flags);
    }

    public IcoFiles() {
    }

    protected IcoFiles(Parcel in) {
        this.images = in.readParcelable(IcoImages.class.getClassLoader());
    }

    public static final Parcelable.Creator<IcoFiles> CREATOR = new Parcelable.Creator<IcoFiles>() {
        @Override
        public IcoFiles createFromParcel(Parcel source) {
            return new IcoFiles(source);
        }

        @Override
        public IcoFiles[] newArray(int size) {
            return new IcoFiles[size];
        }
    };
}
