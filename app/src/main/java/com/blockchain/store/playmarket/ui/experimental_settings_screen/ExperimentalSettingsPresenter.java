package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import com.blockchain.store.playmarket.data.entities.IpfsData;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.repositories.IpfsRepository;
import com.blockchain.store.playmarket.repositories.IpfsRepository.IpfsRepositoryData;
import com.blockchain.store.playmarket.ui.experimental_settings_screen.ExperimentalSettingsContract.View;

public class ExperimentalSettingsPresenter implements ExperimentalSettingsContract.Presenter, NotificationManagerCallbacks {
    View view;

    @Override
    public void init(View view) {
        this.view = view;
        loadState();
    }

    public void loadState() {
        if (NotificationManager.getManager().isCallbackAlreadyRegistered(new IpfsData(""))) {
            NotificationManager.getManager().registerCallback(new IpfsData(""), this);
        }
    }

    @Override
    public void onAppDownloadStarted(NotificationImpl app) {
        view.reportDownloadUpdate(0);
    }

    @Override
    public void onAppDownloadProgressChanged(NotificationImpl app, int progress) {
        view.reportDownloadUpdate(progress);
    }

    @Override
    public void onAppDownloadSuccessful(NotificationImpl app) {
        view.reportDownloadEndWithSuccess();
    }

    @Override
    public void onAppDownloadError(NotificationImpl app, String message) {
        view.reportDownloadError(message);
    }

    public void loadIpfsData() {
        new IpfsRepository().getIpfsData()
                .subscribe(this::onIpfsDataReady,this::onIpfsError);
    }

    private void onIpfsDataReady(IpfsRepositoryData ipfsRepositoryData) {
        view.onDataReady(ipfsRepositoryData);
    }


    private void onIpfsError(Throwable throwable) {
        view.onIpfsError(throwable);
    }
}
