package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

public class ProposalCreationContract {

    interface View {

    }

    interface Presenter {

        void init(View view);

        void createProposal(String recipient, String amount, String description, String fullDescHash, String transactionByteCode);

    }

}
