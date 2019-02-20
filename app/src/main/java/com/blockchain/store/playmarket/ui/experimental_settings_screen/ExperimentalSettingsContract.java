package com.blockchain.store.playmarket.ui.experimental_settings_screen;

public class ExperimentalSettingsContract {
    public interface View {

        void reportDownloadUpdate(int i);

        void reportDownloadEndWithSuccess();

        void reportDownloadError(String message);
    }

    public interface Presenter {
        void init(View view);
    }
}
