package com.blockchain.store.playmarket.ui.app_detail_screen;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;
import com.blockchain.store.playmarket.utilities.installer.ApkInstaller;
import com.blockchain.store.playmarket.utilities.net.APIUtils;

import static com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract.*;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class AppDetailPresenter implements Presenter {
    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void appDownloadClicked(App app) {
        ApkInstaller apkInstaller = new ApkInstaller();
        apkInstaller.setContext(Application.getInstance().getApplicationContext());
        try {
            apkInstaller.execute(app.getDownloadLink());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
