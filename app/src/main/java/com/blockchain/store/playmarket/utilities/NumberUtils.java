package com.blockchain.store.playmarket.utilities;

import android.util.Log;

public class NumberUtils {
    private static final String TAG = "NumberUtils";
    public static final int ADDRESS_NUMBER_OF_CHARACTER = 6;
    public static final int TOKEN_NUMBER_OF_CHARACTER = 3;


    public static String formatStringToSpacedNumber(String numberString, int numberOfCharacterBeforeSpace) {
        StringBuilder hex = new StringBuilder(numberString);
        Log.d(TAG, "getFormattedAddress: " + hex);
        for (int i = 2; i < hex.toString().length(); i += 6) {
            hex.insert(i, " ");
        }
        Log.d(TAG, "getFormattedAddress: " + hex);
        return hex.toString();
    }
}
