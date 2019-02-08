package com.blockchain.store.playmarket.ui.settings_screen;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

    @BindView(R.id.settings_enable_update_notification_checkbox)
    CheckBox appUpdateNotificationCheckBox;
    @BindView(R.id.settings_enable_update_notification)
    LinearLayout appUpdateNotificationHolder;

    @BindView(R.id.settings_enable_self_update_notification_checkbox)
    CheckBox playMarketUpdateNotificationCheckBox;
    @BindView(R.id.settings_enable_self_update_notification)
    LinearLayout playMarketUpdateNotificationHolder;

    @BindView(R.id.settings_enable_transaction_update_notification_checkbox)
    CheckBox transactionUpdateCheckBox;
    @BindView(R.id.settings_enable_transaction_update_notification)
    LinearLayout transactionUpdateHolder;

    @BindView(R.id.setting_wifi_only_checkbox)
    CheckBox wifiOnlyCheckBox;
    @BindView(R.id.setting_wifi_only)
    LinearLayout wifiOnlyHolder;

    @BindView(R.id.setting_check_for_update_while_charing_checkbox)
    CheckBox updateWhileChargingCheckBox;
    @BindView(R.id.setting_check_for_update_while_charing)
    LinearLayout updateWhileChargingHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
        setViews();

    }

    private void setViews() {
        appUpdateNotificationHolder.setOnClickListener(v -> {
            appUpdateNotificationCheckBox.setChecked(!appUpdateNotificationCheckBox.isChecked());
            Hawk.put(Constants.SETTINGS_SHOW_UPDATE_NOTIFICATION, appUpdateNotificationCheckBox.isChecked());
        });
        playMarketUpdateNotificationHolder.setOnClickListener(v -> {
            playMarketUpdateNotificationCheckBox.setChecked(!playMarketUpdateNotificationCheckBox.isChecked());
            Hawk.put(Constants.SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION, playMarketUpdateNotificationCheckBox.isChecked());
        });
        transactionUpdateHolder.setOnClickListener(v -> {
            transactionUpdateCheckBox.setChecked(!transactionUpdateCheckBox.isChecked());
            Hawk.put(Constants.SETTINGS_SHOW_TRANSACTION_UPDATE_NOTIFICATION, transactionUpdateCheckBox.isChecked());
        });
        wifiOnlyHolder.setOnClickListener(v -> {
            wifiOnlyCheckBox.setChecked(!wifiOnlyCheckBox.isChecked());
            Hawk.put(Constants.SETTINGS_DOWNLOAD_ONLY_ON_WIFI, wifiOnlyCheckBox.isChecked());
        });
        updateWhileChargingHolder.setOnClickListener(v -> {
            updateWhileChargingCheckBox.setChecked(!updateWhileChargingCheckBox.isChecked());
            Hawk.put(Constants.SETTINGS_SEARCH_FOR_UPDATE_ONLY_WHILE_CHARGING, updateWhileChargingCheckBox.isChecked());
        });
    }

    private void initViews() {
        toolbarTitle.setText(R.string.settings_title);
        autoInstallCheckbox.setChecked(Hawk.get(Constants.SETTINGS_AUTOINSTALL_FLAG, true));
        appUpdateNotificationCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_UPDATE_NOTIFICATION, true));
        playMarketUpdateNotificationCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION, true));
        transactionUpdateCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_TRANSACTION_UPDATE_NOTIFICATION, true));
        wifiOnlyCheckBox.setChecked(Hawk.get(Constants.SETTINGS_DOWNLOAD_ONLY_ON_WIFI, true));
        updateWhileChargingCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SEARCH_FOR_UPDATE_ONLY_WHILE_CHARGING, true));
        if (Hawk.contains(Constants.SETTINGS_USER_LOCALE)) {

        }
        langEn.setChecked(Hawk.get(Constants.SETTINGS_USER_LOCALE, false));
        langRus.setChecked(Hawk.get(Constants.SETTINGS_USER_LOCALE, false));
    }


    @OnClick(R.id.settings_autoinstall_holder)
    void onAutoInstallItemClicked() {
        autoInstallCheckbox.setChecked(!autoInstallCheckbox.isChecked());
        Hawk.put(Constants.SETTINGS_AUTOINSTALL_FLAG, autoInstallCheckbox.isChecked());
    }

    @OnClick(R.id.settings_lang_en_holder)
    void onEngHolderClicked() {
//        this.finish();

        overridePendingTransition(0, 0);
        Hawk.put(Constants.SETTINGS_USER_LOCALE, "en");
        LocaleUtils.setLocale(new Locale("en"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();

    }

    @OnClick(R.id.settings_lang_rus_holder)
    void onRusHolderClicked() {
//        this.finish();

        overridePendingTransition(0, 0);
        Hawk.put(Constants.SETTINGS_USER_LOCALE, "ru");
        LocaleUtils.setLocale(new Locale("ru"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }


}
