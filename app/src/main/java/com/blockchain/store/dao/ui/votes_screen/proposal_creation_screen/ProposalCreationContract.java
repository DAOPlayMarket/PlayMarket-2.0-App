package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.widget.EditText;

import com.blockchain.store.dao.data.TokenBalance;
import com.blockchain.store.dao.database.model.Proposal;

public class ProposalCreationContract {

    interface View {

        void setRecipientError(String errorText);

        void setAmountError(String errorText);

        void setDaoTokenBalance(TokenBalance tokenBalance);
    }

    interface Presenter {

        void init(View view);

        void createProposal(Proposal proposal);

        void getDaoTokenBalance();

        boolean isHasNoErrors(String recipient, String amount);
    }

}
