package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.blockchain.store.playmarket.interfaces.AppsAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAppsActivity extends AppCompatActivity implements MyAppsContract.View, AppsAdapterCallback {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);
        ButterKnife.bind(this);
        attachPresenter();
        ViewTreeObserver viewTreeObserver = layoutHolder.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            if (!isGlobalLayoutListenerTriggered) {
                presenter.getApps();
                isGlobalLayoutListenerTriggered = true;
            }
        });
        setTitle();
        initSnackBar();
    }

    private void initSnackBar() {
        snackbar = Snackbar.make(layoutHolder, "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Update", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setTitle() {
        toolbarTitle.setText(R.string.my_apps);
    }

    private void attachPresenter() {
        presenter = new MyAppsPresenter();
        presenter.init(this);

    }

    @Override
    public void onAppsReady(ArrayList<AppLibrary> appLibraries, Boolean isHasAnyUpdates) {
        updateAllBtn.setVisibility(isHasAnyUpdates ? View.VISIBLE : View.GONE);
        adapter = new MyAppsAdapter(appLibraries, this);
        adapter.setHasStableIds(true);
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
    public void onActionButtonClicked(AppLibrary app, int position) {
        presenter.onActionItemClicked(app, position);
    }

    @Override
    public void onLayoutClicked(AppLibrary app, int position) {
        adapter.selectItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            presenter.onDestroy(adapter.getAllItems());
        }

    }
}
