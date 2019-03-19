package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.content.pm.ApplicationInfo;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAppsPresenter implements MyAppsContract.Presenter, NotificationManagerCallbacks {
    private static final String TAG = "MyAppsPresenter";

    private MyAppsContract.View view;
    private String arrayOfInstalledApps;

    @Override
    public void init(MyAppsContract.View view) {
        this.view = view;
        arrayOfInstalledApps = MyPackageManager.prepareApplicationInfoForRequest();
    }

    public void getApps() {
        RestApi.getServerApi().getAppsByPackage(arrayOfInstalledApps)
                .map(this::mapWithLocalApps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showLoading(true))
                .doOnTerminate(() -> view.showLoading(false))
                .subscribe(this::onAppsReady, this::onAppsFailed);
    }

    private Pair<ArrayList<AppLibrary>, Integer> mapWithLocalApps(ArrayList<App> apps) {
        Integer howManyAppsNeedUpdate = 0;
        ArrayList<AppLibrary> appLibraries = new ArrayList<>();
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
            appLibrary.versionCode = MyPackageManager.getVersionCodeByPackageName(appLibrary.applicationInfo.packageName);

            if (isHasLocalCopy && appLibrary.isHasUpdate) {
                MyPackageManager myPackageManager = new MyPackageManager();
                File fileByPackageName = myPackageManager.findFileByPackageName(appLibrary.app.packageName, Application.getInstance().getBaseContext());
                int versionFromFile = myPackageManager.getVersionFromFile(fileByPackageName);
                if (versionFromFile > appLibrary.versionCode) {
                    appLibrary.appState = Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED;
                    appLibrary.isHasUpdate = false;
                } else {
                    howManyAppsNeedUpdate++;
                }
                loadState(appLibrary);
                appLibraries.add(0, appLibrary);
            } else {
                appLibraries.add(appLibrary);
            }

        }
        addIconAndTitle(appLibraries);
        return new Pair<>(appLibraries, howManyAppsNeedUpdate);
    }

    private void loadState(AppLibrary appLibrary) {
        if (NotificationManager.getManager().isCallbackAlreadyRegistered(appLibrary.app)) {
            NotificationManager.getManager().registerCallback(appLibrary.app, this);
        }
    }

    private void addIconAndTitle(ArrayList<AppLibrary> appLibraries) {
        for (AppLibrary library : appLibraries) {
            library.title = (String) MyPackageManager.get().getApplicationLabel(library.applicationInfo);
            library.icon = library.applicationInfo.loadIcon(MyPackageManager.get());
        }
    }

    private void onAppsReady(Pair<ArrayList<AppLibrary>, Integer> pair) {
        view.onAppsReady(pair.first, pair.second);
    }


    private void onAppsFailed(Throwable throwable) {
        view.onAppsFailed(throwable);
    }

    public void onActionItemClicked(AppLibrary appLibrary) {
        if (appLibrary.appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED) {
            installApk(appLibrary.app);
//            new MyPackageManager().installApkByApp(appLibrary.app);
        } else if (appLibrary.appState == Constants.APP_STATE.STATE_UNKNOWN) {
            NotificationManager.getManager().registerCallback(appLibrary.app, this);
            new MyPackageManager().startDownloadApkService(appLibrary.app);
            view.updateApp(appLibrary.app, 0, Constants.APP_STATE.STATE_DOWNLOADING);
        }

    }

    private void installApk(App app) {
        this.view.installApp(app);
    }

    @Override
    public void onAppDownloadStarted(NotificationImpl app) {
        view.updateApp((App) app, 0, Constants.APP_STATE.STATE_DOWNLOAD_STARTED);
    }

    @Override
    public void onAppDownloadProgressChanged(NotificationImpl app, int progress) {
        view.updateApp((App) app, progress, Constants.APP_STATE.STATE_DOWNLOADING);

    }

    @Override
    public void onAppDownloadSuccessful(NotificationImpl app) {
        view.updateApp((App) app, 0, Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED);
    }

    @Override
    public void onAppDownloadError(NotificationImpl app, String message) {
        view.updateApp((App) app, 0, Constants.APP_STATE.STATE_DOWNLOAD_ERROR);
    }

    public void onDestroy(ArrayList<AppLibrary> allItems) {
        for (AppLibrary library : allItems) {
            NotificationManager.getManager().removeCallback(library.app, this);
        }
    }

    public void updateAppStatuses(ArrayList<AppLibrary> allItemsWithUpdate) {
        MyPackageManager myPackageManager = new MyPackageManager();
        for (AppLibrary library : allItemsWithUpdate) {
            library.isHasUpdate = MyPackageManager.isAppHasUpdate(library.app);
            if (library.appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED) {
                File fileByPackageName = myPackageManager.findFileByPackageName(library.app.packageName, Application.getInstance().getBaseContext());
                int versionFromFile = myPackageManager.getVersionFromFile(fileByPackageName);
                library.isHasUpdate = versionFromFile > library.versionCode;

                if (versionFromFile <= library.versionCode) {
                    library.appState = Constants.APP_STATE.STATE_UNKNOWN;
                }
            } else {
                library.appState = Constants.APP_STATE.STATE_UNKNOWN;
            }
        }
        view.onCheckForUpdatesReady(allItemsWithUpdate);
    }
}
