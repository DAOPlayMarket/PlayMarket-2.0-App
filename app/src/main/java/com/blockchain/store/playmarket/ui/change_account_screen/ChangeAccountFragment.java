package com.blockchain.store.playmarket.ui.change_account_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAccountFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    public ChangeAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_account, container, false);
        ButterKnife.bind(view);

        return view;
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        getActivity().onBackPressed();
    }

}
