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
    public long proposalID;
    public String recipient;
    public long amount;
    public String description;
    public String fullDescHash;
    public int votesSupport;
    public int votesAgainst;
    public int quorum;
    public boolean active;
    @TypeConverters(VotesConverter.class)
    public ArrayList<Vote> votes = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.proposalID);
        dest.writeString(this.recipient);
        dest.writeLong(this.amount);
        dest.writeString(this.description);
        dest.writeString(this.fullDescHash);
        dest.writeInt(this.votesSupport);
        dest.writeInt(this.votesAgainst);
        dest.writeInt(this.quorum);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeList(this.votes);
    }

    public Proposal() {
    }

    protected Proposal(Parcel in) {
        this.proposalID = in.readInt();
        this.recipient = in.readString();
        this.amount = in.readInt();
        this.description = in.readString();
        this.fullDescHash = in.readString();
        this.votesSupport = in.readInt();
        this.votesAgainst = in.readInt();
        this.quorum = in.readInt();
        this.active = in.readByte() != 0;
        this.votes = new ArrayList<Vote>();
        in.readList(this.votes, Vote.class.getClassLoader());
    }

    public static final Parcelable.Creator<Proposal> CREATOR = new Parcelable.Creator<Proposal>() {
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
