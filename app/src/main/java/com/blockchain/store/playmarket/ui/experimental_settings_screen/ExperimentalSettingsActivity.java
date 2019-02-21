package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.repositories.IpfsRepository;
import com.blockchain.store.playmarket.services.IpfsDaemonService;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExperimentalSettingsActivity extends AppCompatActivity implements ExperimentalSettingsContract.View {
    private static final String TAG = "ExperimentalSettingsAct";

    @BindView(R.id.top_layout_app_name) TextView screenTitle;
    @BindView(R.id.ipfs_status) TextView ipfsStatus;
    @BindView(R.id.ipfs_peers) TextView ipfsPeers;
    @BindView(R.id.ipfs_peer_id) TextView ipfs_peer_id;
    @BindView(R.id.ipfs_version) TextView ipfs_version;

    @BindView(R.id.ipfs_download_holder_checkbox) CheckBox ipfs_download_holder_checkbox;
    @BindView(R.id.ipfs_download_holder) LinearLayout ipfs_download_holder;
    @BindView(R.id.ipfs_auto_start_checkbox) CheckBox ipfs_auto_start_checkbox;
    @BindView(R.id.ipfs_auto_start_holder) LinearLayout ipfs_auto_start_holder;

    @BindView(R.id.init_ipfs_btn) Button init_ipfs_btn;
    @BindView(R.id.start_ipfs_btn) Button startIpfs;
    @BindView(R.id.stop_ipfs_btn) Button stopIpfs;
    @BindView(R.id.layout_holder) LinearLayout rootHolder;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.download_ipfs_btn) Button downloadIpfs;

    private ExperimentalSettingsPresenter presenter;
    private BroadcastReceiver broadcastReceiver;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental_settings);
        ButterKnife.bind(this);
        initViews();
        attachPresenter();
        setItems();
        initCheckBoxes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadCast();
    }

    private void initCheckBoxes() {
        Boolean isUserIpfs = Hawk.get(Constants.IS_USE_IPFS_TO_DOWNLOAD, true);
        Boolean isUseAutoStart = Hawk.get(Constants.IPFS_AUTO_START, false);

        Log.d(TAG, "is use ipfs: " + isUserIpfs);
        Log.d(TAG, "is use autoStart: " + isUseAutoStart);

        ipfs_download_holder_checkbox.setChecked(isUserIpfs);
        ipfs_auto_start_checkbox.setChecked(isUseAutoStart);

        ipfs_download_holder.setOnClickListener(v -> {
            ipfs_download_holder_checkbox.setChecked(!ipfs_download_holder_checkbox.isChecked());
            Hawk.put(Constants.IS_USE_IPFS_TO_DOWNLOAD, ipfs_download_holder_checkbox.isChecked());
        });

        ipfs_auto_start_holder.setOnClickListener(v -> {
            ipfs_auto_start_checkbox.setChecked(!ipfs_auto_start_checkbox.isChecked());
            Hawk.put(Constants.IPFS_AUTO_START, ipfs_auto_start_checkbox.isChecked());
        });
    }

    private void initViews() {
        screenTitle.setText(R.string.experimental_settings_title);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }

    private void attachPresenter() {
        presenter = new ExperimentalSettingsPresenter();
        presenter.init(this);
        presenter.loadIpfsData();
    }

    private void setItems() {
        if (IPFSDaemon.getInstance().isDaemonDownloaded()) {
            ipfsStatus.setText(getString(R.string.settings_offline));
            downloadIpfs.setVisibility(View.GONE);
            rootHolder.setVisibility(View.VISIBLE);
        } else {
            downloadIpfs.setVisibility(View.VISIBLE);
            rootHolder.setVisibility(View.GONE);
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

    @OnClick(R.id.open_config)
    public void onOpenConfigClicked() {
        startActivity(new Intent(this, IpfsConfigActivity.class));
    }

    @OnClick(R.id.start_ipfs_btn)
    public void start_ipfs_btn() {
        Intent intent = new Intent(this, IpfsDaemonService.class);
        startService(intent);

        stopIpfs.setEnabled(true);
        startIpfs.setEnabled(false);
    }

    @OnClick(R.id.stop_ipfs_btn)
    public void stop_ipfs_btn() {
        stopService(new Intent(this, IpfsDaemonService.class));
        IPFSDaemon.getIpfsProcess().destroy();

        stopIpfs.setEnabled(false);
        startIpfs.setEnabled(true);

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
        runOnUiThread(() -> downloadIpfs.setText(getString(R.string.download_and_run_ipfs)));
        ToastUtil.showToast(message);
    }

    @Override
    public void onIpfsError(Throwable throwable) {
//        rootHolder.setVisibility(View.VISIBLE);
        stopIpfs.setEnabled(false);
        startIpfs.setEnabled(true);
        ipfsStatus.setText(getString(R.string.settings_offline));
        ipfsPeers.setText(String.valueOf("-"));
        ipfs_peer_id.setText("-");
        ipfs_version.setText("-");

    }

    @Override
    public void onDataReady(IpfsRepository.IpfsRepositoryData ipfsRepositoryData) {
        stopIpfs.setEnabled(true);
        startIpfs.setEnabled(false);
        setViews(ipfsRepositoryData);
    }

    private void setViews(IpfsRepository.IpfsRepositoryData ipfsRepositoryData) {
        ipfsStatus.setText(getString(R.string.settings_online));
        ipfsPeers.setText(String.valueOf(ipfsRepositoryData.peers.peers.size()));
        ipfs_peer_id.setText(ipfsRepositoryData.config.identity.peerId);
        ipfs_version.setText(ipfsRepositoryData.version.version);

    }

    private void initBroadCast() {
        runnable = () -> presenter.loadIpfsData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
                if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(IpfsDaemonService.ACTION_START)) {
                    handler.postDelayed(runnable, 5500);
                } else {
                    handler.postDelayed(runnable, 500);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(IpfsDaemonService.ACTION_START);
        intentFilter.addAction(IpfsDaemonService.ACTION_STOP);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        handler.removeCallbacks(runnable);
    }
}
