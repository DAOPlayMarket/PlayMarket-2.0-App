package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import com.blockchain.store.dao.database.model.Proposal;

public class ProposalCreationContract {

    interface View {

    }

    interface Presenter {

        void init(View view);

        void createProposal(Proposal proposal);
    }

}
