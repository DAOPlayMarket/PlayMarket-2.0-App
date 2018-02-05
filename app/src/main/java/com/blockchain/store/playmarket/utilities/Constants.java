package com.blockchain.store.playmarket.utilities;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class Constants {
    public enum APP_STATE {
        STATE_DOWNLOAD_STARTED, STATE_DOWNLOADING, STATE_DOWNLOADED_NOT_INSTALLED, STATE_DOWNLOAD_ERROR, STATE_INSTALLING, STATE_INSTALLED, STATE_INSTALL_FAIL, STATE_NOT_DOWNLOAD, STATE_UNKOWN
    }

    public static final String WELCOME_ACTIVITY_ADDRESS_EXTRA = "welcome_activity_address_extra";

    public static final int DOWNLOAD_APPS_PER_REQUEST = 40;

    public static final String DOWNLOAD_SERVICE_APP_EXTRA = "download_service_app_extra";
    public static final String DOWNLOAD_SERVICE_URL_EXTRA = "download_service_url_extra";

    public static final String DOWNLOAD_SERVICE_PROGRESS_EXTRA = "download_service_progress_extra";
    public static final String DOWNLOAD_SERVICE_STATE_EXTRA = "download_service_state_extra";
    public static final String DOWNLOAD_SERVICE_APP_ID_EXTRA = "download_service_id_extra";

    public static final String DOWNLOAD_SERVICE_ACTION_KEY = "download_service_action_key";
}
