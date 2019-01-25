package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import com.blockchain.store.dao.database.model.Rules;

class ProposalDetailsContract {

    interface View {

    }

    interface Presenter {

        void init(View view);

        Rules getRules();

        String obtainPercentage(long value, long maxValue);

    }

}
