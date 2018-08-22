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
    public void getMyIcoApps() {
        icoAppsRepository.getIcoApps()
                .map(IcoAppsInfoRepository::filterWithEmptyBalanc)
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onIcoAppsReady, this::onIcoAppsFailed);

    }

    @Override
    public void onIcoAppsReady(ArrayList<AppInfo> apps) {
        view.onIcoAppsReady(apps);

    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {
        view.onIcoAppsFailed(throwable);
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
