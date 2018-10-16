package com.blockchain.store.playmarket.ui.ico_screen;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.repositories.IcoAppsInfoRepository;

import java.util.ArrayList;

public class IcoFragmentPresenter implements IcoFragmentContracts.Presenter, IcoAppsInfoRepository.IcoAppsRepositoryCallback {
    private IcoFragmentContracts.View view;
    private IcoAppsInfoRepository icoAppsRepository;

    @Override
    public void init(IcoFragmentContracts.View view) {
        this.view = view;
        icoAppsRepository = new IcoAppsInfoRepository();
    }

    @Override
    public void getIcoApps() {
        icoAppsRepository.getIcoApps(this);
    }

    @Override
    public void onIcoAppsReady(ArrayList<AppInfo> apps) {
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
        throwable.printStackTrace();
        view.onIcoAppsFailed(throwable);
    }
}
