package com.blockchain.store.playmarket.ui.settings_screen;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.LocaleUtils;
import com.orhanobut.hawk.Hawk;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.setting_autoinstall_checkbox) CheckBox autoInstallCheckbox;
    @BindView(R.id.setting_lang_en) CheckBox langEn;
    @BindView(R.id.setting_lang_rus) CheckBox langRus;

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

    @OnClick(R.id.settings_lang_en_holder)
    void onEngHolderClicked() {
        this.finish();
        LocaleUtils.setLocale(new Locale("en"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();

    }

    @OnClick(R.id.settings_lang_rus_holder)
    void onRusHolderClicked() {
        this.finish();
        LocaleUtils.setLocale(new Locale("ru"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }


}
