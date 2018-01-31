package com.blockchain.store.playmarket.utilities;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class Constants {
    public enum DOWNLOAD_STATE {
        STATE_DOWNLOAD, STATE_DOWNLOAD_SUCCESSFUL, STATE_DOWNLOAD_ERROR, STATE_INSTALLING, STATE_INSTALLED, STATE_INSTALL_FAIL
    }

    public static final String WELCOME_ACTIVITY_ADDRESS_EXTRA = "welcome_activity_address_extra";

    public static final int DOWNLOAD_APPS_PER_REQUEST = 30;

    public static final String DOWNLOAD_SERVICE_APP_EXTRA = "download_service_app_extra";
    public static final String DOWNLOAD_SERVICE_URL_EXTRA = "download_service_url_extra";

    public static final String DOWNLOAD_SERVICE_PROGRESS_EXTRA = "download_service_progress_extra";
    public static final String DOWNLOAD_SERVICE_STATE_EXTRA = "download_service_state_extra";
    public static final String DOWNLOAD_SERVICE_APP_ID_EXTRA = "download_service_id_extra";

    public static final String DOWNLOAD_SERVICE_ACTION_KEY = "download_service_action_key";
}
