package com.blockchain.store.dao.ui.votes_screen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalDetailsFragment extends Fragment {

    private static String ACCEPT_TAG = "IsAccept";
    private static String EXECUTE_TAG = "IsExecute";

    public static Fragment newInstance(Boolean isAccept, Boolean isExecute){
        Fragment fragment = new ProposalDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ACCEPT_TAG, isAccept);
        bundle.putBoolean(EXECUTE_TAG, isExecute);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proposal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.close_image_button)
    void onCloseClicked(){
        getActivity().onBackPressed();
    }

}
