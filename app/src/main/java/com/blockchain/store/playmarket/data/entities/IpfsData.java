package com.blockchain.store.playmarket.data.entities;

import android.content.Context;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;

public class IpfsData implements NotificationImpl {
    private Context context;
    private String url;

    public IpfsData(String url) {
        this.url = url;
        this.context = Application.getInstance().getApplicationContext();
    }

    @Override
    public int getId() {
        return 555;
    }

    @Override
    public String getTitleName() {
        return "Скачивание IPFS";
    }

    @Override
    public String getSuccessResultName() {
        return "IPFS успешно скачано";
    }

    @Override
    public String getFailedResultName() {
        return "При скачивании IPFS произошла ошибка";
    }
}
