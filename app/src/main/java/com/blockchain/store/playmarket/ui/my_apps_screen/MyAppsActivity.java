package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.MyAppsAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.installer.InstallerViewModel;
import com.blockchain.store.playmarket.interfaces.AppsAdapterCallback;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAppsActivity extends BaseActivity implements MyAppsContract.View, AppsAdapterCallback {
    private static final String TAG = "MyAppsActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.layout_holder) View layoutHolder;
    @BindView(R.id.update_all_btn) Button updateAllBtn;

    private MyAppsPresenter presenter;
    private MyAppsAdapter adapter;
    private boolean isGlobalLayoutListenerTriggered;
    private Snackbar snackbar;
    private Integer howManyAppsNeedUpdate;
    private InstallerViewModel installerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);
        ButterKnife.bind(this);
        attachPresenter();
        installerViewModel = ViewModelProviders.of(this).get(InstallerViewModel.class);
        ViewTreeObserver viewTreeObserver = layoutHolder.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            if (!isGlobalLayoutListenerTriggered) {
                presenter.getApps();
                isGlobalLayoutListenerTriggered = true;
            }
        });
        setTitle();
        initSnackBar();
        initViewModel();
    }

    private void initViewModel() {
        installerViewModel.getState().observe(this, state -> {
            switch (state) {
                case IDLE:
                    break;
                case INSTALLING:

                    break;
            }
        });
        installerViewModel.getEvents().observe(this, (event) -> {
            if (event.isConsumed()) {
                return;
            }
            String[] eventData = event.consume();

            switch (eventData[0]) {
                case InstallerViewModel.EVENT_INSTALLATION_FAILED:
                    adapter.handleInstallationFailure();
//                    ToastUtil.showToast(eventData[1]);
                    break;

                case InstallerViewModel.EVENT_PACKAGE_INSTALLED:
                    adapter.handleInstallationSucceess(eventData[1]);
                    break;

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (adapter != null) {
            presenter.updateAppStatuses(adapter.getAllItemsWithUpdate());
        }
    }

    private void initSnackBar() {
        snackbar = Snackbar.make(layoutHolder, "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.update, v -> adapter.performActionOnSelectedItems());
    }

    private void setTitle() {
        toolbarTitle.setText(R.string.my_apps);
    }

    private void attachPresenter() {
        presenter = new MyAppsPresenter();
        presenter.init(this);

    }

    @Override
    public void onAppsReady(ArrayList<AppLibrary> appLibraries, Integer howManyAppsNeedUpdate) {
        updateAllBtn.setVisibility(howManyAppsNeedUpdate > 0 ? View.VISIBLE : View.GONE);

        this.howManyAppsNeedUpdate = howManyAppsNeedUpdate;

        adapter = new MyAppsAdapter(appLibraries, this);
        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAppsFailed(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateApp(App app, int progress, Constants.APP_STATE appState) {
        runOnUiThread(() -> {
            if (adapter != null)
                adapter.reportAppStateChanged(app, progress, appState);
        });

    }

    @Override
    public void onCheckForUpdatesReady(ArrayList<AppLibrary> allItemsWithUpdate) {
        if (adapter != null) {
            adapter.refreshItemStatus(allItemsWithUpdate);
        }
    }

    @Override
    public void installApp(App app) {
        installerViewModel.installPackages(new MyPackageManager().getFileFromApp(app));
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorRepeatClicked() {
        errorHolder.setVisibility(View.GONE);
        presenter.getApps();
    }

    @OnClick(R.id.top_layout_back_arrow)
    public void onBackArrowClicked() {
        this.onBackPressed();
    }

    @Override
    public void showLoading(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActionButtonClicked(AppLibrary app) {
        presenter.onActionItemClicked(app);
    }

    @OnClick(R.id.update_all_btn)
    void onUpdateAllClicked() {
        adapter.performUpdateAll();
    }

    @Override
    public void onLayoutClicked(int numberOfSelectedItem) {
        updateSnackBar(numberOfSelectedItem);

    }

    private void updateSnackBar(int numberOfSelectedItem) {
        String snackbarText = String.format(getString(R.string.my_apps_update_snackbar_text), numberOfSelectedItem, howManyAppsNeedUpdate);
        snackbar.setText(snackbarText);
        if (numberOfSelectedItem == 0) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            presenter.onDestroy(adapter.getAllItems());
        }

    }
}
