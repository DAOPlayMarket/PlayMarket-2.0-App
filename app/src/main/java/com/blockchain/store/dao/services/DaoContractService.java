package com.blockchain.store.dao.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.votes_screen.main_votes_screen.MainVotesFragment;
import com.blockchain.store.playmarket.Application;

import java.util.List;

public class DaoContractService extends Service {

    private static final String TAG = "DaoContractService";
    private DaoDatabase daoDatabase = Application.getDaoDatabase();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = 0;
        DaoTransactionRepository.getRules().subscribe(this::onGetRulesSuccess, this::onGetRulesError);
        return super.onStartCommand(intent, flags, startId);
    }

    private void onGetRulesSuccess(Rules rules) {
        Rules databaseRules = daoDatabase.rulesDao().getRules();
        if (databaseRules == null) daoDatabase.rulesDao().insert(rules);
        else daoDatabase.rulesDao().update(rules);

        int id = 0;

        List<Proposal> proposals = daoDatabase.proposalDao().getAll();
        if (!proposals.isEmpty()) {
            for (Proposal proposal : proposals) {
                if (proposal.isExecuted || (proposal.endTimeOfVoting * 1000 > System.currentTimeMillis() && proposal.numberOfVotes > rules.minimumQuorum)) {
                    id = proposal.proposalID;
                }
            }
        }
        DaoTransactionRepository.getProposals(id).subscribe(this::onProposalsLoaded, this::onProposalsError);
    }

    private void onGetRulesError(Throwable throwable) {
    }

    private void onProposalsLoaded(List<Proposal> proposals) {
        for (Proposal proposal : proposals) {
            if (daoDatabase.proposalDao().getById(proposal.proposalID) == null) {
                daoDatabase.proposalDao().insert(proposal);
            } else {
                daoDatabase.proposalDao().update(proposal);
            }
        }
        sendBroadcast();
    }


    private void onProposalsError(Throwable throwable) {
        Log.d(TAG, "onProposalsLoaded: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendBroadcast() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainVotesFragment.BROADCAST_ACTION);
        broadcastIntent.putExtra("IsSync", true);
        sendBroadcast(broadcastIntent);
    }


}
