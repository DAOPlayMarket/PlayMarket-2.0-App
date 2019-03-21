package com.blockchain.store.playmarket.installer;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.blockchain.store.playmarket.installer.rootless.RootlessSAIPackageInstaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InstallerViewModel extends AndroidViewModel implements SAIPackageInstaller.InstallationStatusListener {
    public static final String EVENT_PACKAGE_INSTALLED = "package_installed";
    public static final String EVENT_INSTALLATION_FAILED = "installation_failed";
    public static final String EVENT_INSTALLING = "installing_started";
    public static final String EVENT_QUEUED = "install_queued";

    private SAIPackageInstaller mInstaller;
    private Context mContext;

    public enum InstallerState {
        IDLE, INSTALLING
    }

    private MutableLiveData<InstallerState> mState = new MutableLiveData<>();
    private MutableLiveData<Event<String[]>> mEvents = new MutableLiveData<>();

    public InstallerViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        ensureInstallerActuality();
    }

    public LiveData<InstallerState> getState() {
        return mState;
    }


    public LiveData<Event<String[]>> getEvents() {
        return mEvents;
    }


    public void installPackages(File apkFile) {
        ArrayList<File> files = new ArrayList<File>();
        files.add(apkFile);
        installPackages(files);
    }

    public void installPackages(List<File> apkFiles) {
        ensureInstallerActuality();
        mInstaller.startInstallationSession(mInstaller.createInstallationSession(apkFiles));
    }

    public void installPackagesFromZip(File zipWithApkFiles) {
        ensureInstallerActuality();
        mInstaller.startInstallationSession(mInstaller.createInstallationSession(zipWithApkFiles));
    }

    private void ensureInstallerActuality() {
        SAIPackageInstaller actualInstaller = RootlessSAIPackageInstaller.getInstance(mContext);
        if (actualInstaller != mInstaller) {
            if (mInstaller != null)
                mInstaller.removeStatusListener(this);

            mInstaller = actualInstaller;
            mInstaller.addStatusListener(this);
            mState.setValue(mInstaller.isInstallationInProgress() ? InstallerState.INSTALLING : InstallerState.IDLE);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mInstaller.removeStatusListener(this);
    }

    @Override
    public void onStatusChanged(long installationID, SAIPackageInstaller.InstallationStatus status, @Nullable String packageNameOrErrorDescription) {
        switch (status) {
            case QUEUED:
                mEvents.setValue(new Event<>(new String[]{EVENT_QUEUED, packageNameOrErrorDescription}));
            case INSTALLING:
                mState.setValue(InstallerState.INSTALLING);
                mEvents.setValue(new Event<>(new String[]{EVENT_INSTALLING, packageNameOrErrorDescription}));
                break;
            case INSTALLATION_SUCCEED:
                mState.setValue(InstallerState.IDLE);
                mEvents.setValue(new Event<>(new String[]{EVENT_PACKAGE_INSTALLED, packageNameOrErrorDescription}));
                break;
            case INSTALLATION_FAILED:
                mState.setValue(InstallerState.IDLE);
                mEvents.setValue(new Event<>(new String[]{EVENT_INSTALLATION_FAILED, packageNameOrErrorDescription}));
                break;
        }
    }
}
