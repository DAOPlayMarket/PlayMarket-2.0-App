package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.utilities.TransictionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> implements Filterable {
    private TokenAdapterListener callback;
    private ArrayList<Token> filteredTokenList;
    private ArrayList<Token> originalTokenList;
    private boolean isLoading = false;
    private boolean isOpenFromBottomSheet = false;
    private RecyclerView recyclerView;
    private int selectedPosition = -1;

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
            holder.bind(null, position);
        } else {
            holder.bind(filteredTokenList.get(position), position);
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
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
                        if (token.name.toLowerCase().contains(charString.toLowerCase()) || token.symbol.toLowerCase().contains(charString.toLowerCase())) {
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
        @BindView(R.id.button) TextView button;
        @BindView(R.id.group) Group group;
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.token_balance) TextView balance;
        @BindView(R.id.token_balance_field) TextView balanceField;
        @BindView(R.id.imageView2) ImageView transferIcon;

        @BindView(R.id.token_divider) View tokenDivider;
        @BindView(R.id.delete_icon) ImageView deleteIcon;
        @BindView(R.id.delete_tv) TextView deleteTv;
        @BindView(R.id.linearLayout2) ConstraintLayout constraintLayout;

        private Resources resources;

        public TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.resources = itemView.getContext().getResources();
        }

        public void bind(Token token, int position) {
            if (isLoading) {
                group.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                group.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                name.setText(token.name);
                symbol.setText(token.symbol);
                button.setOnClickListener(v -> callback.onTokenClicked(token));
                transferIcon.setOnClickListener(v -> callback.onTokenClicked(token));
                balance.setText(token.balanceOf);
            }
            transferIcon.setVisibility(isOpenFromBottomSheet ? View.GONE : View.VISIBLE);
            button.setTextColor(resources.getColor(R.color.colorAccent));
            if (isOpenFromBottomSheet) {
                balanceField.setVisibility(View.GONE);
                balance.setVisibility(View.GONE);
                if (TokenRepository.isTokenAlreadyAdded(token)) {
                    button.setText("Token added");
                    transferIcon.setVisibility(View.GONE);
                    button.setTextColor(resources.getColor(R.color.added_token_color));
                } else {
                    transferIcon.setVisibility(View.VISIBLE);
                    button.setText("Add token");
                    transferIcon.setImageResource(R.drawable.ic_add);
                }
            } else {
                transferIcon.setVisibility(View.VISIBLE);
                tokenDivider.setVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
                deleteIcon.setVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
                deleteTv.setVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
                constraintLayout.setOnClickListener(v -> {
                    if (selectedPosition == position) {
                        selectedPosition = -1;
                    } else {
                        selectedPosition = position;
                    }

                    TransitionSet transitionSet = TransictionUtils.getTransactionSetForHistoryAdapter();
                    TransitionManager.beginDelayedTransition(recyclerView, transitionSet);
                    notifyDataSetChanged();
                });
            }
        }

    }

    public interface TokenAdapterListener {
        void onTokenClicked(Token token);
    }
}
