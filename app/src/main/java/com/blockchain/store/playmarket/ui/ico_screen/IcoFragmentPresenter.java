package com.blockchain.store.playmarket.ui.ico_screen;

import android.util.Log;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.repositories.IcoAppsRepository;

import java.util.ArrayList;

public class IcoFragmentPresenter implements IcoFragmentContracts.Presenter, IcoAppsRepository.IcoAppsRepositoryCallback {
    private IcoFragmentContracts.View view;
    private IcoAppsRepository icoAppsRepository;

    @Override
    public void init(IcoFragmentContracts.View view) {
        this.view = view;
        icoAppsRepository = new IcoAppsRepository();
    }

    @Override
    public void getIcoApps() {
        icoAppsRepository.getIcoApps(this);
    }

    @Override
    public void onIcoAppsReady(ArrayList<App> apps) {
        view.onIcoAppsReady(apps);
    }

    @Override
    public void onIcoAppsSubscribed() {
        view.setProgress(true);
    }

    @Override
    public void onIcoAppsTerminated() {
        view.setProgress(false);
    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {
        view.onIcoAppsFailed(throwable);
    }
}
