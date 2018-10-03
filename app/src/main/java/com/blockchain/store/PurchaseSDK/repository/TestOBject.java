package com.blockchain.store.PurchaseSDK.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.PurchaseSDK.services.RemoteConstants;
import com.blockchain.store.playmarket.utilities.Constants;

public class TestOBject implements Parcelable {
    public String someString = "some String";
    public int randomInt = 123;
    public RemoteConstants.TransactionTypes transactionTypes = RemoteConstants.TransactionTypes.BUY;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.someString);
        dest.writeInt(this.randomInt);
        dest.writeInt(this.transactionTypes == null ? -1 : this.transactionTypes.ordinal());
    }

    public TestOBject() {
    }

    protected TestOBject(Parcel in) {
        this.someString = in.readString();
        this.randomInt = in.readInt();
        int tmpTransactionTypes = in.readInt();
        this.transactionTypes = tmpTransactionTypes == -1 ? null : RemoteConstants.TransactionTypes.values()[tmpTransactionTypes];
    }

    public static final Parcelable.Creator<TestOBject> CREATOR = new Parcelable.Creator<TestOBject>() {
        @Override
        public TestOBject createFromParcel(Parcel source) {
            return new TestOBject(source);
        }

        @Override
        public TestOBject[] newArray(int size) {
            return new TestOBject[size];
        }
    };
}
