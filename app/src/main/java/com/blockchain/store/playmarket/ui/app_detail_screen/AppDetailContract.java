package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailContract {

    public interface View {

    }

    public interface Presenter {
        public void init(View view);

        void appDownloadClicked(App app);
    }
}
