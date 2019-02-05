package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.DaoAdapterCallback;
import com.blockchain.store.playmarket.repositories.TokenRepository;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaoTokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DaoToken> daoTokens;
    DaoAdapterCallback callback;
    private boolean isOpenAsWallet = false;
    private int selectPosition = 0;

    public DaoTokenAdapter(List<DaoToken> daoTokens, DaoAdapterCallback callback) {
        this.daoTokens = daoTokens;
        this.callback = callback;
    }

    public DaoTokenAdapter(List<DaoToken> daoTokens, DaoAdapterCallback callback, boolean openAsDaoWallet) {
        this.isOpenAsWallet = openAsDaoWallet;
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
            if (isOpenAsWallet) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dao_wallet_token_list_item, parent, false);
                DaoWalletViewHolder daoWalletViewHolder = new DaoWalletViewHolder(view);
                daoWalletViewHolder.deleteHolder.setOnClickListener(v -> {
                    int position = daoWalletViewHolder.getAdapterPosition();
                    TokenRepository.deleteToken(daoTokens.get(position));
                    try {
                        daoTokens.remove(position);
                        notifyItemRemoved(position);
                    } catch (Exception e) {

                    }

                });
                return daoWalletViewHolder;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dao_token_list_item, parent, false);
                return new DaoTokenViewHolder(view);
            }

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
        if (holder instanceof DaoWalletViewHolder) {
            ((DaoWalletViewHolder) holder).bind(daoTokens.get(position), position);
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
        @BindView(R.id.touch_helper) View touchHelper;

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
            touchHelper.setOnClickListener(v -> callback.onPmTokenClicked(daoToken));

        }

        private void setClickEnabled(boolean isEnabled) {
            transferIcon.setEnabled(isEnabled);
            button.setEnabled(isEnabled);

            transferIcon.setAlpha(isEnabled ? 1 : 0.3f);
            button.setAlpha(isEnabled ? 1 : 0.3f);
        }
    }

    class DaoWalletViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.touch_helper) View touch_helper;
        @BindView(R.id.button) TextView button;
        @BindView(R.id.token_balance_field) TextView token_balance_field;
        @BindView(R.id.token_balance) TextView token_balance;
        @BindView(R.id.imageView2) ImageView imageView2;
        @BindView(R.id.expandable_layout) ExpandableLayout expandableLayout;
        @BindView(R.id.linearLayout2) ConstraintLayout rootLayout;
        @BindView(R.id.delete_holder) View deleteHolder;
        @BindView(R.id.dividends_amount_field) TextView dividendsAmountField;
        @BindView(R.id.dividends_amount) TextView dividendsAmount;


        public DaoWalletViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(DaoToken daoToken, int position) {
            if (daoToken.address.equalsIgnoreCase(DaoConstants.CRYPTO_DUEL_CONTRACT)) {
                dividendsAmountField.setVisibility(View.VISIBLE);
                dividendsAmount.setVisibility(View.VISIBLE);
                dividendsAmount.setText(daoToken.getOwnersBal());
            } else {
                dividendsAmountField.setVisibility(View.GONE);
                dividendsAmount.setVisibility(View.GONE);
            }

            name.setText(daoToken.name);
            symbol.setText(daoToken.symbol);
            name.setText(daoToken.name);
            symbol.setText(daoToken.symbol);
            token_balance.setText(String.valueOf(daoToken.getBalanceWithDecimals()));

            imageView2.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
            button.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
            touch_helper.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));

            rootLayout.setOnClickListener(v -> {
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                } else {
                    expandableLayout.expand();
                }
            });

        }
    }

    class DaoTokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.receive_text) TextView receiveText;
        @BindView(R.id.token_dividends_amount) TextView dividendsAmount;
        @BindView(R.id.imageView2) ImageView transferIcon;
        @BindView(R.id.touch_helper) View touchHelper;

        public DaoTokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DaoToken daoToken) {
            name.setText(daoToken.name);
            symbol.setText(daoToken.symbol);
            dividendsAmount.setText(String.valueOf(daoToken.getTokenCountWithDecimals()));
            setClickEnabled(daoToken.countToken() != 0 && daoToken.isWithdrawBlocked);
            transferIcon.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
            receiveText.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
            touchHelper.setOnClickListener(v -> callback.onDaoTokenClicked(daoToken));
        }

        private void setClickEnabled(boolean isEnabled) {
            transferIcon.setEnabled(isEnabled);
            receiveText.setEnabled(isEnabled);
            touchHelper.setEnabled(isEnabled);

            transferIcon.setAlpha(isEnabled ? 1 : 0.4f);
            receiveText.setAlpha(isEnabled ? 1 : 0.4f);


        }

        @OnClick({R.id.receive_text, R.id.imageView2})
        void onTransferClicked() {

        }
    }
}
