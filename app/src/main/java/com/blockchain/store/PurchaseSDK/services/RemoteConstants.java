package com.blockchain.store.PurchaseSDK.services;

public class RemoteConstants {
    /* PlayMarket results*/
    public static final String EXTRA_METHOD_RESULT = "method_extra_result";
    public static final String EXTRA_METHOD_ERROR = "method_extra_error";
    public static final String REMOTE_INTENT_NAME = "RemoteService";
    public static final String USER_NOT_PROVIDED_ERROR = "user_not_provided_error";
    public static final String UNKNOWN_HOST_EXCEPTION = "unknown_host_exception";

    /* Methods constants */
    public static final String EXTRA_METHOD_NAME = "method_name";
    public static final String METHOD_GET_BALANCE = "method_get_balance";
    public static final String METHOD_GET_ACCOUNT = "method_get_account";

    public static final String METHOD_TRANSACTION = "method_get_transaction";

    /* Transfer constants*/
    public static final String TRANSFER_TRANSFER_AMOUNT = "transfer_amount";
    public static final String TRANSFER_RECIPIENT_ADDRESS = "transfer_recipient_address";
    public static final String TRANSFER_PASSWORD = "transfer_password";
    public static final String TRANSFER_TRANSACTION_TYPE = "transfer_transaction_type";
    public static final String TRANSFER_PRICE = "transfer_price";
    public static final String TRANSFER_PACKAGE_NAME = "transfer_package_name";
    public static final String TRANSFER_OBJECT_ID = "transfer_object_id";

    /*Transaction types*/
    public static final int TRANSACTION_BUY = 0;
    public static final int TRANSACTION_BUY_OBJECT = 1;
    public static final int TRANSACTION_BUY_OBJECT_WITH_PRICE_CHECK = 2;
    public static final int TRANSACTION_BUY_SUB = 3;
    public static final int TRANSACTION_BUY_SUB_WITH_PRICE = 4;

    public static final String WRONG_PASSWORD_ERROR = "password_wrong_error";
}
