package com.blockchain.store.playmarket.interfaces;

import android.view.View;

import com.blockchain.store.playmarket.data.entities.App;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public interface AppListCallbacks {
    void onAppClicked(App app);

    default void onAppClickedWithTransition(App app, View view){

    };

}
