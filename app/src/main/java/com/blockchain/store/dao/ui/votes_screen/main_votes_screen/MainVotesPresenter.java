package com.blockchain.store.dao.ui.votes_screen.main_votes_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.services.DaoContractService;
import com.blockchain.store.playmarket.Application;

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
                view.initTabLayout(proposals);
                view.hideProgressBar();
                unregisterBroadcastReceiver();
                stopDaoService();
            }
        }
    };

    @Override
    public void init(MainVotesContract.View view, Context context) {
        this.view = view;
        this.context = context;
        registerBroadcastReceiver();
    }

    @Override
    public void startDaoService() {
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
