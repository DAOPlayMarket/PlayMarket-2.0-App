package com.blockchain.store.playmarket.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 28.02.2018.
 */

public class ChangellyCurrenciesAdapter extends RecyclerView.Adapter<ChangellyCurrenciesAdapter.CurrencyViewHolder> implements Filterable {
    ArrayList<ChangellyCurrency> originalCurrencies;
    ArrayList<ChangellyCurrency> filteredCurrencies;
    ChangellyAdapterCallback callback;

    public ChangellyCurrenciesAdapter(ArrayList<ChangellyCurrency> currencies, ChangellyAdapterCallback callback) {
        this.originalCurrencies = currencies;
        this.filteredCurrencies = currencies;
        this.callback = callback;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.changelly_currency_item, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        holder.bind(filteredCurrencies.get(position));
    }

    @Override
    public long getItemId(int position) {
        return filteredCurrencies.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return filteredCurrencies.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<ChangellyCurrency> tempCurrencies = new ArrayList<>();
                if (charString.isEmpty()) {
                    tempCurrencies = originalCurrencies;
                } else {
                    for (ChangellyCurrency originalCurrency : originalCurrencies) {
                        if (originalCurrency.fullName.toLowerCase().contains(charString.toLowerCase())) {
                            tempCurrencies.add(originalCurrency);
                        }
                    }
                    if (tempCurrencies.isEmpty()) {
                        for (ChangellyCurrency originalCurrency : originalCurrencies) {
                            if (originalCurrency.name.toLowerCase().contains(charString.toLowerCase())) {
                                tempCurrencies.add(originalCurrency);
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
                filteredCurrencies = (ArrayList<ChangellyCurrency>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.currency_icon) SimpleDraweeView currencyIcon;
        @BindView(R.id.currency_name) TextView currencyName;
        @BindView(R.id.currency_holder) View currencyHolder;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ChangellyCurrency changellyCurrency) {
            currencyIcon.setImageURI(Uri.parse(changellyCurrency.getImageUrl()));
            currencyName.setText(changellyCurrency.fullName);
            currencyHolder.setOnClickListener(v -> callback.onChangellyCurrencyClicked(changellyCurrency));
        }
    }

    public interface ChangellyAdapterCallback {
        void onChangellyCurrencyClicked(ChangellyCurrency changellyCurrency);
    }

}
