package com.blockchain.store.dao.ui.votes_screen.voting_screen;

import com.blockchain.store.dao.data.TokenBalance;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;

class VotingContract {

    interface View {

        void setDaoTokenBalance(TokenBalance tokenBalance);

    }

    interface Presenter {

        void init(View view);

        Rules getRules();

        String obtainPercentage(String value, String maxValue);

        void getDaoTokenBalance();

        void votingForProposal(int id, boolean isSupport, String justificationText);

        void executeProposal(Proposal proposal);

        double getTokenDecimals(long tokenBalance);
    }

}
