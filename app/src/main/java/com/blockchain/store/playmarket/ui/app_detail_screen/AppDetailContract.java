package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;


/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailContract {

    public interface View {

        void onDetailedInfoReady(AppInfo appInfo);

        void setActionButtonText(String string);

        void setInvestDeleteButtonText(String string);

        void onDetailedInfoFailed(Throwable throwable);

        void setProgress(boolean isShow);

        void showErrorView(boolean isShow);

        void setDeleteButtonVisibility(boolean isShow);
    }

    public interface Presenter {
        public void init(View view);

        void getDetailedInfo(App app);

        void onActionButtonClicked(App app);

        void loadButtonsState(App app);

        void onDestroy();

        void onDeleteButtonClicked(App app);
    }
}
