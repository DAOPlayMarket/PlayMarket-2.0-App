package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.AppDetailAdapter;
import com.blockchain.store.playmarket.interfaces.AppDetailsImpl;

import java.util.ArrayList;

public class AppReviewsData implements AppDetailsImpl {
    public ArrayList<UserReview> userReviews;

    public AppReviewsData(ArrayList<UserReview> userReviews) {
        this.userReviews = userReviews;
    }

    @Override
    public AppDetailAdapter.ViewTypes getViewType() {
        return AppDetailAdapter.ViewTypes.REVIEWS;
    }
}
