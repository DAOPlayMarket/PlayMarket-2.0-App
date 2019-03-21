package com.blockchain.store.dao.ui.dividends_screen;

public class DividendsContract {

    interface View {

    }

    interface Presenter {

        void init(View view);

        void getDividendsTokens();

    }

}
