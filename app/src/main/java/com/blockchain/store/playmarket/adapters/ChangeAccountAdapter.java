package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.AccountManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.ethereum.geth.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.ChangeAccountViewHolder> {
    private static final String TAG = "ChangeAccountAdapter";

    private List<Account> accountList = Collections.emptyList();
    private ArrayList<Pair<String, String>> arrayOfPair = new ArrayList<>();
    private adapterCallback callback;
    private int selectionPosition = AccountManager.getCurrentUserPosition();

    public ChangeAccountAdapter(adapterCallback callback) {
        try {
            this.callback = callback;
            accountList = AccountManager.getKeyManager().getAccounts();
            for (Account account : accountList) {
                arrayOfPair.add(new Pair<>(account.getAddress().getHex(), "-1"));
            }
            Log.d(TAG, "ChangeAccountAdapter: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(Pair<String, String> updatedData) {
        for (int i = 0; i < arrayOfPair.size(); i++) {
            Pair<String, String> pair = arrayOfPair.get(i);
            if (pair.first.equalsIgnoreCase(updatedData.first)) {
                arrayOfPair.set(i, updatedData);
            }
        }
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ChangeAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.change_account_item, parent, false);
        ChangeAccountViewHolder changeAccountViewHolder = new ChangeAccountViewHolder(view);
        changeAccountViewHolder.rootView.setOnClickListener(v -> {
            int position = changeAccountViewHolder.getAdapterPosition();
            selectionPosition = position;
            callback.onAddressClicked(position);
            notifyDataSetChanged();
        });
        changeAccountViewHolder.rootView.setOnLongClickListener(view1 -> {
            if (accountList.size() == 1) {
                Toast.makeText(changeAccountViewHolder.itemView.getContext(), R.string.cant_delete_last_Account, Toast.LENGTH_SHORT).show();
                return false;
            }
            int adapterPosition = changeAccountViewHolder.getAdapterPosition();
            callback.onDeleteAccountClicked(accountList.get(adapterPosition));
            return false;
        });
        return changeAccountViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAccountViewHolder holder, int position) {
        holder.bind(arrayOfPair.get(position), position);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public void refreshAccounts() {
        try {
            accountList = AccountManager.getKeyManager().getAccounts();
            ArrayList<Pair<String, String>> newPair = new ArrayList<>();
            for (int i = 0; i < accountList.size(); i++) {
                boolean isAlreadyHas = false;
                for (int l = 0; l < arrayOfPair.size(); l++) {
                    Account currentAccount = accountList.get(i);
                    Pair<String, String> currentPair = arrayOfPair.get(l);

                    if (currentAccount.getAddress().getHex().equalsIgnoreCase(currentPair.first)) {
                        newPair.add(arrayOfPair.get(l));
                        isAlreadyHas = true;
                    }
                }
                if (!isAlreadyHas) {
                    callback.onNewAccountDetected(accountList.get(i).getAddress().getHex());
                    newPair.add(new Pair<>(accountList.get(i).getAddress().getHex(), "-1"));
                }
            }

            this.arrayOfPair = newPair;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ChangeAccountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_address) TextView account;
        @BindView(R.id.expandedLayout) ExpandableLayout expandableLayout;
        @BindView(R.id.delete_holder) View deleteHolder;
        @BindView(R.id.balance) TextView balanceView;
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.root_view) View rootView;

        private Resources resources;
        private Context context;

        public ChangeAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            resources = itemView.getContext().getResources();
            this.context = itemView.getContext();
        }

        public void bind(Pair<String, String> account, int position) {
            this.account.setText(account.first);
            this.account.setTextColor(selectionPosition == position ?
                    resources.getColor(R.color.colorAccent)
                    : resources.getColor(R.color.white));

            if (account.second.equalsIgnoreCase("-1")) {
                balanceView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

            } else {
                progressBar.setVisibility(View.GONE);
                balanceView.setVisibility(View.VISIBLE);
                balanceView.setText(String.format(context.getString(R.string.balance_field_with_eth), new EthereumPrice(account.second).inEther().toString()));
            }
        }
    }

    public interface adapterCallback {
        void onAddressClicked(int position);

        void onDeleteAccountClicked(Account account);

        void onNewAccountDetected(String address);
    }
}
