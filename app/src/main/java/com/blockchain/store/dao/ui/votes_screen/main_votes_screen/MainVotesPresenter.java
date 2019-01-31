package com.blockchain.store.dao.ui.votes_screen.main_votes_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.services.DaoContractService;
import com.blockchain.store.playmarket.Application;

import java.util.ArrayList;
import java.util.List;

import static com.blockchain.store.dao.ui.votes_screen.main_votes_screen.MainVotesFragment.BROADCAST_ACTION;

public class MainVotesPresenter implements MainVotesContract.Presenter {

    private MainVotesContract.View view;
    private Context context;
    private DaoDatabase daoDatabase = Application.getDaoDatabase();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSync = intent.getBooleanExtra("IsSync", false);
            if (isSync) {
                List<Proposal> proposals = daoDatabase.proposalDao().getAll();
                Pair<ArrayList<Proposal>, ArrayList<Proposal>> proposalsPair = sortProposals(proposals);
                view.initTabLayout(proposalsPair);
                view.hideProgressBar();
                unregisterBroadcastReceiver();
                stopDaoService();
            }
        }
    };

    private Pair<ArrayList<Proposal>, ArrayList<Proposal>> sortProposals(List<Proposal> proposals) {
        ArrayList<Proposal> archiveProposals = new ArrayList<>();
        ArrayList<Proposal> ongoingProposals = new ArrayList<>();
        for (Proposal proposal : proposals) {
            Proposal.ProposalType proposalType = proposal.getProposalType();
            if (proposalType.equals(Proposal.ProposalType.Executed) || proposalType.equals(Proposal.ProposalType.Unexecutable))
                archiveProposals.add(proposal);
            else
                ongoingProposals.add(proposal);
        }
        return new Pair<>(ongoingProposals, archiveProposals);
    }

    @Override
    public void init(MainVotesContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void startDaoService() {
        registerBroadcastReceiver();
        context.startService(new Intent(context, DaoContractService.class));
    }

    @Override
    public void stopDaoService() {
        context.stopService(new Intent(context, DaoContractService.class));
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        this.context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void unregisterBroadcastReceiver() {
        try {
            this.context.unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
