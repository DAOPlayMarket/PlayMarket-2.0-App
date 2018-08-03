package com.blockchain.store.playmarket.ui.activity_screen;

import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;

public class NewsActivityContract {
    public interface View{
        void onNewsReady(PlaymarketFeed feed);
        void onNewsError(Throwable throwable);
        void showProgress(boolean isShow);
    }
    public interface Presenter{
        void init(View view);
        void getNews();
    }
}
