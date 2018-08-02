package com.blockchain.store.playmarket.ui.invest_screen;

import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CurrentInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;

/**
 * Created by Crypton04 on 19.02.2018.
 */

public class InvestContract {
    interface View{

        void onCurrentInfoReady(CurrentInfo currentInfo);

        void onCurrentInfoError(Throwable throwable);
    }
    interface Presenter{
        void init(View view);

        void onInvestClicked(AppInfo appInfo, String investAmount);
    }
}
