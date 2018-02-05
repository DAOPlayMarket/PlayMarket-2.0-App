package com.blockchain.store.playmarket.ui.main_list_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.AppListAdapter;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.utilities.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuFragment extends Fragment implements MainFragmentContract.View, EndlessRecyclerOnScrollListener.EndlessCallback {
    private static final String TAG = "MainMenuFragment";
    private static final String CATEGORY_EXTRA_ARGS = "category_extra_args";

    @BindView(R.id.recycler_view_main) RecyclerView recyclerView;
    @BindView(R.id.progress_bar_main) ProgressBar progressBar;

    private AppListCallbacks mainCallback;
    private AppListAdapter adapter;
    private Category category;
    private MainFragmentContract.Presenter presenter;
    private boolean isFragmentVisible = false;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_EXTRA_ARGS, category);
        MainMenuFragment fragment = new MainMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.category = getArguments().getParcelable(CATEGORY_EXTRA_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        attachPresenter();
        return view;
    }

    private void attachPresenter() {
        presenter = new MainFragmentPresenter();
        presenter.init(this);
        presenter.setProvider(category);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppListCallbacks) {
            mainCallback = (AppListCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AppListCallbacks");
        }
    }

    @Override
    public void firstItemsReady(AppDispatcherType appDispatcherType) {
        adapter.addNewItems(appDispatcherType);
    }

    @Override
    public void onDispatchersReady(ArrayList<AppDispatcherType> appDispatcherTypes) {
        setRecyclerView(appDispatcherTypes);
    }

    private void setRecyclerView(ArrayList<AppDispatcherType> appDispatcherTypes) {
        recyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setItemPrefetchEnabled(false);
        layoutManager.setInitialPrefetchItemCount(0);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppListAdapter(category.subCategories, appDispatcherTypes, this, mainCallback);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDestroy();
        mainCallback = null;
    }

    @Override
    public void onLoadMore(int currentPage, AppDispatcherType dispatcherType) {
        Log.d(TAG, "onLoadMore() called with: currentPage = [" + currentPage + "], dispatcherType = [" + dispatcherType + "]");
        presenter.loadNewData(dispatcherType);

    }

    /* check for fragment visibility*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() called with: isVisibleToUser = [" + isVisibleToUser + "]");
    }
}
