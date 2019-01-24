package com.blockchain.store.playmarket.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.repositories.TokenRepository;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final String TAG = "TokenAdapter";

    private TokenAdapterListener callback;
    private ArrayList<DaoToken> filteredTokenList;
    private ArrayList<DaoToken> originalTokenList;
    private ArrayList<DaoToken> userSavedTokens;

    public TokenAdapter(ArrayList<DaoToken> tokensList, ArrayList<DaoToken> userSavedTokens, TokenAdapterListener callback) {
        this.filteredTokenList = tokensList;
        this.originalTokenList = tokensList;
        this.userSavedTokens = userSavedTokens;
        this.callback = callback;
    }

    public void setTokens(ArrayList<DaoToken> tokensList) {
        this.filteredTokenList = tokensList;
        this.originalTokenList = tokensList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (filteredTokenList.size() == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_token_item, parent, false);
            return new AddTokenViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.token_list_item, parent, false);
            return new TokenViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TokenViewHolder) {
            ((TokenViewHolder) holder).bind(filteredTokenList.get(position), position);
        }
        if (holder instanceof AddTokenViewHolder) {
            ((AddTokenViewHolder) holder).bind();
        }

    }

    @Override
    public long getItemId(int position) {
        return filteredTokenList.get(position).symbol.hashCode();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return filteredTokenList.size();
    }

    public void addNewToken(DaoToken token) {
        this.filteredTokenList.add(0, token);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<DaoToken> tempCurrencies = new ArrayList<>();
                if (charString.isEmpty()) {
                    tempCurrencies = originalTokenList;
                } else {
                    for (DaoToken token : originalTokenList) {
                        if (token.name.toLowerCase().contains(charString.toLowerCase()) ||
                                token.symbol.toLowerCase().contains(charString.toLowerCase()) ||
                                token.address.toLowerCase().contains(charString.toLowerCase())) {
                            tempCurrencies.add(token);
                        }
                    }

                    if (tempCurrencies.isEmpty()) {
                        for (DaoToken token : originalTokenList) {
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
                filteredTokenList = (ArrayList<DaoToken>) results.values;
                callback.isListEmpty(filteredTokenList.isEmpty());
                notifyDataSetChanged();
            }
        };
    }

    public boolean isTokenAlreadyAdded(DaoToken daoToken) {
        for (DaoToken userSavedToken : userSavedTokens) {
            if (userSavedToken.symbol.equalsIgnoreCase(daoToken.symbol)) {
                return true;
            }
        }
        return false;
    }


    class TokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.button) TextView button;
        @BindView(R.id.group) Group group;
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.imageView2) ImageView transferIcon;

        private Resources resources;


        public TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.resources = itemView.getContext().getResources();
        }

        public void bind(DaoToken token, int position) {
            if (isTokenAlreadyAdded(token)) {
                transferIcon.setEnabled(false);
                button.setText(resources.getText(R.string.token_added));
                button.setEnabled(false);
                button.setAlpha(0.3f);
                transferIcon.setVisibility(View.GONE);
            } else {
                transferIcon.setVisibility(View.VISIBLE);
                button.setAlpha(1f);
                button.setText(resources.getText(R.string.add_token));
                button.setEnabled(true);
                button.setOnClickListener(v -> addToken(token, position));
                transferIcon.setOnClickListener(v -> addToken(token, position));
            }
            name.setText(token.name);
            symbol.setText(token.symbol);
            button.setTextColor(resources.getColor(R.color.colorAccent));
        }

        private void addToken(DaoToken daoToken, int position) {
            callback.onTokenClicked(daoToken);
            userSavedTokens.add(daoToken);
            TokenRepository.addToken(daoToken);
            notifyItemChanged(position);
        }

    }

    class AddTokenViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_token) Button addToken;

        public AddTokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind() {


        }
    }

    public interface TokenAdapterListener {
        void onTokenClicked(DaoToken token);

        void isListEmpty(boolean empty);

    }
}
