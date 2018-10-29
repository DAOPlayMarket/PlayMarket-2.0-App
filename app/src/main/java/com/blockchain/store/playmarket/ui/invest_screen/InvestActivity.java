package com.blockchain.store.playmarket.ui.invest_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.IcoInfoResponse;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;


import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.RECIPIENT_ARG;

public class InvestActivity extends YouTubeBaseActivity implements InvestContract.View, InvestAdapterCallback, AppCompatCallback {
    private static final String TAG = "InvestActivity";
    private static final String INVEST_APP_PARAM = "invest_app_param";
    private static final String INVEST_IS_OPEN_FROM_ICO = "open_from_param";

    private boolean isOpenFromIcoScreen;

    private Toolbar toolbar;
    private AppInfo appInfo;
    private InvestPresenter presenter;
    private InvestScreenAdapter adapter;
    private RecyclerView recyclerView;
    private TextView backArrow;
    private AppCompatDelegate appCompatDelegate;
    private ProgressBar progressBar;
    private View errorHolder;
    private Button errorRepeatButton;

    public static void start(Context context, AppInfo appInfo) {
        Intent starter = new Intent(context, InvestActivity.class);
        starter.putExtra(INVEST_APP_PARAM, appInfo);
        context.startActivity(starter);
    }

    public static void startFromIco(Context context, AppInfo appInfo) {
        Intent starter = new Intent(context, InvestActivity.class);
        starter.putExtra(INVEST_APP_PARAM, appInfo);
        starter.putExtra(INVEST_IS_OPEN_FROM_ICO, true);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCompatDelegate = AppCompatDelegate.create(this, this);
        appCompatDelegate.onCreate(savedInstanceState);
        appCompatDelegate.setContentView(R.layout.activity_invest);
        appCompatDelegate.setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        backArrow = findViewById(R.id.top_layout_back_arrow);
        progressBar = findViewById(R.id.progress_bar);
        errorHolder = findViewById(R.id.error_holder);
        errorRepeatButton = findViewById(R.id.error_view_repeat_btn);
        backArrow.setOnClickListener(v -> super.onBackPressed());
        if (getIntent() != null) {
            appInfo = getIntent().getParcelableExtra(INVEST_APP_PARAM);
            isOpenFromIcoScreen = getIntent().getBooleanExtra(INVEST_IS_OPEN_FROM_ICO, false);
            errorRepeatButton.setOnClickListener(v -> getCurrentInfo());
        } else {
            throw new RuntimeException("App must be provided!");
        }
        if (appInfo == null) {
            setUpRecycler(null);
            return;
        }
        attachPresenter();
        if (appInfo.icoInfoResponse == null) {
            getCurrentInfo();
        } else {
            setUpRecycler(appInfo);
        }
    }

    private void getCurrentInfo() {
        errorHolder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        presenter.getCurrentInfo(appInfo);
    }

    private void
    setUpRecycler(AppInfo appInfo) {
        if (appInfo == null) {
            adapter = new InvestScreenAdapter(this, isOpenFromIcoScreen);
        } else {
            adapter = new InvestScreenAdapter(appInfo, this, isOpenFromIcoScreen);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void attachPresenter() {
        presenter = new InvestPresenter();
        presenter.init(this);
    }

    @Override
    public void onCurrentInfoReady(IcoInfoResponse icoInfoResponse) {
        progressBar.setVisibility(View.GONE);
        errorHolder.setVisibility(View.GONE);
        appInfo.icoInfoResponse = icoInfoResponse;
        setUpRecycler(appInfo);
    }

    @Override
    public void onCurrentInfoError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        errorHolder.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInvestBtnClicked(String address) {
        Intent intent = new Intent(this, TransferActivity.class);
        intent.putExtra(RECIPIENT_ARG, address);
        startActivity(intent);
    }

    @Override
    public void onOpenAppClicked() {
        AppDetailActivity.start(this, appInfo);
        this.finish();
    }

    //region Action bar delegate
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    //endregion

}
