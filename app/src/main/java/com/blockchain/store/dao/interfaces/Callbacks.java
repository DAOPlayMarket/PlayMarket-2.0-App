package com.blockchain.store.dao.interfaces;

import com.blockchain.store.dao.database.model.Proposal;

public abstract class Callbacks {

    public interface ProposalCallback {

        void onItemClicked(Proposal proposal);

    }

}
