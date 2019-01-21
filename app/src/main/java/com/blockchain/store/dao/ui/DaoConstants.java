package com.blockchain.store.dao.ui;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class DaoConstants {

    public static final String INFURA_WEBSOCKET_MAINNET = "wss://mainnet.infura.io/ws";

    /* Contracts for release */
    public static final String Foundation = "0x7f3833e21c116C54cf157B3457896C7F5c85FE79"; // test
    public static final String DAO = "0x6ed82ffe4c0db4f9b0d52ef943a4e75b4159d85a"; // test
    public static final String Repository = "0xa294558c55343a31c1fb3d443db2b7a1b8df2e80"; //test


//    public static final String Repository = "0xe789578025fC8DFEa42C6Cecb4D42C65fa5B4826"; //mainnet
    //    public static final String Foundation = "0xb04A05905Dc94345DCAD4846664001d4818E16D2"; // mainnet
//    public static final String DAO = "0x550cbf0d9c62dbb7ff0ac8be1ad9fba4cb2ce49d"; // mainnet



//    public static final String PlayMarket_token_contract="0xc1322d8ae3b0e2e437e0ae36388d0cfd2c02f1c9"; // mainnet
    public static final String PlayMarket_token_contract="0x538106e553f5BA3298199C1998ba061922815A6c"; // test


    public static final String CHANGE_OF_RULES = "0xe7a31acb9efc5512cc1896266cbc06335ca5e661547b177002b7bb5d8535871e";
    public static final String PAYMENT = "0xd4f43975feb89f48dd30cabbb32011045be187d1e11c8ea9faa43efc35282519";
    public static final String PROPOSAL_TALLIED = "0xaaeb6d33bfb00b3e17bf4254a99bbc05ae6215c458924aed1119209ed8fe2416";
    public static final String VOTED = "0xc34f869b7ff431b034b7b9aea9822dac189a685e0b015c7d1be3add3f89128e8";
    public static final String PROPOSAL_ADDED = "0x4c6647c4a4058c1a0bcd513fe2522706466700a104b82d4487d888f17ce5be13";

    public static final String TOKEN_HOLDER_USED_FOR_TEST = "0x4aC145CB8033649C3727BDe0b1BfAcfc3A3a9A9a";

    public static final String DAO_BLOCK_NUMBER = "6955312";
    public static final String DAO_BLOCK_NUMBER_HEX = Numeric.toHexStringWithPrefixSafe(new BigInteger(DAO_BLOCK_NUMBER));

    public static final String INFURA_WEBSOCKET_RINKEBYT = "wss://rinkeby.infura.io/ws";
    public static final String CONTRACT_ADDRESS_RINKEBY = "0x6ed82ffe4c0db4f9b0d52ef943a4e75b4159d85a";
    public static final String CONTRACT_CREATION_BLOCK_RINKEBY = "0x31C390";



    /* Contracts for test*/
}
