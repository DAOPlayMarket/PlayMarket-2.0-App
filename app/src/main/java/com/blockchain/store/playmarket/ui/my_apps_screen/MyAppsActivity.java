package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
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

    }

    @Override
    public void onAppsFailed(Throwable throwable) {

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
