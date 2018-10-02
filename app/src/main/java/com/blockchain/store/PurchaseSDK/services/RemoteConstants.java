package com.blockchain.store.PurchaseSDK.services;

public class RemoteConstants {
    static final String EXTRA_METHOD_NAME = "method_name";
    static final String METHOD_GET_BALANCE = "method_get_balance";
    static final String METHOD_GET_ACCOUNT = "method_get_account";
    static final String METHOD_TRANSACTION = "method_get_transaction";
    static final String EXTRA_METHOD_RESULT = "method_extra_result";
    static final String EXTRA_METHOD_ERROR = "method_extra_error";
    static final String REMOTE_INTENT_NAME = "RemoteService";
    static final String USER_NOT_PROVIDED_ERROR = "user_not_provided_error";
    static final String UNKNOWN_HOST_EXCEPTION = "unknown_host_exception";

    static final String VALUE_TRANSFER_AMOUNT = "transfer_amount";
    static final String VALUE_RECIPIENT_ADDRESS = "recipient_address";
    static final String VALUE_PASSWORD = "user_password";

    enum TransactionTypes {
        BUY, BUY_OBJ, BUY_OBJ_WITH_PRICE, BUY_SUB, BUY_SUB_WITH_PRICE

    }

    public static final String WRONG_PASSWORD_ERROR = "password_wrong_error";
}
