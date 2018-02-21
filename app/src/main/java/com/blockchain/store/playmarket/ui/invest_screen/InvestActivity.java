package com.blockchain.store.playmarket.ui.invest_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
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

public class InvestActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, InvestContract.View {
    private static final String TAG = "InvestActivity";
    private static final String INVEST_APP_PARAM = "invest_app_param";

    @BindView(R.id.top_layout_back_arrow) ImageView top_layout_back_arrow;
    @BindView(R.id.top_layout_app_name) TextView top_layout_app_name;
    @BindView(R.id.top_layout_holder) LinearLayout top_layout_holder;
    @BindView(R.id.app_logo) ImageView app_logo;
    @BindView(R.id.invest_btn) Button invest_btn;
    @BindView(R.id.youtube) YouTubePlayerView youtube;

    private AppInfo appInfo;
    private InvestPresenter presenter;

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
        youtube.initialize(Constants.YOUTUBE_KEY, this);
    }

    private void attachPresenter() {
        presenter = new InvestPresenter();
        presenter.init(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @OnClick(R.id.invest_btn)
    public void invest_btn() {
        new DialogManager().showInvestDialog(appInfo, this, investAmount -> presenter.onInvestClicked(appInfo, investAmount));
    }

    @OnClick(R.id.top_layout_back_arrow)
    public void onBackArrowClicked() {
        super.onBackPressed();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo("lG0Ys-2d4MA");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse) {
        ToastUtil.showToast(R.string.successfully_paid);
    }

    @Override
    public void onPurchaseError(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }
}
