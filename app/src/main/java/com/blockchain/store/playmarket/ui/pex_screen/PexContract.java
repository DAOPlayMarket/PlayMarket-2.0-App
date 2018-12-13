package com.blockchain.store.playmarket.ui.pex_screen;

import com.blockchain.store.playmarket.data.entities.PexHistory;

public class PexContract {

    interface View {

        void onHistoryReady(PexHistory pexHistory);
    }

    interface Presenter {
        void init(View view);

        void loadHistory();

    }
}
