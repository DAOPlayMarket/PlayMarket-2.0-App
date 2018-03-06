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

    public static enum Currency{
        ETHER,
        GWEI,
        WEI
    }

    public EthereumPrice (String price) {
        wei = new BigDecimal(price);
        df = makeDecimalFormat();
    }

    public EthereumPrice(String price, Currency currency){
        wei = new BigDecimal(price);
        df = makeDecimalFormat();

        if (currency == Currency.ETHER){
            wei = wei.multiply(new BigDecimal("1000000000000000000"));
        }

        if (currency == Currency.GWEI){
            wei = wei.multiply(new BigDecimal("1000000000"));
        }
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
        if(wei.compareTo(new BigDecimal("0")) == 0){
            return new BigDecimal("0");
        }
        return wei.divide(new BigDecimal("1000000000.0"));
    }
    public BigDecimal inEther() {
        if(wei.compareTo(new BigDecimal("0")) == 0){
            return new BigDecimal("0");
        }
        return inGwei().divide(new BigDecimal("1000000000.0"));
    }

    public String getDisplayPrice(boolean onlyNumber) {
        if (isZero() && onlyNumber == false) {
            return "FREE";
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
