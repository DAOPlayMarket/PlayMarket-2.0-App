package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.Token;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> {
    private final TokenAdapterListener callback;
    private ArrayList<Token> tokensList;

    public TokenAdapter(ArrayList<Token> tokensList, TokenAdapterListener callback) {
        this.tokensList = tokensList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_list_item, parent, false);
        return new TokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {
        holder.bind(tokensList.get(position));
    }

    @Override
    public int getItemCount() {
        return tokensList.size();
    }

    public void addNewToken(Token token) {
        this.tokensList.add(0, token);
        notifyDataSetChanged();
    }

    class TokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.decimals) TextView decimals;
        @BindView(R.id.site) TextView site;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.button) Button button;

        public TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Token token) {
            name.setText(token.name);
            symbol.setText(token.symbol);
            decimals.setText(token.decimals);
            site.setText(token.site);
            price.setText(token.price);
            button.setOnClickListener(v -> callback.onTokenClicked(token));
        }
    }

    public interface TokenAdapterListener {
        void onTokenClicked(Token token);
    }
}
