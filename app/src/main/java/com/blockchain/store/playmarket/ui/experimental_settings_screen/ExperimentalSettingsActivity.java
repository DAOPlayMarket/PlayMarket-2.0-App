package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import android.content.Intent;
import android.os.Bundle;
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
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;
import com.orhanobut.hawk.Hawk;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExperimentalSettingsActivity extends BaseActivity implements ExperimentalSettingsContract.View {
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

    @BindView(R.id.ipfs_safe_mode_checkbox) CheckBox ipfs_safe_mode_checkbox;
    @BindView(R.id.ipfs_safe_mode_holder) LinearLayout ipfs_safe_mode_holder;

    @BindView(R.id.init_ipfs_btn) Button init_ipfs_btn;
    @BindView(R.id.start_ipfs_btn) Button startIpfs;
    @BindView(R.id.stop_ipfs_btn) Button stopIpfs;
    @BindView(R.id.layout_holder) LinearLayout rootHolder;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.download_ipfs_btn) Button downloadIpfs;
    @BindView(R.id.bottom_buttons_holder) View buttonsHolder;

    private ExperimentalSettingsPresenter presenter;

    private TimerTask myTimerTask;
    private Timer mTimer;
    private boolean isDaemonStartClicked = false;
    private IpfsRepository ipfsRepository = new IpfsRepository();
    private boolean isDownloadStarted = false;

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
        ipfs_safe_mode_checkbox.setChecked(Hawk.get(Constants.IPFS_SAFE_MODE, true));

        ipfs_download_holder.setOnClickListener(v -> {
            ipfs_download_holder_checkbox.setChecked(!ipfs_download_holder_checkbox.isChecked());
            Hawk.put(Constants.IS_USE_IPFS_TO_DOWNLOAD, ipfs_download_holder_checkbox.isChecked());
        });

        ipfs_auto_start_holder.setOnClickListener(v -> {
            ipfs_auto_start_checkbox.setChecked(!ipfs_auto_start_checkbox.isChecked());
            Hawk.put(Constants.IPFS_AUTO_START, ipfs_auto_start_checkbox.isChecked());
        });


        ipfs_safe_mode_holder.setOnClickListener(v -> {
            ipfs_safe_mode_checkbox.setChecked(!ipfs_safe_mode_checkbox.isChecked());
            Hawk.put(Constants.IPFS_SAFE_MODE, ipfs_safe_mode_checkbox.isChecked());
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
        if (IPFSDaemon.getInstance().isNewVersionAvailable()) {
            downloadIpfs.setText(getString(R.string.update) + " IPFS");
            downloadIpfs.setVisibility(View.VISIBLE);
            rootHolder.setVisibility(View.VISIBLE);
        }


    }

    @OnClick(R.id.download_ipfs_btn)
    public void download_ipfs_btn() {
        onStopIpfsClicked();
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
        isDaemonStartClicked = true;
        Intent intent = new Intent(this, IpfsDaemonService.class);
        startService(intent);

        stopIpfs.setEnabled(false);
        startIpfs.setEnabled(false);
    }

    @OnClick(R.id.stop_ipfs_btn)
    public void onStopIpfsClicked() {
        stopService(new Intent(this, IpfsDaemonService.class));
        this.ipfsRepository.shuwDownIpfs();
        stopIpfs.setEnabled(false);
        startIpfs.setEnabled(false);

    }

    @Override
    public void reportDownloadUpdate(int i) {
        runOnUiThread(
                () -> {
                    downloadIpfs.setText(String.valueOf(i) + " %");
                    buttonsHolder.setVisibility(View.GONE);
                }
        );
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

        ipfsStatus.setText(getString(R.string.settings_offline));
        ipfsPeers.setText(String.valueOf("-"));
        ipfs_peer_id.setText("-");
        ipfs_version.setText("-");
        if (!isDaemonStartClicked) {
            stopIpfs.setEnabled(false);
            startIpfs.setEnabled(true);
        }


    }

    @Override
    public void onDataReady(IpfsRepository.IpfsRepositoryData ipfsRepositoryData) {

        isDaemonStartClicked = false;
        stopIpfs.setEnabled(true);
        startIpfs.setEnabled(false);
        setViews(ipfsRepositoryData);
    }

    private void setViews(IpfsRepository.IpfsRepositoryData ipfsRepositoryData) {
        buttonsHolder.setVisibility(View.VISIBLE);
        ipfsStatus.setText(getString(R.string.settings_online));
        ipfsPeers.setText(String.valueOf(ipfsRepositoryData.peers.peers.size()));
        ipfs_peer_id.setText(ipfsRepositoryData.config.identity.peerId);
        ipfs_version.setText(ipfsRepositoryData.version.version);

    }

    private void initBroadCast() {
        mTimer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                presenter.loadIpfsData();
            }

        };
        mTimer.scheduleAtFixedRate(myTimerTask, 1000, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimer.cancel();
        mTimer.purge();
    }
}
