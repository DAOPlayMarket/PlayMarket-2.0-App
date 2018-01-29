package com.blockchain.store.playmarket.ui.main_list_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.AppListAdapter;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.interfaces.MainFragmentCallbacks;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuFragment extends Fragment implements MainFragmentContract.View {
    private static final String TAG = "MainMenuFragment";
    private static final String CATEGORY_EXTRA_ARGS = "category_extra_args";
    @BindView(R.id.recycler_view_main) RecyclerView recyclerView;
    @BindView(R.id.progress_bar_main) ProgressBar progressBar;

    private MainFragmentCallbacks mainCallback;
    private AppListAdapter adapter;
    private Category category;
    private MainFragmentContract.Presenter presenter;


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
        attachPresenter();

        setRetainInstance(true);
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
        if (context instanceof MainFragmentCallbacks) {
            mainCallback = (MainFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentCallbacks");
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
        adapter = new AppListAdapter(category.subCategories,appDispatcherTypes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDestroy();
        mainCallback = null;
    }
}
