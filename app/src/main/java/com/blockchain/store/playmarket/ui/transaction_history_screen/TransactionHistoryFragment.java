package com.blockchain.store.playmarket.ui.transaction_history_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TransactionHistoryAdapter;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionHistoryFragmentContract.View {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    private static final String STATUS_KEY = "status_key";
    private static final String SHOW_ALL_KEY = "show_all_transcation";

    private boolean isShowAllTransactions;
    private Constants.TransactionTypes showTransactionType;
    private TransactionHistoryFragmentPresenter presenter;
    private TransactionHistoryAdapter adapter;

    public static TransactionHistoryFragment instance(Constants.TransactionStatus statusToShow) {
        return instance(statusToShow, false);
    }

    public static TransactionHistoryFragment instance(boolean showAllTransactions) {
        return instance(null, true);
    }

    private static TransactionHistoryFragment instance(Constants.TransactionStatus statusToShow, boolean showAllTransaction) {
        Bundle args = new Bundle();
        args.putSerializable(STATUS_KEY, statusToShow);
        args.putBoolean(SHOW_ALL_KEY, showAllTransaction);
        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        ButterKnife.bind(this, view);
        isShowAllTransactions = getArguments().getBoolean(SHOW_ALL_KEY, false);
        showTransactionType = (Constants.TransactionTypes) getArguments().getSerializable(STATUS_KEY);
        initPresenter();
        return view;
    }

    private void initPresenter() {
        presenter = new TransactionHistoryFragmentPresenter();
        presenter.init(this);
        loadData();
    }

    private void loadData() {
        if (isShowAllTransactions) {

        } else {

        }
    }

    private void populateRecyclerView(ArrayList<TransactionModel> transactionModels) {
        if (!transactionModels.isEmpty()) {
            emptyView.setVisibility(View.GONE);

            adapter = new TransactionHistoryAdapter(transactionModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);

        } else {
            emptyView.setVisibility(View.VISIBLE);
        }


    }

}
