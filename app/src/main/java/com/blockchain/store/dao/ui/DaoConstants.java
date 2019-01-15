package com.blockchain.store.dao.ui;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class DaoConstants {

    public static final String INFURA_WEBSOCKET_MAINNET = "wss://mainnet.infura.io/ws";

    /* Contracts for release */
    public static final String Foundation = "0xb04A05905Dc94345DCAD4846664001d4818E16D2";
    public static final String Repository = "0xe789578025fC8DFEa42C6Cecb4D42C65fa5B4826";
    public static final String DAO = "0x550cbf0d9c62dbb7ff0ac8be1ad9fba4cb2ce49d";

    public static final String TOKEN_HOLDER_USED_FOR_TEST = "0x4aC145CB8033649C3727BDe0b1BfAcfc3A3a9A9a";

    public static final String DAO_BLOCK_NUMBER = "6955312";
    public static final String DAO_BLOCK_NUMBER_HEX = Numeric.toHexStringWithPrefixSafe(new BigInteger(DAO_BLOCK_NUMBER));



    /* Contracts for test*/
}
