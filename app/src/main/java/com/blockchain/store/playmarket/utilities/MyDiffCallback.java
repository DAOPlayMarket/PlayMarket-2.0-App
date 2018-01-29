package com.blockchain.store.playmarket.utilities;

import android.support.v7.util.DiffUtil;

import com.blockchain.store.playmarket.data.entities.AppDispatcherType;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public class MyDiffCallback extends DiffUtil.Callback {
    AppDispatcherType oldDispatcher;
    AppDispatcherType newDispatcher;

    public MyDiffCallback(AppDispatcherType oldDispatcher, AppDispatcherType newDispatcher) {
        this.oldDispatcher = oldDispatcher;
        this.newDispatcher = newDispatcher;
    }

    @Override
    public int getOldListSize() {
        return oldDispatcher.apps.size();
    }

    @Override
    public int getNewListSize() {
        return newDispatcher.apps.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDispatcher.apps.get(oldItemPosition).appId.equalsIgnoreCase(newDispatcher.apps.get(newItemPosition).appId);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDispatcher.apps.get(oldItemPosition).equals(newDispatcher.apps.get(newItemPosition));
    }

}
