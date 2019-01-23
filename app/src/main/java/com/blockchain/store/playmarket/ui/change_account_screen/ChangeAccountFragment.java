package com.blockchain.store.playmarket.ui.change_account_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ChangeAccountAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAccountFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChangeAccountAdapter adapter;

    public ChangeAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_account, container, false);
        ButterKnife.bind(this,view);
        initAdapter();
        return view;
    }

    private void initAdapter() {
        adapter = new ChangeAccountAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        getActivity().onBackPressed();
    }

}
