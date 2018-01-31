package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.Constants;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailContract {

    public interface View {

        void setState(Constants.DOWNLOAD_STATE stateInstalled);

        void setButtonText(String started);

    }

    public interface Presenter {
        public void init(View view);

        void registerCallback(App app);

        void appDownloadClicked(App app);

        void checkAppLoadState(App app);

        void onDestroy();
    }
}
