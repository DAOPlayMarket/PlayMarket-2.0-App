package com.blockchain.store.playmarket.ui.main_list_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.Category;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class MainMenuContract {
    interface View {
        void setProgress(boolean isShow);

        void onCategoryLoaded(ArrayList<Category> categories);

        void onCategoryLoadFailed(Throwable throwable);

        void onSearchResultReady(ArrayList<App> apps);

        void onSearchResultFail(Throwable throwable);

        void onSearchSuggestionReady(String[] strings);
    }

    interface Presenter {
        void init(MainMenuContract.View view);

        void loadCategories();

        void searchQuery(String text);

    }
}
