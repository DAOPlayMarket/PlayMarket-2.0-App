package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.services.IpfsDaemonService;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExperimentalSettingsActivity extends AppCompatActivity implements ExperimentalSettingsContract.View {
    private static final String TAG = "ExperimentalSettingsAct";

    @BindView(R.id.top_layout_app_name) TextView screenTitle;
    @BindView(R.id.ipfs_status) TextView ipfsStatus;
    @BindView(R.id.ipfs_peers) TextView ipfsPeers;
    @BindView(R.id.ipfs_download_holder_checkbox) CheckBox ipfs_download_holder_checkbox;
    @BindView(R.id.ipfs_download_holder) LinearLayout ipfs_download_holder;
    @BindView(R.id.ipfs_auto_start_checkbox) CheckBox ipfs_auto_start_checkbox;
    @BindView(R.id.ipfs_auto_start_holder) LinearLayout ipfs_auto_start_holder;
    @BindView(R.id.init_ipfs_btn) Button init_ipfs_btn;
    @BindView(R.id.start_ipfs_btn) Button start_ipfs_btn;
    @BindView(R.id.stop_ipfs_btn) Button stop_ipfs_btn;
    @BindView(R.id.layout_holder) LinearLayout rootHolder;

    @BindView(R.id.download_ipfs_btn) Button downloadIpfs;

    ExperimentalSettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental_settings);
        ButterKnife.bind(this);
        setViews();
        attachPresenter();
        setItems();
    }

    private void setViews() {
        screenTitle.setText(R.string.experimental_settings_title);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }

    private void attachPresenter() {
        presenter = new ExperimentalSettingsPresenter();
        presenter.init(this);
    }

    private void setItems() {
        if (IPFSDaemon.getInstance().isDaemonDownloaded()) {
            downloadIpfs.setVisibility(View.GONE);
        } else {
            downloadIpfs.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.download_ipfs_btn)
    public void download_ipfs_btn() {
        IPFSDaemon.getInstance().downloadDaemon();
        presenter.loadState();
    }

    @OnClick(R.id.init_ipfs_btn)
    public void init_ipfs_btn() {
        IPFSDaemon.getInstance().initDaemon();
    }

    @OnClick(R.id.start_ipfs_btn)
    public void start_ipfs_btn() {
        Intent intent = new Intent(this, IpfsDaemonService.class);
        startService(intent);
    }

    @OnClick(R.id.stop_ipfs_btn)
    public void stop_ipfs_btn() {
        stopService(new Intent(this, IpfsDaemonService.class));
        IPFSDaemon.getIpfsProcess().destroy();
    }

    @Override
    public void reportDownloadUpdate(int i) {
        runOnUiThread(() -> downloadIpfs.setText(String.valueOf(i)));
    }

    @Override
    public void reportDownloadEndWithSuccess() {
        runOnUiThread(() -> downloadIpfs.setVisibility(View.GONE));

    }

    @Override
    public void reportDownloadError(String message) {
        ToastUtil.showToast(message);
    }
}
