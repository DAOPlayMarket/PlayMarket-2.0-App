package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.MyPackageManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppDetailActivity extends AppCompatActivity implements AppDetailContract.View {
    private static final String TAG = "AppDetailActivity";
    private static final String APP_EXTRA = "app_extra";
    @BindView(R.id.top_layout_app_name) TextView toolbarAppName;
    @BindView(R.id.top_layout_holder) LinearLayout top_layout_holder;
    @BindView(R.id.download_btn) Button download_btn;

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
        presenter.registerCallback(app);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        presenter.checkAppLoadState(app);
    }

    private void setViews() {
        toolbarAppName.setText(app.nameApp);
    }


    @OnClick(R.id.download_btn)
    public void download_btn() {
        if (download_btn.getText().toString().equalsIgnoreCase("OPEN")) {
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
    public void setState(Constants.DOWNLOAD_STATE stateInstalled) {
        if (stateInstalled == Constants.DOWNLOAD_STATE.STATE_INSTALLED) {
            download_btn.setText("OPEN");
        }
    }

    @Override
    public void setButtonText(String started) {
        runOnUiThread(() -> download_btn.setText(started));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
