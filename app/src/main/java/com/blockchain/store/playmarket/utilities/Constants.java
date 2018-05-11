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

    public enum TRANSACTION_TYPE {
        BUY_APP {
            @Override
            public String toString() {
                return "buy-app";
            }
        },
        INVEST {
            @Override
            public String toString() {
                return "invest";
            }
        },
        TRANSFER {
            @Override
            public String toString() {
                return "transfer";
            }
        }


    }


    public static final String WELCOME_ACTIVITY_ADDRESS_EXTRA = "welcome_activity_address_extra";
    public static final String WELCOME_ACTIVITY_IS_LUANCHED_FROM_SETTINGS_EXTRA = "welcome_activity_is_launched_from_settings_extra";

    public static final int DOWNLOAD_APPS_PER_REQUEST = 40;
    public static final int GAS_LIMIT = 250000;
    public static final int RINKEBY_ID = 4;

    public static final String DOWNLOAD_SERVICE_APP_EXTRA = "download_service_app_extra";
    public static final String DOWNLOAD_SERVICE_URL_EXTRA = "download_service_url_extra";

    public static final String SEARCH_ACTIVITY_QUERY_EXTRA = "search_activity_query_extra";

    /* Transaction prefs */
    public static final String PLAY_MARKET_ADDRESS = "0x3f2512Aee154F1D7d5EF3670ec0234AEe2FB60A9";
    public static final String INVEST_ADDRESS = "0xB268d40843224B539068606Fb9F0dbAFb430D765";
    public static final String NODE_ADDRESS = "65D8706C1Ff1f9323272A818C22C1381de6D7556";

    /*Settings prefs*/
    public static final String SETTINGS_AUTOINSTALL_FLAG = "settings_autoinstall_flag";

    /* Library prefs*/
    public static final String DOWNLOADED_APPS_LIST = "downloaded_apps_list";

    public static final String YOUTUBE_KEY = "AIzaSyC7xC32ijRVgfRQmHPQGlhOHinLe1kNpJc";

    /*fragment tags*/

    public static final String NAV_VIEW_FRAGMENT_TAG = "nav_view_fragment_tag";
    public static final String ENCRYPTED_PASSWORD = "encrypted_account_password";
}
