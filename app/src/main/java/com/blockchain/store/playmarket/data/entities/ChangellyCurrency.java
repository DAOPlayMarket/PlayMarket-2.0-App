package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ChangellyCurrency implements Parcelable {
    private static final String TAG = "ChangellyCurrency";
    public String name;
    public String fullName;
    public boolean enabled;

    public String getImageUrl() {
        Log.d(TAG, "getImageUrl: " + name);
        return "file:///android_asset/svg/" + name + ".svg";
        //return "https://changelly.com/coins/" + name + ".svg";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
    }

    public ChangellyCurrency() {
    }

    protected ChangellyCurrency(Parcel in) {
        this.name = in.readString();
        this.fullName = in.readString();
        this.enabled = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ChangellyCurrency> CREATOR = new Parcelable.Creator<ChangellyCurrency>() {
        @Override
        public ChangellyCurrency createFromParcel(Parcel source) {
            return new ChangellyCurrency(source);
        }

        @Override
        public ChangellyCurrency[] newArray(int size) {
            return new ChangellyCurrency[size];
        }
    };
}
