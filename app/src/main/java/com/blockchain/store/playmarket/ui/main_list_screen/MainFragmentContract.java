package com.blockchain.store.playmarket.ui.main_list_screen;

import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.Category;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class MainFragmentContract {
    interface View{

        void firstItemsReady(AppDispatcherType apps);

        void onDispatchersReady(ArrayList<AppDispatcherType> appDispatcherTypes);
    }
    interface Presenter{
        void init(MainFragmentContract.View view);

        void setProvider(Category category);

        void onDestroy();

        void loadNewData(AppDispatcherType dispatcherType);
    }
}
