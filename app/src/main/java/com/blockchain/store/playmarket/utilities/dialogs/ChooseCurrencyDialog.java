package com.blockchain.store.playmarket.utilities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ChangellyCurrenciesAdapter;
import com.blockchain.store.playmarket.adapters.ChangellyCurrenciesAdapter.ChangellyAdapterCallback;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 28.02.2018.
 */

public class ChooseCurrencyDialog extends DialogFragment {
    private static final String TAG = "ChooseCurrencyDialog";
    private static final String CURRENCIES_ARGS = "currencies";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.search_view) SearchView searchView;

    ArrayList<ChangellyCurrency> currencies;
    ChangellyCurrenciesAdapter adapter;
    ChangellyAdapterCallback callback;

    public static ChooseCurrencyDialog instance(ArrayList<ChangellyCurrency> currencies) {
        ChooseCurrencyDialog dialog = new ChooseCurrencyDialog();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(CURRENCIES_ARGS, currencies);
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currencies = getArguments().getParcelableArrayList(CURRENCIES_ARGS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_currency_dialog, null);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        adapter = new ChangellyCurrenciesAdapter(currencies, callback);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        searchView.setQuery("",false);
        searchView.clearFocus();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof ChangellyAdapterCallback) {
            this.callback = (ChangellyAdapterCallback) getParentFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        dismissAllowingStateLoss();
        super.onSaveInstanceState(outState);
    }
}
