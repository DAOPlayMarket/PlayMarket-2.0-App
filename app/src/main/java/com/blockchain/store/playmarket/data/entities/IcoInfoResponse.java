package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class IcoInfoResponse implements Parcelable {
    public String totalSupply;
    public String decimals;
    public String weiRaised;
    public String tokensSold;
    public String targetInUsd;
    public String weiRefunded;
    public String deceloperAddress;
    public String numberofPeriods;
    public String stage;
    public ArrayList<IcoStages> stages;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalSupply);
        dest.writeString(this.decimals);
        dest.writeString(this.weiRaised);
        dest.writeString(this.tokensSold);
        dest.writeString(this.targetInUsd);
        dest.writeString(this.weiRefunded);
        dest.writeString(this.deceloperAddress);
        dest.writeString(this.numberofPeriods);
        dest.writeString(this.stage);
        dest.writeTypedList(this.stages);
    }

    public IcoInfoResponse() {
    }

    protected IcoInfoResponse(Parcel in) {
        this.totalSupply = in.readString();
        this.decimals = in.readString();
        this.weiRaised = in.readString();
        this.tokensSold = in.readString();
        this.targetInUsd = in.readString();
        this.weiRefunded = in.readString();
        this.deceloperAddress = in.readString();
        this.numberofPeriods = in.readString();
        this.stage = in.readString();
        this.stages = in.createTypedArrayList(IcoStages.CREATOR);
    }

    public static final Parcelable.Creator<IcoInfoResponse> CREATOR = new Parcelable.Creator<IcoInfoResponse>() {
        @Override
        public IcoInfoResponse createFromParcel(Parcel source) {
            return new IcoInfoResponse(source);
        }

        @Override
        public IcoInfoResponse[] newArray(int size) {
            return new IcoInfoResponse[size];
        }
    };
}
