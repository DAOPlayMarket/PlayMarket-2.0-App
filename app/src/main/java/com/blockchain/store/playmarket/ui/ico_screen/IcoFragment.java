package com.blockchain.store.playmarket.ui.ico_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.IcoListAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcoFragment extends Fragment implements IcoFragmentContracts.View, AppListCallbacks {
    private static final String TAG = "IcoFragment";

    private IcoFragmentPresenter presenter;
    private IcoListAdapter adapter;
    private RecyclerView recyclerView;
    private View errorHolder;
    private Button errorRepeatButton;
    private ProgressBar progressBar;

    public IcoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico, container, false);
        bindViews(view);
        attachPresenter();
        getIcoApps();
        return view;
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        errorHolder = view.findViewById(R.id.error_holder);
        errorRepeatButton = view.findViewById(R.id.error_view_repeat_btn);
        progressBar = view.findViewById(R.id.progress_bar);
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
    public void onIcoAppsReady(ArrayList<App> apps) {
        if (apps.isEmpty()) {
            setErrorViewVisible(true);
        } else {
            setupRecyclerView(apps);
        }

    }

    private void setErrorViewVisible(boolean isVisible) {
        errorHolder.setVisibility(isVisible ? View.VISIBLE : View.GONE);

    }

    private void setupRecyclerView(ArrayList<App> apps) {
        adapter = new IcoListAdapter(apps, this);
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
    public void onAppClicked(App app) {
        AppDetailActivity.start(getActivity(), app);
    }
}
