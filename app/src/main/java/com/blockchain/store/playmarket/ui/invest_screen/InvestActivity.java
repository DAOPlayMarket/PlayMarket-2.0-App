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
import android.widget.ImageView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.google.android.youtube.player.YouTubeBaseActivity;


import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.RECIPIENT;

public class InvestActivity extends YouTubeBaseActivity implements InvestContract.View, InvestAdapterCallback, AppCompatCallback {
    private static final String TAG = "InvestActivity";
    private static final String INVEST_APP_PARAM = "invest_app_param";

    private Toolbar toolbar;
    private AppInfo appInfo;
    private InvestPresenter presenter;
    private InvestScreenAdapter adapter;
    private AppCompatDelegate appCompatDelegate;
    private RecyclerView recyclerView;
    private ImageView backArrow;

    public static void start(Context context, AppInfo appInfo) {
        Intent starter = new Intent(context, InvestActivity.class);
        starter.putExtra(INVEST_APP_PARAM, appInfo);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCompatDelegate = AppCompatDelegate.create(this, this);
        appCompatDelegate.onCreate(savedInstanceState);
        appCompatDelegate.setContentView(R.layout.activity_invest);
        appCompatDelegate.setSupportActionBar(toolbar);
        setUpCountDownTimer();

        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        backArrow = findViewById(R.id.top_layout_back_arrow);
        backArrow.setOnClickListener(v -> super.onBackPressed());
        if (getIntent() != null) {
            appInfo = getIntent().getParcelableExtra(INVEST_APP_PARAM);
        } else {
            throw new RuntimeException("App must be provided!");
        }
        attachPresenter();
        setUpRecycler(appInfo);
    }

    private void setUpCountDownTimer() {

    }

    private void setUpRecycler(AppInfo appInfo) {
        adapter = new InvestScreenAdapter(appInfo, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void attachPresenter() {
        presenter = new InvestPresenter();
        presenter.init(this);
    }


    @Override
    public void onInvestBtnClicked(String address) {
        Intent intent = new Intent(this, TransferActivity.class);
        intent.putExtra(RECIPIENT, address);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
