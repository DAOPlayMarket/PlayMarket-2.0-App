package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.dao.database.model.Proposal;

public interface NavigationCallback {

    void onVotesClicked();

    void onWalletClicked();

    void onNewProposalClicked();

    void onProposalDetailsClicked(Proposal proposal);

}
