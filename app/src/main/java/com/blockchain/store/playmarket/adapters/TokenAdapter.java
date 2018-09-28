package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.Token;

import java.util.ArrayList;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> {
    private ArrayList<Token> tokensList;

    public TokenAdapter(ArrayList<Token> tokensList) {
        this.tokensList = tokensList;
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_list_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tokensList.size();
    }

    class TokenViewHolder extends RecyclerView.ViewHolder {

        public TokenViewHolder(View itemView) {
            super(itemView);
        }
    }
}
