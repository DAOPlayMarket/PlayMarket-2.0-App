package com.blockchain.store.playmarket.ui.change_account_screen;

import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.repositories.UserBalanceRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import java.util.ArrayList;

public class ChangeAccountPresenter implements ChangeAccountContract.Presenter {
    private static final String TAG = "ChangeAccountPresenter";

    private ChangeAccountContract.View view;

    @Override
    public void init(ChangeAccountContract.View view) {
        this.view = view;
    }

    @Override
    public void loadBalances() {
        ArrayList<String> allAddresses = AccountManager.getAllAddresses();
        new UserBalanceRepository().getMultipleAccountbalances(allAddresses)
                .subscribe(this::onNext, this::onError, this::onComplete);
    }

    @Override
    public void updateBalance(String address) {
        ArrayList<String> addresses = new ArrayList<>();
        addresses.add(address);
        new UserBalanceRepository().getMultipleAccountbalances(addresses)
                .subscribe(this::onNext, this::onError, this::onComplete);
    }

    private void onNext(Pair<String, String> pair) {
        view.onBalanceReady(pair);
        Log.d(TAG, "onNext() called with: stringStringPair = [" + pair + "]");
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "onError() called with: throwable = [" + throwable.getLocalizedMessage() + "]");
    }

    private void onComplete() {
        Log.d(TAG, "onComplete: ");
    }
}
