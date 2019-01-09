package com.blockchain.store.dao.ui.dao_activity;

public class DaoActivityPresenter implements DaoActivityContract.DaoPresenter {
    private DaoActivityContract.DaoView view;

    @Override
    public void init(DaoActivityContract.DaoView view) {
        this.view = view;
    }
}
