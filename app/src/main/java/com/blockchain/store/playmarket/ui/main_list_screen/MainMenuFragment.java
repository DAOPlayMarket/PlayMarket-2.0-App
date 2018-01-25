package com.blockchain.store.playmarket.ui.main_list_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.MainFragmentCallbacks;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuFragment extends Fragment {
    private static final String TAG = "MainMenuFragment";

    @BindView(R.id.recycler_view_main) RecyclerView recyclerView;
    @BindView(R.id.progress_bar_main) ProgressBar progressBar;

    private MainFragmentCallbacks mCallbacks;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentCallbacks) {
            mCallbacks = (MainFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
