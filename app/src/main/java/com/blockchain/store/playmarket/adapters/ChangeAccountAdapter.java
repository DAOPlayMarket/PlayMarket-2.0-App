package com.blockchain.store.playmarket.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.ethereum.geth.Account;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.ChangeAccountViewHolder> {
    private static final String TAG = "ChangeAccountAdapter";

    private List<Account> accountList = Collections.emptyList();
    private adapterCallback callback;
    private int selectionPosition = AccountManager.getCurrentUserPosition();

    public ChangeAccountAdapter(adapterCallback callback) {
        try {
            this.callback = callback;
            accountList = AccountManager.getKeyManager().getAccounts();
            Log.d(TAG, "ChangeAccountAdapter: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ChangeAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.change_account_item, parent, false);
        ChangeAccountViewHolder changeAccountViewHolder = new ChangeAccountViewHolder(view);
        changeAccountViewHolder.account.setOnClickListener(v -> {
            int position = changeAccountViewHolder.getAdapterPosition();
            if (position == selectionPosition) {
//                changeAccountViewHolder.expandableLayout.setExpanded(true);
            }
            selectionPosition = position;
            callback.onAddressClicked(position);
            notifyDataSetChanged();
        });
        changeAccountViewHolder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountList.size() == 1) {
                    Toast.makeText(changeAccountViewHolder.itemView.getContext(), R.string.cant_delete_last_Account, Toast.LENGTH_SHORT).show();
                    return;
                }
                int adapterPosition = changeAccountViewHolder.getAdapterPosition();
                callback.onDeleteAccountClicked(accountList.get(adapterPosition));
            }
        });
        return changeAccountViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAccountViewHolder holder, int position) {
        holder.bind(accountList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class ChangeAccountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_address) TextView account;
        @BindView(R.id.expandedLayout) ExpandableLayout expandableLayout;
        @BindView(R.id.delete_holder) View deleteHolder;

        Resources resources;

        public ChangeAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            resources = itemView.getContext().getResources();
        }

        public void bind(Account account, int position) {
            this.account.setText(account.getAddress().getHex());
            this.account.setTextColor(selectionPosition == position ?
                    resources.getColor(R.color.colorAccent)
                    : resources.getColor(R.color.white));


        }
    }

    public interface adapterCallback {
        public void onAddressClicked(int position);

        void onDeleteAccountClicked(Account account);
    }
}
