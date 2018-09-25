package com.blockchain.store.playmarket.ui.my_ico_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.IcoListAdapter;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.interfaces.AppInfoCallback;
import com.blockchain.store.playmarket.ui.invest_screen.InvestActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.RECIPIENT_ARG;

public class MyIcoActivity extends AppCompatActivity implements MyIcoContract.View, AppInfoCallback {
    private static final String TAG = "MyIcoActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_holder) LinearLayout errorHolder;

    private MyIcoPresenter presenter;
    private IcoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ico);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.my_ico);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new MyIcoPresenter();
        presenter.init(this);
        presenter.getMyIcoApps();
    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onIcoAppsReady(ArrayList<AppInfo> apps) {
        adapter = new IcoListAdapter(apps, this, true);
        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorViewRepeatClicked() {
        errorHolder.setVisibility(View.GONE);
        presenter.getMyIcoApps();
    }

    @Override
    public void onAppInfoClicked(AppInfo appinfo) {
        InvestActivity.startFromIco(this, appinfo);

    }

    @Override
    public void onAppTransferTokenClicked(AppInfo appinfo) {
        TransferActivity.startAsTokenTransfer(this, appinfo);
    }

    @Override
    public void onAppInvestClicked(String address) {
        Intent intent = new Intent(this, TransferActivity.class);
        intent.putExtra(RECIPIENT_ARG, address);
        startActivity(intent);
    }


}
