package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TransactionHistoryAdapt";

    private ArrayList<TransactionModel> transactionModels;

    public TransactionHistoryAdapter(ArrayList<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
    }

    @Override
    public int getItemViewType(int position) {
        return transactionModels.get(position).getTransactionType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        if (viewType == Constants.TransactionTypes.BUY_APP.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        }
        if (viewType == Constants.TransactionTypes.INVEST.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        }
        if (viewType == Constants.TransactionTypes.TRANSFER.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        }
        if (viewType == Constants.TransactionTypes.TRANSFER_TOKEN.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        }
        if (viewType == Constants.TransactionTypes.SEND_REVIEW.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        }


        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DefaultViewHolder) {
            ((DefaultViewHolder) holder).bind(transactionModels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title) TextView item_title;
        @BindView(R.id.item_status) TextView item_status;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TransactionModel model) {
            item_title.setText(model.getTransactionType().toString());
            item_status.setText(model.transactionStatus.toString());

        }


    }
}
