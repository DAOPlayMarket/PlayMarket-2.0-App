package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.Token;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> implements Filterable {

    private TokenAdapterListener callback;
    private ArrayList<Token> filteredTokenList;
    private ArrayList<Token> originalTokenList;
    private boolean isLoading = false;
    private boolean isOpenFromBottomSheet = false;

    public TokenAdapter(ArrayList<Token> tokensList, TokenAdapterListener callback) {
        this.filteredTokenList = tokensList;
        this.originalTokenList = tokensList;
        this.callback = callback;
    }

    public TokenAdapter(TokenAdapterListener callback) {
        this.filteredTokenList = new ArrayList<>();
        this.callback = callback;
        this.isLoading = true;
        this.isOpenFromBottomSheet = true;
    }

    public void setTokens(ArrayList<Token> tokensList) {
        this.filteredTokenList = tokensList;
        this.originalTokenList = tokensList;
        isLoading = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_list_item, parent, false);
        return new TokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {
        if (isLoading) {
            holder.bind(null);
        } else {
            holder.bind(filteredTokenList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (isLoading) return 1;
        return filteredTokenList.size();
    }

    public void addNewToken(Token token) {
        this.filteredTokenList.add(0, token);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<Token> tempCurrencies = new ArrayList<>();
                if (charString.isEmpty()) {
                    tempCurrencies = originalTokenList;
                } else {
                    for (Token token : originalTokenList) {
                        if (token.name.toLowerCase().contains(charString.toLowerCase())) {
                            tempCurrencies.add(token);
                        }
                    }

                    if (tempCurrencies.isEmpty()) {
                        for (Token token : originalTokenList) {
                            if (token.name.toLowerCase().contains(charString.toLowerCase())) {
                                tempCurrencies.add(token);
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tempCurrencies;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredTokenList = (ArrayList<Token>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    class TokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.button) Button button;
        @BindView(R.id.group) Group group;
        @BindView(R.id.progress_bar) ProgressBar progressBar;

        public TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Token token) {
            if (isLoading) {
                group.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                group.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                name.setText(token.name);
                symbol.setText(token.symbol);
                button.setOnClickListener(v -> callback.onTokenClicked(token));
            }

            if (isOpenFromBottomSheet) {
                button.setText("Add to list");
            }

        }
    }

    public interface TokenAdapterListener {
        void onTokenClicked(Token token);
    }
}
