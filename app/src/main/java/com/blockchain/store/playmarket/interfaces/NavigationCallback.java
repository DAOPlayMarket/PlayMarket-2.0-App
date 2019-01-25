package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.database.model.Proposal;

public interface NavigationCallback {

    void onVotesClicked();

    void onWalletClicked();

    void onDividendsClicked();

    void onNewProposalClicked();

    void onProposalDetailsClicked(Proposal proposal);

    void onTokenTransferClicked(DaoToken daoToken);

    void onChangeAccountClicked();

    void onAddTokenClicked();
}
