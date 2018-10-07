package com.blockchain.store.playmarket.data.entities;

public class CryptoPriceResponse {
    public String price;

    public double getPrice() {
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            return 0d;
        }
    }
}
