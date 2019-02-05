package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.dao.data.entities.DaoToken;

public interface DaoAdapterCallback {
    void onPmTokenClicked(DaoToken daoToken);
    void onDaoTokenClicked(DaoToken daoToken);
}