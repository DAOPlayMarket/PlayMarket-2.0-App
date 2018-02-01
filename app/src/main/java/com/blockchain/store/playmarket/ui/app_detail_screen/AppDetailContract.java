package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailContract {

    public interface View {

        void onDetailedInfoReady(AppInfo appInfo);

        void setButtonText(String string);

        void onDetailedInfoFailed(Throwable throwable);

        void setProgress(boolean isShow);

        void showErrorView(boolean isShow);
    }

    public interface Presenter {
        public void init(View view);

        void getDetailedInfo(App app);

        void appDownloadClicked(App app);

        void checkAppLoadState(App app);

        void onDestroy();
    }
}
