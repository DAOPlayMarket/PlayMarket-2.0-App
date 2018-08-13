package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.MyAppsAdapter;
import com.blockchain.store.playmarket.data.entities.AppLibrary;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAppsActivity extends AppCompatActivity implements MyAppsContract.View {
    private static final String TAG = "MyAppsActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_holder) LinearLayout errorHolder;

    private MyAppsPresenter presenter;
    private MyAppsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);
        ButterKnife.bind(this);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new MyAppsPresenter();
        presenter.init(this);
        presenter.getApps();
    }

    @Override
    public void onAppsReady(ArrayList<AppLibrary> appLibraries) {
        for (AppLibrary appLibrary : appLibraries) {
            Log.d(TAG, "onAppsReady: " + appLibrary.isHasUpdate());

        }
        adapter = new MyAppsAdapter(appLibraries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAppsFailed(Throwable throwable) {
        Log.d(TAG, "onAppsFailed() called with: throwable = [" + throwable + "]");
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorRepeatClicked() {
        presenter.getApps();
    }

    @OnClick(R.id.top_layout_back_arrow)
    public void onBackArrowClicked() {
        this.onBackPressed();
    }

    @Override
    public void showLoading(boolean isShow) {

    }
}
