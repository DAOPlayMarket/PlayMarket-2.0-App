package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CheckPurchaseResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;


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

        void showPurchaseDialog();

        void onCheckPurchaseReady(CheckPurchaseResponse checkPurchaseResponse);

        void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse);

        void onPurchaseError(Throwable throwable);
    }

    public interface Presenter {
        public void init(View view);

        void getDetailedInfo(App app);

        void onActionButtonClicked(App app);

        void loadButtonsState(App app, boolean isUserPurchasedApp);

        void onDestroy(App app);

        void onDeleteButtonClicked(App app);

        void onInvestClicked(AppInfo appInfo, String investCount);

        void onPurchasedClicked(AppInfo appInfo);
    }
}
