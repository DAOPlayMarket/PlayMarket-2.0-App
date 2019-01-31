package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import android.util.Pair;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.dao.repository.DaoTokenRepository;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;

public class VoteDetailsPresenter implements VoteDetailsContract.Presenter, Callbacks.DaoTokenCallback {

    private VoteDetailsContract.View view;
    private DaoDatabase daoDatabase;
    private DaoTokenRepository daoTokenRepository;

    @Override
    public void init(VoteDetailsContract.View view) {
        this.view = view;
        daoDatabase = Application.getDaoDatabase();
        daoTokenRepository = Application.getDaoTokenRepository();
    }

    @Override
    public Rules getRules() {
        return daoDatabase.rulesDao().getRules();
    }

    @Override
    public String obtainPercentage(String value, String maxValue) {
        String result = String.valueOf(Double.valueOf(value) / Double.valueOf(maxValue) * 100);
        return String.valueOf(Math.round(Double.valueOf(result)));
    }

    @Override
    public void getDaoTokenBalance() {
        daoTokenRepository.getDaoTokenBalance(this);
    }

    @Override
    public void votingForProposal(int id, boolean isSupport, String justificationText) {
        byte[] txData = new GenerateTransactionData()
                .setMethod("vote")
                .putInt(new Uint256(id))
                .putTypeData(new Bool(isSupport))
                .putString(justificationText)
                .build();
        new CryptoUtils().sendTx(txData);
    }

    @Override
    public double getTokenDecimals(long tokenBalance) {
        if (tokenBalance == 0) {
            return 0;
        } else {
            return (double) tokenBalance / Math.pow(10, 4);
        }
    }

    @Override
    public void executeProposal(int id, String transactionByteCode) {
        byte[] txData = new GenerateTransactionData()
                .setMethod("executeProposal")
                .putInt(new Uint256(id))
                .putTypeData(new DynamicBytes(transactionByteCode.getBytes()))
                .build();
        new CryptoUtils().sendTx(txData);
    }

    @Override
    public void onBalanceReady(Pair<String, String> tokenBalancePair) {
        view.setDaoTokenBalance(tokenBalancePair);
    }

    @Override
    public void onBalanceFailed(Throwable throwable) {

    }
}
