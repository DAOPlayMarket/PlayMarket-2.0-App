package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import com.blockchain.store.playmarket.repositories.IpfsRepository;

public class ExperimentalSettingsContract {
    public interface View {

        void reportDownloadUpdate(int i);

        void reportDownloadEndWithSuccess();

        void reportDownloadError(String message);

        void onIpfsError(Throwable throwable);

        void onDataReady(IpfsRepository.IpfsRepositoryData ipfsRepositoryData);
    }

    public interface Presenter {
        void init(View view);
    }
}
