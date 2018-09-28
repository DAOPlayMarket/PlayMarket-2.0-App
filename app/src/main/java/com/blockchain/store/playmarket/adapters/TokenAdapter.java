package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> {

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TokenViewHolder extends RecyclerView.ViewHolder {

        public TokenViewHolder(View itemView) {
            super(itemView);
        }
    }
}
