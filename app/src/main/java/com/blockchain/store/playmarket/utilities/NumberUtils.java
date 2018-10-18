package com.blockchain.store.playmarket.utilities;

import android.util.Log;

public class NumberUtils {
    private static final String TAG = "NumberUtils";
    public static final int ADDRESS_NUMBER_OF_CHARACTER = 6;
    public static final int TOKEN_NUMBER_OF_CHARACTER = 4;


    public static String formatStringToSpacedNumber(String numberString) {
        StringBuilder hex = new StringBuilder(numberString);
        for (int i = 2; i < hex.toString().length(); i += ADDRESS_NUMBER_OF_CHARACTER) {
            hex.insert(i, " ");
        }
        return hex.toString();
    }

    public static String formatTokenToSpacedNumber(String numberString){
        StringBuilder hex = new StringBuilder(numberString);
        for (int i = 1; i < hex.toString().length(); i += TOKEN_NUMBER_OF_CHARACTER) {
            hex.insert(i, " ");
        }
        return hex.toString();
    }
}
