package com.blockchain.store.playmarket.utilities;

import com.blockchain.store.playmarket.BuildConfig;

import static com.blockchain.store.playmarket.api.RestApi.SERVER_ENDPOINT_WITHOUT_POST;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class Constants {

    public static boolean IS_MAINNET_BUILD = BuildConfig.BUILD_TYPE.contentEquals("mainnet");

    /* Job service extra*/
    public static String JOB_HASH_EXTRA = "job_hash_extra";
    public static String JOB_APP_ID = "job_app_id";
    public static String JOB_APP_NODE = "job_app_node";
    public static String JOB_HASH_FIRST_IS_COMPLETED = "job_hash_first_is_completed";
    public static String JOB_SECOND_HASH_EXTRA = "job_second_hash_extra";
    public static String JOB_SECOND_RAW_TX = "JOB_HASH_SECOND_IS_COMPLETED";
    public static String JOB_TRANSACTION_TYPE_ORDINAL = "transaction_type_ordinal";
    public static int IPFS_NOTIFICATION_ID = 55555;

    public enum APP_STATE {
        STATE_DOWNLOAD_STARTED, STATE_DOWNLOADING, STATE_DOWNLOADED_NOT_INSTALLED,
        STATE_DOWNLOAD_ERROR, STATE_INSTALLING, STATE_INSTALLED,
        STATE_INSTALL_FAIL, STATE_NOT_DOWNLOAD, STATE_NOT_PURCHASED,
        STATE_PURCHASE_UNDEFIEND, STATE_PURCHASE, STATE_UNKNOWN, STATE_HAS_UPDATE, STATE_UPDATE_DOWNLOADED_NOT_INSTALLED, STATE_UPDATING
    }

    public enum TransactionTypes {
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
        },
        TRANSFER_TOKEN {
            @Override
            public String toString() {
                return "transfer token";
            }
        },
        SEND_REVIEW {
            @Override
            public String toString() {
                return "send review";
            }
        },
        SEND_INTO_REPOSITORY {
            @Override
            public String toString() {
                return "send into repository";
            }
        },
        WITHDRAW_TOKEN {
            @Override
            public String toString() {
                return "withdraw tokens";
            }
        },
        GET_DIVIDENDS {
            @Override
            public String toString() {
                return "get dividends";
            }
        },
        CREATE_PROPOSAL {
            @Override
            public String toString() {
                return "create proposal";
            }
        },
        VOTE_FOR_PROPOSAL {
            @Override
            public String toString() {
                return "vote for proposal";
            }
        },
        EXECUTE_RPOPOSAL {
            @Override
            public String toString() {
                return "execute for proposal";
            }
        },


    }

    public static final String OPEN_MY_APPS_EXTRA = "open_my_apps_extra";

    public enum TransactionStatus {
        SUCCEES, FAILED, PENDING, ALL
    }

    public static final String WELCOME_ACTIVITY_ADDRESS_EXTRA = "welcome_activity_address_extra";
    public static final String WELCOME_ACTIVITY_IS_LUANCHED_FROM_SETTINGS_EXTRA = "welcome_activity_is_launched_from_settings_extra";

    public static final int GAS_LIMIT_ADDITION = 21_000;

    public static final int DOWNLOAD_APPS_PER_REQUEST = 40;
    public static final int GAS_LIMIT = 200_000;

    public static final int USER_ETHERSCAN_ID = (IS_MAINNET_BUILD ? 1 : 4);


    public static final String DOWNLOAD_SERVICE_APP_EXTRA = "download_service_app_extra";
    public static final String DOWNLOAD_SERVICE_URL_EXTRA = "download_service_url_extra";
    public static final String DOWNLOAD_SERVICE_FORCE_INSTALL = "download_service_force_install";
    public static final String DOWNLOAD_SERVICE_BITMAP_EXTRA = "download_service_bitmap_extra";

    public static final String NOTIFICATION_UPDATE_GROUP = "notification_update_group";

    public static final String SEARCH_ACTIVITY_QUERY_EXTRA = "search_activity_query_extra";

    /* Transaction prefs */
    public static final String PLAY_MARKET_ADDRESS_TESTNET = "0x5245b2014e888b93418688cb9c9477b1b65c4630";
    public static final String PLAY_MARKET_ADDRESS_MAINNET = "0x839ea7b29be3075a463cab3b065a828d8c37cfaf";
    public static final String PLAY_MARKET_ADDRESS = (IS_MAINNET_BUILD ? PLAY_MARKET_ADDRESS_MAINNET : PLAY_MARKET_ADDRESS_TESTNET);

    public static final String ETHER_SCAN_TX_URL_TESTNET = "https://rinkeby.etherscan.io/tx/";
    public static final String ETHER_SCAN_TX_URL_MAINNET = "https://etherscan.io/tx";
    public static final String ETHER_SCAN_TX_URL = (IS_MAINNET_BUILD ? ETHER_SCAN_TX_URL_MAINNET : ETHER_SCAN_TX_URL_TESTNET);

    public static final String CRYPTO_DUEL_CONTRACT_CROWDSALE = "0x2ab54369795266fc84e12270A0219d2e7d21F70f";
    public static final String CRYPTO_DUEL_CONTRACT = "0x869eb8a1a479a80f9907673eae8336625dc3e526";
    public static final String CRYPTO_DUEL_CONTRACT_FOR_ADVER_BUDGET = "0xbc82729a3a5063465f66b54d9a1e18496529c532";

    public static final String EMPTY_ADDRESS = "0x0000000000000000000000000000000000000000";

    public static final String PM_TOKEN_ADDRESS = "0xcf42d66172f8fdec2b42515f0f0289049e4011c2";


    public static final String TRANSACTION_MODEL_KEY = "transaction_model_key";

    public static final String LOCAL_TOKEN_KEYS = "local_token_keys";

    public static final String LOCAL_TOKEN_KEYS_V2 = "local_token_keysv2";

    /* Library prefs*/
    public static final String DOWNLOADED_APPS_LIST = "downloaded_apps_list";

    public static final String YOUTUBE_KEY = "AIzaSyC7xC32ijRVgfRQmHPQGlhOHinLe1kNpJc";

    /*fragment tags*/

    public static final String NAV_VIEW_FRAGMENT_TAG = "nav_view_fragment_tag";
    public static final String ENCRYPTED_PASSWORD = "encrypted_account_password";
    public static final String ENCRYPTED_PASSWORD_MAP = "encrypted_account_password_map";
    public static final String USER_ACCOUNT_POSITION = "user_account_position";

    public static final String USER_DOWNLOAD_APPS_MAP = "user_download_apps_map";


    /*Social titles*/
    public static final String social_google_plus = "google-plus";
    public static final String social_facebook = "facebook";
    public static final String social_linkedin = "linkedin";
    public static final String social_twitter = "twitter";
    public static final String social_instagram = "instagram";
    public static final String social_vk = "vk";
    public static final String social_youtube = "youtube";
    public static final String social_telegram = "telegram";
    public static final String social_git = "git";

    public static final int USER_REVIEW_EXPAND_ANIMATION_MILLIS = 120;
    public static final String INFURA_TOKEN = "mS4OSWU5XZxDi9R75Dc7";


    /*Token list activity*/
    public static final String TOKEN_ARGS = "token_args";

    /* URLs*/
    public static final String TOKEN_URL = "https://tokens.playmarket.io";
    public static final String PAX_URL_MAINNET = "https://dex.playmarket.io/";
    public static final String PAX_URL_TESTNET = "https://testdex.playmarket.io/";
    public static final String DOWNLOAD_APP_URL = SERVER_ENDPOINT_WITHOUT_POST + "/api/download-app?idApp=";
    public static final String PAX_URL = (IS_MAINNET_BUILD ? PAX_URL_MAINNET : PAX_URL_TESTNET);

    /*Settings prefs*/
    public static final String CURRENT_CURRENCY = "current_currency";
    public static final String SETTINGS_AUTOINSTALL_FLAG = "settings_autoinstall_flag";
    public static final String BASE_URL = "base_url_extra";
    public static final String DOWNLOAD_NEW_VERSION_WITHOUT_PROMPT = "download_new_version_without_prompt";
    public static final String LAST_KNOWN_NODE_ADDRESS = "last_known_node_address";


    public static final String SETTINGS_SHOW_UPDATE_NOTIFICATION = "SETTINGS_SHOW_UPDATE_NOTIFICATION";
    public static final String SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION = "SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION";
    public static final String SETTINGS_SHOW_TRANSACTION_UPDATE_NOTIFICATION = "SETTINGS_SHOW_TRANSACTION_UPDATE_NOTIFICATION";
    public static final String SETTINGS_DOWNLOAD_ONLY_ON_WIFI = "download_only_on_wifi";
    public static final String SETTINGS_SEARCH_FOR_UPDATE_ONLY_WHILE_CHARGING = "search_for_update_only_while_charging";

    public static final String SETTINGS_USER_LOCALE = "settings_user_locale";
    public static final String SETTINGS_USER_CURRENCY = "settings_user_currency";

    /*Experimental prefs*/

    public static final String IS_USE_IPFS_TO_DOWNLOAD = "is_use_ipfs_to_download";
    public static final String IPFS_AUTO_START = "is_use_auto_start";
    public static final String IPFS_SAFE_MODE = "ipfs_safe_mode";
    public static final String IPFS_LAST_KNOWN_HASH_KEY = "last_known_hash";

//    public static final String LAST_KNOWN_IPFS_HASH = "QmRQYQgw3qtfhaXm9vSdvbXfUEBwyAifJ72hSaPcUH4fZX";
    public static final String LAST_KNOWN_IPFS_HASH = "QmejXkNPNFqgX4i6R8cakgvRbpPTqdfJYVsScdF5zg7eTj";
    public static final String IPFS_BINARIES_PATH_ARM = "https://ipfs.io/ipfs/" + LAST_KNOWN_IPFS_HASH + "/arm.zip";
    public static final String IPFS_BINARIES_PATH_X86 = "https://ipfs.io/ipfs/" + LAST_KNOWN_IPFS_HASH + "/x86.zip";


}
