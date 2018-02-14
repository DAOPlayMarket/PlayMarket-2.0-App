package com.blockchain.store.playmarket.ui.settings_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.setting_autoinstall_checkbox) CheckBox autoInstallCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbarTitle.setText(R.string.settings_title);
        if (Hawk.contains(Constants.SETTINGS_AUTOINSTALL_FLAG)) {
            autoInstallCheckbox.setChecked(Hawk.get(Constants.SETTINGS_AUTOINSTALL_FLAG));
        }
    }

    @OnClick(R.id.settings_autoinstall_holder)
    void onAutoInstallItemClicked() {
        autoInstallCheckbox.setChecked(!autoInstallCheckbox.isChecked());
        Hawk.put(Constants.SETTINGS_AUTOINSTALL_FLAG, autoInstallCheckbox.isChecked());
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }


}
