package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 22.02.2018.
 */

public class ChangellyMinimumAmountResponse implements Parcelable {
    public String jsonrpc;
    public String id;
    public String result;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jsonrpc);
        dest.writeString(this.id);
        dest.writeString(this.result);
    }

    public ChangellyMinimumAmountResponse() {
    }

    protected ChangellyMinimumAmountResponse(Parcel in) {
        this.jsonrpc = in.readString();
        this.id = in.readString();
        this.result = in.readString();
    }

    public static final Parcelable.Creator<ChangellyMinimumAmountResponse> CREATOR = new Parcelable.Creator<ChangellyMinimumAmountResponse>() {
        @Override
        public ChangellyMinimumAmountResponse createFromParcel(Parcel source) {
            return new ChangellyMinimumAmountResponse(source);
        }

        @Override
        public ChangellyMinimumAmountResponse[] newArray(int size) {
            return new ChangellyMinimumAmountResponse[size];
        }
    };
}
