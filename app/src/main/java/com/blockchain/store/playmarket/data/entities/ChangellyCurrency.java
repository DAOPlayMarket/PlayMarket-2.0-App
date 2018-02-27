package com.blockchain.store.playmarket.data.entities;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ChangellyCurrency {
    public String name;
    public String fullName;
    public boolean enabled;

    public String getImageUrl() {
        return "https://changelly.com/coins/" + name + ".png";
    }
}
