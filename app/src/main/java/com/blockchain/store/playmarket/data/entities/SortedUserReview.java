package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

public class SortedUserReview {
    public UserReview userReview;
    public ArrayList<UserReview> reviewOnUserReview = new ArrayList<>();

    public int getReviewsCount() {
        int totalCount = 1;
        for (UserReview review : reviewOnUserReview) {
            totalCount++;
        }
        return totalCount;
    }
}
