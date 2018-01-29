package com.blockchain.store.playmarket.ui.intro_logo_activity;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class SplashContracts {
    public interface View {

    }

    public interface Presenter {
        void init(View view);

        void getNearestNodes();

    }
}