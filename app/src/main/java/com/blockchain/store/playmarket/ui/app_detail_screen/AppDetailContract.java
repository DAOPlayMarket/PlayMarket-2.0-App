package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.SortedUserReview;

import java.util.ArrayList;


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

        void onCheckPurchaseReady(boolean isPurchased);

        void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse);

        void onPurchaseError(Throwable throwable);

        void onReviewsReady(ArrayList<SortedUserReview> userReviews);
    }

    public interface Presenter {
        public void init(View view);

        void getDetailedInfo(App app);

        void onActionButtonClicked(App app);

        void loadButtonsState(App app, boolean isUserPurchasedApp);

        void onDestroy(App app);

        void onDeleteButtonClicked(App app);


        void onPurchasedClicked(AppInfo appInfo);

        void getReviews(String appId);
    }
}
