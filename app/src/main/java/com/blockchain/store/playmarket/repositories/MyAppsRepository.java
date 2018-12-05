package com.blockchain.store.playmarket.repositories;

import android.content.pm.ApplicationInfo;
import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.utilities.MyPackageManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAppsRepository {

    public Observable<Pair<ArrayList<App>, Integer>> getApps() {
        String arrayOfInstalledApps = MyPackageManager.prepareApplicationInfoForRequest();
        return RestApi.getServerApi().getAppsByPackage(arrayOfInstalledApps)
                .map(this::mapWithLocalApps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Pair<ArrayList<App>, Integer> mapWithLocalApps(ArrayList<App> apps) {
        Integer howManyAppsNeedUpdate = 0;
        List<ApplicationInfo> allInstalledApps = MyPackageManager.getAllInstalledApps();
        for (ApplicationInfo applicationInfo : allInstalledApps) {
            AppLibrary appLibrary = new AppLibrary();
            appLibrary.applicationInfo = applicationInfo;
            boolean isHasLocalCopy = false;
            for (App app : apps) {
                if (applicationInfo.packageName.equalsIgnoreCase(app.packageName)) {
                    appLibrary.app = app;
                    isHasLocalCopy = true;
                }

            }
            appLibrary.isHasUpdate = MyPackageManager.isAppHasUpdate(appLibrary.app);
            appLibrary.versionName = MyPackageManager.getVersionNameByPackageName(appLibrary.applicationInfo.packageName);
            if (isHasLocalCopy && appLibrary.isHasUpdate) {
                howManyAppsNeedUpdate++;
            }

        }
        return new Pair<>(apps, howManyAppsNeedUpdate);
    }
}
