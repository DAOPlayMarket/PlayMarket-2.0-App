package com.blockchain.store.dao.ui.dividends_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.DaoTokenTransfer;
import com.blockchain.store.dao.ui.dao_activity.DaoActivity;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.DaoTokenAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DividendsFragment extends Fragment {
    private static final String TAG = "DividendsFragment";

    @BindView(R.id.background) View background;
    @BindView(R.id.back_arrow) ImageView backArrow;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    DaoTokenAdapter adapter;

    public DividendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dividends, container, false);
        ButterKnife.bind(this, view);
        getTokens();
        return view;
    }

    @OnClick(R.id.back_arrow)
    void onBackArrowClicked() {
        getActivity().onBackPressed();
    }

    private void getTokens() {
        DaoTransactionRepository.getTokens().subscribe(this::onTokensReady, this::onTokensError);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void onTokensError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
    }

    private void onTokensReady(List<DaoToken> daoTokens) {
        progressBar.setVisibility(View.GONE);
        initAdapter(daoTokens);
    }

    private void initAdapter(List<DaoToken> daoTokens) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DaoTokenAdapter(daoTokens, daoToken -> DaoTokenTransfer.start(getActivity(), daoToken));
        recyclerView.setAdapter(adapter);
    }

}
