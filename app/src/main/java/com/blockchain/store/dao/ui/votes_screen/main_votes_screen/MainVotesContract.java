package com.blockchain.store.dao.ui.votes_screen.main_votes_screen;

import android.content.Context;

import com.blockchain.store.dao.database.model.Proposal;

import java.util.List;

public class MainVotesContract {

    public interface View {

        void initTabLayout(List<Proposal> proposals);

        void hideProgressBar();

    }

    public interface Presenter {

        void init(View view, Context context);

        void startDaoService();

        void stopDaoService();

        void unregisterBroadcastReceiver();

    }

}
