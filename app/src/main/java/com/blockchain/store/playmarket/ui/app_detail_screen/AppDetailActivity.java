package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppDetailActivity extends AppCompatActivity implements AppDetailContract.View {
    private static final String TAG = "AppDetailActivity";
    private static final String APP_EXTRA = "app_extra";

    @BindView(R.id.top_layout_app_name) TextView toolbarAppName;
    @BindView(R.id.top_layout_holder) LinearLayout top_layout_holder;
    @BindView(R.id.download_btn) Button downloadBtn;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.main_layout_holder) View mainLayoutHolder;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.image_icon) ImageView imageIcon;
    @BindView(R.id.app_name) TextView appName;
    @BindView(R.id.app_description) TextView appDescription;

    private AppDetailPresenter presenter;
    private App app;

    public static void start(Context context, App app) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_EXTRA, app);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            app = getIntent().getParcelableExtra(APP_EXTRA);
        }
        attachPresenter();
        setViews();
    }

    private void attachPresenter() {
        presenter = new AppDetailPresenter();
        presenter.init(this);
        presenter.getDetailedInfo(app);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkAppLoadState(app);
    }

    private void setViews() {
        Glide.with(this).load(app.getIconUrl()).into(imageIcon);
        toolbarAppName.setText(app.nameApp);
        appName.setText(app.nameApp);
    }

    @Override
    public void onDetailedInfoReady(AppInfo appInfo) {
        mainLayoutHolder.setVisibility(View.VISIBLE);
        appDescription.setText(appInfo.description);
    }

    @Override
    public void setButtonText(String string) {
        runOnUiThread(() -> downloadBtn.setText(string));

    }

    @OnClick(R.id.download_btn)
    public void download_btn() {
        if (downloadBtn.getText().toString().equalsIgnoreCase("OPEN")) {
            new MyPackageManager().openAppByPackage(app.hash);
            return;
        }
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA, app);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA, app.getDownloadLink());
        startService(intent);
        presenter.appDownloadClicked(app);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }


    @Override
    public void onDetailedInfoFailed(Throwable throwable) {
        mainLayoutHolder.setVisibility(View.GONE);
        showErrorView(true);
    }

    @Override
    public void setProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    @Override
    public void showErrorView(boolean isShow) {
        errorHolder.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorViewRepeatClicked() {
        presenter.getDetailedInfo(app);
    }


}
