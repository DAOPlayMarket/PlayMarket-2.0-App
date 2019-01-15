package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.dao.data.entities.DaoToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaoTokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DaoToken> daoTokens;

    public DaoTokenAdapter(List<DaoToken> daoTokens) {
        this.daoTokens = daoTokens;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dao_token_list_item, parent, false);
        return new DaoTokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DaoTokenViewHolder) {
            ((DaoTokenViewHolder) holder).bind(daoTokens.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return daoTokens.size();
    }

    class DaoTokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.button) TextView button;
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
            setClickEnabled(daoToken.total != 0);

        }

        private void setClickEnabled(boolean isEnabled) {
            transferIcon.setEnabled(isEnabled);
            button.setEnabled(isEnabled);

            transferIcon.setAlpha(isEnabled ? 1 : 0.3f);
            button.setAlpha(isEnabled ? 1 : 0.3f);

            transferIcon.setOnClickListener(v -> {

            });
            button.setOnClickListener(v -> {

            });
        }

        @OnClick({R.id.button, R.id.imageView2})
        void onTransferClicked() {

        }
    }
}
