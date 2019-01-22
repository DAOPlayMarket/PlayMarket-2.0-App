package com.blockchain.store.dao.interfaces;


import android.support.v7.app.AlertDialog;

import com.blockchain.store.dao.database.model.Proposal;

public abstract class Callbacks {

    public interface ProposalCallback {

        void onItemClicked(Proposal proposal);

    }

    public interface PasswordCallback {

        void onAccountUnlock(Boolean isUnlock);

    }

    public interface VotesCallback {

        void OnRecyclerViewScrolled(int y);

    }

}
