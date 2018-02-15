package com.blockchain.store.playmarket.utilities;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class Constants {
    public enum APP_STATE {
        STATE_DOWNLOAD_STARTED, STATE_DOWNLOADING, STATE_DOWNLOADED_NOT_INSTALLED,
        STATE_DOWNLOAD_ERROR, STATE_INSTALLING, STATE_INSTALLED,
        STATE_INSTALL_FAIL, STATE_NOT_DOWNLOAD, STATE_NOT_PURCHASED,
        STATE_PURCHASE_UNDEFIEND, STATE_PURCHASE, STATE_UNKOWN
    }

    public static final String WELCOME_ACTIVITY_ADDRESS_EXTRA = "welcome_activity_address_extra";

    public static final int DOWNLOAD_APPS_PER_REQUEST = 40;
    public static final int GAS_LIMIT = 250000;
    public static final int RINKEBY_ID = 4;

    public static final String DOWNLOAD_SERVICE_APP_EXTRA = "download_service_app_extra";
    public static final String DOWNLOAD_SERVICE_URL_EXTRA = "download_service_url_extra";

    public static final String SEARCH_ACTIVITY_QUERY_EXTRA = "search_activity_query_extra";

    public static final String DOWNLOAD_SERVICE_PROGRESS_EXTRA = "download_service_progress_extra";
    public static final String DOWNLOAD_SERVICE_STATE_EXTRA = "download_service_state_extra";
    public static final String DOWNLOAD_SERVICE_APP_ID_EXTRA = "download_service_id_extra";
    public static final String DOWNLOAD_SERVICE_ACTION_KEY = "download_service_action_key";

    public static final String NODE_ADDRESS = "65D8706C1Ff1f9323272A818C22C1381de6D7556";

    public static final String PLAY_MARKET_ADDRESS = "0x3f2512Aee154F1D7d5EF3670ec0234AEe2FB60A9";
    public static final String INVEST_ADDRESS = "0xB268d40843224B539068606Fb9F0dbAFb430D765";

    public static final String SETTINGS_AUTOINSTALL_FLAG = "settings_autoinstall_flag";
}
