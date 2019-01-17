package com.blockchain.store.dao.database.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vote implements Parcelable {

    public int proposalID;
    public boolean position;
    public String voter;
    public String justification;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.proposalID);
        dest.writeByte(this.position ? (byte) 1 : (byte) 0);
        dest.writeString(this.voter);
        dest.writeString(this.justification);
    }

    public Vote() {
    }

    protected Vote(Parcel in) {
        this.proposalID = in.readInt();
        this.position = in.readByte() != 0;
        this.voter = in.readString();
        this.justification = in.readString();
    }

    public static final Parcelable.Creator<Vote> CREATOR = new Parcelable.Creator<Vote>() {
        @Override
        public Vote createFromParcel(Parcel source) {
            return new Vote(source);
        }

        @Override
        public Vote[] newArray(int size) {
            return new Vote[size];
        }
    };
}
