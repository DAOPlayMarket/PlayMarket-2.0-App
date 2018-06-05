package com.blockchain.store.playmarket.ui.invest_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.interfaces.InvestAdapterCallback;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvestActivity extends YouTubeBaseActivity implements InvestContract.View, InvestAdapterCallback {
    private static final String TAG = "InvestActivity";
    private static final String INVEST_APP_PARAM = "invest_app_param";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private AppInfo appInfo;
    private InvestPresenter presenter;
    private InvestScreenAdapter adapter;

    public static void start(Context context, AppInfo appInfo) {
        Intent starter = new Intent(context, InvestActivity.class);
        starter.putExtra(INVEST_APP_PARAM, appInfo);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            appInfo = getIntent().getParcelableExtra(INVEST_APP_PARAM);
        } else {
            throw new RuntimeException("App must be provided!");
        }
        attachPresenter();
        setUpRecycler();
    }

    private void setUpRecycler() {
        adapter = new InvestScreenAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void attachPresenter() {
        presenter = new InvestPresenter();
        presenter.init(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @OnClick(R.id.top_layout_back_arrow)
    public void onBackArrowClicked() {
        super.onBackPressed();
    }

    @Override
    public void onInvestBtnClicked(String address) {
        startActivity(new Intent(this, TransferActivity.class));
    }

}
