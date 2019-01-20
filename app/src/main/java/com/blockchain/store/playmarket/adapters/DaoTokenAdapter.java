package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.dao_activity.DaoActivity;
import com.blockchain.store.playmarket.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaoTokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DaoToken> daoTokens;
    DaoActivity.DaoAdapterCallback callback;

    public DaoTokenAdapter(List<DaoToken> daoTokens, DaoActivity.DaoAdapterCallback callback) {
        this.daoTokens = daoTokens;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dao_pm_token_list_item, parent, false);
            return new DaoPlayMarketTokenViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dao_token_list_item, parent, false);
            return new DaoTokenViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DaoTokenViewHolder) {
            ((DaoTokenViewHolder) holder).bind(daoTokens.get(position));
        }
        if (holder instanceof DaoPlayMarketTokenViewHolder) {
            ((DaoPlayMarketTokenViewHolder) holder).bind(daoTokens.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return daoTokens.size();
    }

    class DaoPlayMarketTokenViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.button) TextView button;
        @BindView(R.id.token_balance) TextView balance;
        @BindView(R.id.token_repository_amount) TextView repositoryBalance;
        @BindView(R.id.imageView2) ImageView transferIcon;

        public DaoPlayMarketTokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DaoToken daoToken) {
            name.setText(daoToken.name);
            symbol.setText(daoToken.symbol);
            balance.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
            repositoryBalance.setText(String.valueOf(daoToken.getDaoBalanceWithDecimals()));
//            setClickEnabled(daoToken.getBalanceWithDecimals() != 0 || daoToken.getDaoBalance() != 0);

            transferIcon.setOnClickListener(v -> callback.onPmTokenClicked(daoToken));
            button.setOnClickListener(v -> callback.onPmTokenClicked(daoToken));

        }

        private void setClickEnabled(boolean isEnabled) {
            transferIcon.setEnabled(isEnabled);
            button.setEnabled(isEnabled);

            transferIcon.setAlpha(isEnabled ? 1 : 0.3f);
            button.setAlpha(isEnabled ? 1 : 0.3f);
        }
    }

    class DaoTokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.receive_text) TextView receiveText;
        @BindView(R.id.token_dividends_amount) TextView dividendsAmount;
        @BindView(R.id.imageView2) ImageView transferIcon;

        public DaoTokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DaoToken daoToken) {
            name.setText(daoToken.name);
            symbol.setText(daoToken.symbol);
            dividendsAmount.setText(String.valueOf(daoToken.countToken()));
//            setClickEnabled(daoToken.total != 0);
            transferIcon.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
            receiveText.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
        }

        private void setClickEnabled(boolean isEnabled) {
            transferIcon.setEnabled(isEnabled);
            receiveText.setEnabled(isEnabled);

            transferIcon.setAlpha(isEnabled ? 1 : 0.3f);
            receiveText.setAlpha(isEnabled ? 1 : 0.3f);


        }

        @OnClick({R.id.receive_text, R.id.imageView2})
        void onTransferClicked() {

        }
    }
}
