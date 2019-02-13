package com.blockchain.store.playmarket.ui.settings_screen;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.repositories.CurrencyRepository;
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

    @BindView(R.id.settings_currency_holder_usd) LinearLayout settings_currency_holder_usd;
    @BindView(R.id.settings_currency_holder_rub) LinearLayout settings_currency_holder_rub;
    @BindView(R.id.setting_currency_rub) CheckBox currencyRub;
    @BindView(R.id.setting_currency_usd) CheckBox currencyUsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setViews();
        initViews();
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

        settings_currency_holder_usd.setOnClickListener(v -> {
            currencyUsd.setChecked(!currencyUsd.isChecked());
            if(currencyUsd.isChecked()){
                Hawk.put(Constants.SETTINGS_USER_CURRENCY, "USD");
            } else {
                Hawk.put(Constants.SETTINGS_USER_CURRENCY, "RUB");
            }
            currencyRub.setChecked(!currencyUsd.isChecked());

        });

        settings_currency_holder_rub.setOnClickListener(v -> {
            currencyRub.setChecked(!currencyRub.isChecked());
            if(currencyRub.isChecked()){
                Hawk.put(Constants.SETTINGS_USER_CURRENCY, "RUB");
            } else {
                Hawk.put(Constants.SETTINGS_USER_CURRENCY, "USD");
            }
            currencyUsd.setChecked(!currencyRub.isChecked());
        });


    }

    private void initViews() {
        toolbarTitle.setText(R.string.settings_title);
        autoInstallCheckbox.setChecked(Hawk.get(Constants.SETTINGS_AUTOINSTALL_FLAG, false));
        appUpdateNotificationCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_UPDATE_NOTIFICATION, true));
        playMarketUpdateNotificationCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION, true));
        transactionUpdateCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SHOW_TRANSACTION_UPDATE_NOTIFICATION, true));
        wifiOnlyCheckBox.setChecked(Hawk.get(Constants.SETTINGS_DOWNLOAD_ONLY_ON_WIFI, true));
        updateWhileChargingCheckBox.setChecked(Hawk.get(Constants.SETTINGS_SEARCH_FOR_UPDATE_ONLY_WHILE_CHARGING, true));
        String locale = Hawk.get(Constants.SETTINGS_USER_LOCALE);
        if (locale.equalsIgnoreCase("ru")) {
            langRus.setChecked(true);
            langEn.setChecked(false);
        } else {
            langEn.setChecked(true);
            langRus.setChecked(false);
        }
        if (CurrencyRepository.getUserCurrency().equalsIgnoreCase("RUB")) {
            currencyRub.setChecked(true);
            currencyUsd.setChecked(false);
        } else {
            currencyRub.setChecked(false);
            currencyUsd.setChecked(true);
        }
    }


    @OnClick(R.id.settings_autoinstall_holder)
    void onAutoInstallItemClicked() {
        autoInstallCheckbox.setChecked(!autoInstallCheckbox.isChecked());
        Hawk.put(Constants.SETTINGS_AUTOINSTALL_FLAG, autoInstallCheckbox.isChecked());
    }

    @OnClick(R.id.settings_lang_en_holder)
    void onEngHolderClicked() {
        langEn.setChecked(true);
        langRus.setChecked(false);
        overridePendingTransition(0, 0);
        Hawk.put(Constants.SETTINGS_USER_LOCALE, "US");
        LocaleUtils.setLocale(new Locale("en", "US"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();

    }

    @OnClick(R.id.settings_lang_rus_holder)
    void onRusHolderClicked() {
        langRus.setChecked(true);
        langEn.setChecked(false);
        overridePendingTransition(0, 0);
        Hawk.put(Constants.SETTINGS_USER_LOCALE, "RU");
        LocaleUtils.setLocale(new Locale("ru", "RU"));
        LocaleUtils.updateConfig(Application.getInstance(), getBaseContext().getResources().getConfiguration());
        LocaleUtils.refreshActivies();
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JobUtils.scheduleCheckUpdateJob(this, false);
    }

}
