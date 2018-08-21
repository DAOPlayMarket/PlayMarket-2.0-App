package com.blockchain.store.playmarket.ui.my_ico_screen;

public class MyIcoContract {
    interface View {
        void showProgress(boolean isShow);
    }

    interface Presenter {
        void init(View v);

        void getMyIco();
    }
}
