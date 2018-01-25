package com.blockchain.store.playmarket.data.types;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by samsheff on 06/09/2017.
 */

public class EthereumPrice {

    public BigDecimal wei = BigDecimal.ZERO;

    public static String ETH = "ETH";
    public static String ETHER = "Ether";
    public static String GWEI = "Gwei";
    public static String WEI = "Wei";

    private DecimalFormat df;

    public EthereumPrice (String price) {
        wei = new BigDecimal(price);
        df = makeDecimalFormat();
    }

    public String getUnits() {
        if (inEther().compareTo(new BigDecimal(0.0001)) == 1) {
            return ETH;
        } else if (inGwei().compareTo(new BigDecimal(0.0001)) == 1) {
            return GWEI;
        } else {
            return WEI;
        }
    }

    public BigDecimal inWei() {
        return wei;
    }

    public String inWeiString() {
        return wei.toPlainString();
    }

    public BigDecimal inGwei() {
        return wei.divide(new BigDecimal("1000000000.0"));
    }
    public BigDecimal inEther() {
        return inGwei().divide(new BigDecimal("1000000000.0"));
    }

    public String getDisplayPrice(boolean onlyNumber) {
        if (isZero() && onlyNumber == false) {
            return "Free";
        }

        String priceUnit = getUnits();
        if  (priceUnit.equals(ETH)) {
            return df.format(inEther()) + " " + ETH;
        } else if (priceUnit.equals(GWEI)) {
            return df.format(inGwei()) + " " + GWEI;
        } else {
            return df.format(inWei()) + " " + WEI;
        }
    }

    public boolean isZero() {
        return wei.equals(BigDecimal.ZERO);
    }

    private DecimalFormat makeDecimalFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return df;
    }
}
