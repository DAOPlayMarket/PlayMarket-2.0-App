package com.blockchain.store.playmarket.ui.search_screen;

import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 12.02.2018.
 */

public class SearchContract {
    interface View{

        void onSearchResultReady(ArrayList<App> apps);

        void onSearchResultFail(Throwable throwable);

        void showProgress(boolean isShow);
    }
    interface Presenter{
        void init(View view);

        void searchQuery(String searchQuery);
    }
}
