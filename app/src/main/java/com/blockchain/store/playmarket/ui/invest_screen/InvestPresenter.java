package com.blockchain.store.playmarket.ui.invest_screen;

import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CurrentInfo;
import com.blockchain.store.playmarket.data.entities.InvestAddressResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.BigInt;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 19.02.2018.
 */

public class InvestPresenter implements InvestContract.Presenter {
    private static final String TAG = "InvestPresenter";
    private InvestContract.View view;

    @Override
    public void init(InvestContract.View view) {
        this.view = view;
    }

    @Override
    public void onInvestClicked(AppInfo appInfo, String investAmount) {
        String account = AccountManager.getAddress().getHex();
        RestApi.getServerApi().getAccountInfo(account)

                .flatMap(accountInfo -> {
                    Log.d(TAG, "onInvestClicked() called with: appInfo = [" + appInfo + "], investCount = [" + investAmount + "]");
                    return mapInvestTransaction(accountInfo, investAmount);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onInvestSuccessful, this::onPurchaseError);
    }

    private Observable<PurchaseAppResponse> mapInvestTransaction(AccountInfoResponse accountInfo, String investCount) {
        Log.d(TAG, "mapInvestTransaction() called with: accountInfo = [" + accountInfo + "], investCount = [" + investCount + "]");
        String rawTransaction = "";
        try {
//            rawTransaction = CryptoUtils.generateInvestTransactionWithAddress(
//                    accountInfo.count,
//                    new BigInt(Long.parseLong(accountInfo.gasPrice)),
//                    investCount, "0xB268d40843224B539068606Fb9F0dbAFb430D765");
            Log.d(TAG, "handleAccountInfoResult: " + rawTransaction);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return RestApi.getServerApi().deployTransaction(rawTransaction);

    }

    private void onInvestSuccessful(PurchaseAppResponse purchaseAppResponse) {
    }


    private void onPurchaseError(Throwable throwable) {
    }

    public void getCurrentInfo(AppInfo appInfo) {
        RestApi.getServerApi().getCurrentInfo(appInfo.adrICO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurrentInfoReady, this::OnCurrentInfoError);
    }

    private void onCurrentInfoReady(CurrentInfo currentInfo) {
        view.onCurrentInfoReady(currentInfo);
    }

    private void OnCurrentInfoError(Throwable throwable) {
        view.onCurrentInfoError(throwable);

    }
}
