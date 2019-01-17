package com.blockchain.store.dao.ui.votes_screen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.dao.adapters.ProposalsAdapter;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VotesFragment extends Fragment implements Callbacks.ProposalCallback {

    public enum StartFlag {
        Ongoing,
        Archive
    }

    private ProposalsAdapter adapter;
    private NavigationCallback navigationCallback;

    @BindView(R.id.proposals_recyclerView) RecyclerView proposalsRecyclerView;

    public static VotesFragment newInstance(StartFlag startFlag, List<Proposal> proposals) {
        Bundle args = new Bundle();
        args.putSerializable("StartFlag", startFlag);
        args.putParcelableArrayList("Proposals", (ArrayList) proposals);
        VotesFragment fragment = new VotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            ArrayList<Proposal> proposals = getArguments().getParcelableArrayList("Proposals");
            showProposals(proposals);
        }
    }

    private void showProposals(ArrayList<Proposal> proposals) {
        if (adapter == null) {
            proposalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            proposalsRecyclerView.setHasFixedSize(true);
            adapter = new ProposalsAdapter(proposals, this);
            adapter.setHasStableIds(true);
            proposalsRecyclerView.setAdapter(adapter);
        } else {
            adapter.setItems(proposals);
        }
    }

    @Override
    public void onItemClicked(Proposal proposal) {
        navigationCallback.onProposalDetailsClicked();
    }

}
