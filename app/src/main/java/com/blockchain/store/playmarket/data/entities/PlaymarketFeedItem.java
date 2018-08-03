package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

class PlaymarketFeedItem implements Parcelable {
    public String title;
    public String pubDate;
    public String link;
    public String guid;
    public String author;
    public String thumbnail;
    public String description;
    public String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.pubDate);
        dest.writeString(this.link);
        dest.writeString(this.guid);
        dest.writeString(this.author);
        dest.writeString(this.thumbnail);
        dest.writeString(this.description);
        dest.writeString(this.content);
    }

    public PlaymarketFeedItem() {
    }

    protected PlaymarketFeedItem(Parcel in) {
        this.title = in.readString();
        this.pubDate = in.readString();
        this.link = in.readString();
        this.guid = in.readString();
        this.author = in.readString();
        this.thumbnail = in.readString();
        this.description = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<PlaymarketFeedItem> CREATOR = new Parcelable.Creator<PlaymarketFeedItem>() {
        @Override
        public PlaymarketFeedItem createFromParcel(Parcel source) {
            return new PlaymarketFeedItem(source);
        }

        @Override
        public PlaymarketFeedItem[] newArray(int size) {
            return new PlaymarketFeedItem[size];
        }
    };
}
