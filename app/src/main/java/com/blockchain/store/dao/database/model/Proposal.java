package com.blockchain.store.dao.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.dao.database.database_converters.VotesConverter;

import java.util.ArrayList;

@Entity
public class Proposal implements Parcelable {

    @PrimaryKey
    public int proposalID;
    public long endTimeOfVoting;
    public boolean isExecuted;
    public boolean proposalPassed;
    public int numberOfVotes;
    public long votesSupport;
    public long votesAgainst;
    public String recipient;
    public long amount;
    public String description;
    public String fullDescriptionHash;

    public Proposal() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.proposalID);
        dest.writeLong(this.endTimeOfVoting);
        dest.writeByte(this.isExecuted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.proposalPassed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.numberOfVotes);
        dest.writeLong(this.votesSupport);
        dest.writeLong(this.votesAgainst);
        dest.writeString(this.recipient);
        dest.writeLong(this.amount);
        dest.writeString(this.description);
        dest.writeString(this.fullDescriptionHash);
    }

    protected Proposal(Parcel in) {
        this.proposalID = in.readInt();
        this.endTimeOfVoting = in.readLong();
        this.isExecuted = in.readByte() != 0;
        this.proposalPassed = in.readByte() != 0;
        this.numberOfVotes = in.readInt();
        this.votesSupport = in.readLong();
        this.votesAgainst = in.readLong();
        this.recipient = in.readString();
        this.amount = in.readLong();
        this.description = in.readString();
        this.fullDescriptionHash = in.readString();
    }

    public static final Creator<Proposal> CREATOR = new Creator<Proposal>() {
        @Override
        public Proposal createFromParcel(Parcel source) {
            return new Proposal(source);
        }

        @Override
        public Proposal[] newArray(int size) {
            return new Proposal[size];
        }
    };
}
