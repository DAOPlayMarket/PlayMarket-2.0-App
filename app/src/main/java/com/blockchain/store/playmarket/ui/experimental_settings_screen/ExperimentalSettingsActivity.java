package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.services.IpfsDaemonService;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExperimentalSettingsActivity extends AppCompatActivity {
    private static final String TAG = "ExperimentalSettingsAct";

    @BindView(R.id.download_ipfs_btn) Button downloadIpfs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental_settings);
        ButterKnife.bind(this);

        setItems();
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

}
