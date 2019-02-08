package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SettingTitle extends RecyclerView.ViewHolder {

        public SettingTitle(View itemView) {
            super(itemView);
        }
    }

    class SettingMenu extends RecyclerView.ViewHolder {

        public SettingMenu(View itemView) {
            super(itemView);
        }
    }
}
