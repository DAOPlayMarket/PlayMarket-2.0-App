package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;

import org.ethereum.geth.Account;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.ChangeAccountViewHolder> {

    private List<Account> accountList = Collections.emptyList();

    public ChangeAccountAdapter() {
        try {
            accountList = AccountManager.getKeyManager().getAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ChangeAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.change_account_item, parent, false);
        return new ChangeAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAccountViewHolder holder, int position) {
        holder.bind(accountList.get(position));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class ChangeAccountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_address)
        TextView account;

        public ChangeAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Account account) {
            this.account.setText(account.getAddress().getHex());
        }
    }
}
