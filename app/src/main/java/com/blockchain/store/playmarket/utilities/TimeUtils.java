package com.blockchain.store.playmarket.utilities;

public class TimeUtils {
    public static String unixTimeToDays(long unixTimeSec) {

        long divReminder = unixTimeSec;
        String result = "";

        int[] dimensionArr = {86400000, 3600000, 60000, 1000};

        for (int dimension : dimensionArr) {
            if (divReminder > dimension) {

                long timeValue = divReminder / dimension;
                timeValue = (int) timeValue;
                String timeValueStr = ((timeValue <= 9) ? "0" + String.valueOf(timeValue) : String.valueOf(timeValue));
                result = result + ((dimension == dimensionArr[0]) ? timeValueStr + " days " : (dimension == dimensionArr[3]) ? timeValueStr : timeValueStr + ':');

                divReminder = unixTimeSec % dimension;
            } else {
                result = result + ((dimension == dimensionArr[0]) ? "00 days " : (dimension == dimensionArr[3]) ? "00" : "00:");
            }
        }

        return result;
    }
}
