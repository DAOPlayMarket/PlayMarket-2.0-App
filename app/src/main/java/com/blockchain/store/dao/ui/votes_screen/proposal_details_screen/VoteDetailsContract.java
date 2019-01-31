package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import android.util.Pair;

import com.blockchain.store.dao.database.model.Rules;

class VoteDetailsContract {

    interface View {

        void setDaoTokenBalance(Pair<String, String> tokenBalancePair);

    }

    interface Presenter {

        void init(View view);

        Rules getRules();

        String obtainPercentage(String value, String maxValue);

        void getDaoTokenBalance();

        void votingForProposal(int id, boolean isSupport, String justificationText);

        void executeProposal(int id, String transactionByteCode);

        double getTokenDecimals(long tokenBalance);
    }

}
