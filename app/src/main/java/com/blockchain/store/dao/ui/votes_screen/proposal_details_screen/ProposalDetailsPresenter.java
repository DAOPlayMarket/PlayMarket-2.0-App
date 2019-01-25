package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.playmarket.Application;

public class ProposalDetailsPresenter implements ProposalDetailsContract.Presenter {

    private ProposalDetailsContract.View view;
    private DaoDatabase daoDatabase;

    @Override
    public void init(ProposalDetailsContract.View view) {
        this.view = view;
        daoDatabase = Application.getDaoDatabase();
    }

    @Override
    public Rules getRules() {
        return daoDatabase.rulesDao().getRules();
    }

    @Override
    public String obtainPercentage(long value, long maxValue) {
        return String.valueOf(value / maxValue * 100);
    }
}
