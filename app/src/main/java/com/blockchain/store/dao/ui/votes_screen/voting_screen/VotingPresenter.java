package com.blockchain.store.dao.ui.votes_screen.voting_screen;

import android.util.Pair;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.dao.repository.DaoTokenRepository;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

public class VotingPresenter implements VotingContract.Presenter, Callbacks.DaoTokenCallback {

    private VotingContract.View view;
    private DaoDatabase daoDatabase;
    private DaoTokenRepository daoTokenRepository;

    @Override
    public void init(VotingContract.View view) {
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
        new CryptoUtils().sendTx(txData)
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.VOTE_FOR_PROPOSAL);
                    return result;
                }).subscribe(this::onTransactionSend, this::onTransactionFailed);
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
    public void executeProposal(Proposal proposal) {
        byte[] bytes = Numeric.hexStringToByteArray(proposal.transactionBytecode);
        byte[] txData = new GenerateTransactionData()
                .setMethod("executeProposal")
                .putInt(new Uint256(proposal.proposalID))
                .putTypeData(new DynamicBytes(bytes))
                .build();
        new CryptoUtils().sendTx(txData)
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.EXECUTE_RPOPOSAL);
                    return result;
                }).subscribe(this::onTransactionSend, this::onTransactionFailed);
    }

    @Override
    public void onBalanceReady(Pair<String, String> tokenBalancePair) {
        view.setDaoTokenBalance(tokenBalancePair);
    }

    @Override
    public void onBalanceFailed(Throwable throwable) {

    }

    private void onTransactionFailed(Throwable throwable) {

    }

    private void onTransactionSend(String s) {

    }

}
