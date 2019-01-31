package com.blockchain.store.playmarket.ui.all_reviews_screen;

import com.blockchain.store.playmarket.data.entities.App;

public class AllReviewsContract {
    interface View{

        void onReviewError(Throwable throwable);

        void onReviewSendSuccessfully();

    }
    interface Presenter{

        void init(View view, App app);
    }
}
