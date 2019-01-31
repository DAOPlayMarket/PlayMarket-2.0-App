package com.blockchain.store.dao.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.Application;

@Entity
public class Proposal implements Parcelable {

    public enum ProposalType {
        Ongoing,
        Unexecutable,
        NotExecutedAccepted,
        NotExecutedNotAccepted,
        Executed,
    }

    @PrimaryKey
    public int proposalID;
    public long endTimeOfVoting;
    public boolean isExecuted;
    public boolean proposalPassed;
    public long numberOfVotes;
    public long votesSupport;
    public long votesAgainst;
    public String recipient;
    public long amount;
    public String description;
    public String fullDescriptionHash;

    public Proposal() {
    }

    public ProposalType getProposalType() {
        Rules rules = Application.getDaoDatabase().rulesDao().getRules();
        if (!isExecuted) {
            if ((endTimeOfVoting * 1000) > System.currentTimeMillis()) return ProposalType.Ongoing;
            else if ((endTimeOfVoting * 1000) < System.currentTimeMillis()) {
                if (numberOfVotes >= rules.minimumQuorum) {
                    if (votesSupport >= rules.requisiteMajority) {
                        return ProposalType.NotExecutedAccepted;
                    }
                    else {
                        return ProposalType.NotExecutedNotAccepted;
                    }
                } else {
                    return ProposalType.Unexecutable;
                }
            }
        } else return ProposalType.Executed;
        return null;
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
        dest.writeLong(this.numberOfVotes);
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
