package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.widget.EditText;

import com.blockchain.store.dao.database.model.Proposal;

public class ProposalCreationContract {

    interface View {

        void setRecipientError(String errorText);

        void setAmountError(String errorText);

    }

    interface Presenter {

        void init(View view);

        void createProposal(Proposal proposal);

        boolean isHasNoErrors(String recipient, String amount);
    }

}
