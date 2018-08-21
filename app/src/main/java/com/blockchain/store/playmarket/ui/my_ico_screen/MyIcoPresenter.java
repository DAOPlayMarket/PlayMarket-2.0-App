package com.blockchain.store.playmarket.ui.my_ico_screen;

import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.repositories.IcoAppsInfoRepository;

import java.util.ArrayList;

import static com.blockchain.store.playmarket.ui.my_ico_screen.MyIcoContract.*;

public class MyIcoPresenter implements Presenter, IcoAppsInfoRepository.IcoAppsRepositoryCallback {
    private View view;
    private IcoAppsInfoRepository icoAppsRepository;

    @Override
    public void init(View v) {
        this.view = v;
        icoAppsRepository = new IcoAppsInfoRepository();
    }

    @Override
    public void getMyIco() {
        icoAppsRepository.getIcoApps(this);

    }

    @Override
    public void onIcoAppsReady(ArrayList<AppInfo> apps) {


    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {

    }

    @Override
    public void onIcoAppsSubscribed() {
        view.showProgress(true);
    }

    @Override
    public void onIcoAppsTerminated() {
        view.showProgress(false);
    }
}
