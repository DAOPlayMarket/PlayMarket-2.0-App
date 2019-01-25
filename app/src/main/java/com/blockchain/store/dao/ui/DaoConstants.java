package com.blockchain.store.dao.ui;

import com.blockchain.store.playmarket.BuildConfig;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class DaoConstants {

    public static final String INFURA_WEBSOCKET_MAINNET = "wss://mainnet.infura.io/ws";

    private static final String Foundation_TEST = "0x7f3833e21c116C54cf157B3457896C7F5c85FE79"; // test
    private static final String DAO_TEST = "0x5e6188ad96f71f3586273ee6547632877c8fc9a4"; // test
    private static final String Repository_TEST = "0x3F94FAbdc7d84C00C84C9c87445de9d63566c6A3"; //test

    //    private static final String Repository_TEST = "0xa294558c55343a31c1fb3d443db2b7a1b8df2e80"; //test // pro
    private static final String PlayMarket_token_contract_test = "0x538106e553f5BA3298199C1998ba061922815A6c"; // test

    private static final String Foundation_mainnet = "0xb04A05905Dc94345DCAD4846664001d4818E16D2"; // mainnet
    private static final String DAO_mainnet = "0x550cbf0d9c62dbb7ff0ac8be1ad9fba4cb2ce49d"; // mainnet
    private static final String Repository_mainnet = "0xe789578025fC8DFEa42C6Cecb4D42C65fa5B4826"; //mainnet
    private static final String PlayMarket_token_contract_mainnet = "0xc1322d8ae3b0e2e437e0ae36388d0cfd2c02f1c9"; // mainnet

    public static final String Foundation = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? Foundation_mainnet : Foundation_TEST);
    public static final String DAO = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? DAO_mainnet : DAO_TEST);
    public static final String Repository = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? Repository_mainnet : Repository_TEST);
    public static final String PlayMarket_token_contract = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? PlayMarket_token_contract_mainnet : PlayMarket_token_contract_test);

    public static final String CRYPTO_DUEL_CONTRACT = "0xA265ac4A788Dc44d3C5CE94B8A8450A45d65be5d";


    public static final String CHANGE_OF_RULES = "0xe7a31acb9efc5512cc1896266cbc06335ca5e661547b177002b7bb5d8535871e";
    public static final String PAYMENT = "0xd4f43975feb89f48dd30cabbb32011045be187d1e11c8ea9faa43efc35282519";
    public static final String EXECUTE_PROPOSAL = "0x748f0810c68633063bb13f3c71e10cd1889925b3721d6e567114d0632574a396";
    public static final String VOTE = "0xc34f869b7ff431b034b7b9aea9822dac189a685e0b015c7d1be3add3f89128e8";
    public static final String ADD_PROPOSAL = "0x4c6647c4a4058c1a0bcd513fe2522706466700a104b82d4487d888f17ce5be13";

    public static final String TOKEN_HOLDER_USED_FOR_TEST = "0x4aC145CB8033649C3727BDe0b1BfAcfc3A3a9A9a";

    public static final String DAO_BLOCK_NUMBER = "6955312";
    public static final String DAO_BLOCK_NUMBER_HEX = Numeric.toHexStringWithPrefixSafe(new BigInteger(DAO_BLOCK_NUMBER));




    /* Contracts for test*/
}
