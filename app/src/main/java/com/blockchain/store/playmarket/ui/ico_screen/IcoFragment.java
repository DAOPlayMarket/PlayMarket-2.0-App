package com.blockchain.store.playmarket.ui.ico_screen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.IcoListAdapter;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.interfaces.AppInfoCallback;
import com.blockchain.store.playmarket.ui.invest_screen.InvestActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;

import java.util.ArrayList;

import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.RECIPIENT_ARG;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcoFragment extends Fragment implements IcoFragmentContracts.View, SwipeRefreshLayout.OnRefreshListener, AppInfoCallback {
    private static final String TAG = "IcoFragment";

    private IcoFragmentPresenter presenter;
    private IcoListAdapter adapter;
    private RecyclerView recyclerView;
    private View errorHolder;
    private Button errorRepeatButton;
    private ProgressBar progressBar;
    private TextView emptyView;
    private SwipeRefreshLayout icoRefreshLayout;

    public IcoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico, container, false);
        bindViews(view);
        icoRefreshLayout.setOnRefreshListener(this);
        attachPresenter();
        getIcoApps();
        return view;
    }

    @Override
    public void onRefresh() {
        attachPresenter();
        getIcoApps();
        icoRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        errorHolder = view.findViewById(R.id.error_holder);
        errorRepeatButton = view.findViewById(R.id.error_view_repeat_btn);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyView = view.findViewById(R.id.empty_view);
        icoRefreshLayout = view.findViewById(R.id.ico_refresh_layout);

        errorRepeatButton.setOnClickListener(v -> presenter.getIcoApps());
    }

    private void attachPresenter() {
        presenter = new IcoFragmentPresenter();
        presenter.init(this);
    }

    private void getIcoApps() {
        presenter.getIcoApps();
        setErrorViewVisible(false);
    }


    @Override
    public void onIcoAppsReady(ArrayList<AppInfo> apps) {
        errorHolder.setVisibility(View.GONE);
        if (apps.isEmpty()) {
            setEmptyViewVisible(true);
        } else {
            setupRecyclerView(apps);
        }

    }

    private void setEmptyViewVisible(boolean isVisible) {
        emptyView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setErrorViewVisible(boolean isVisible) {
        errorHolder.setVisibility(isVisible ? View.VISIBLE : View.GONE);

    }

    private void setupRecyclerView(ArrayList<AppInfo> apps) {
        adapter = new IcoListAdapter(apps, this, true);
        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {
        setErrorViewVisible(true);
    }

    @Override
    public void setProgress(boolean progress) {
        progressBar.setVisibility(progress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAppInfoClicked(AppInfo appinfo) {
        InvestActivity.startFromIco(getActivity(), appinfo);
    }

    @Override
    public void onAppTransferTokenClicked(AppInfo appinfo) {
        TransferActivity.startAsTokenTransfer(getActivity(), appinfo);
    }

    @Override
    public void onAppInvestClicked(String address) {
        Intent intent = new Intent(getActivity(), TransferActivity.class);
        intent.putExtra(RECIPIENT_ARG, address);
        startActivity(intent);
    }
}
