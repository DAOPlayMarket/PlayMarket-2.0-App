package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.content.Context;

import com.blockchain.store.dao.data.entities.ProposalDescriptions;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;
import com.google.gson.Gson;

import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProposalCreationPresenter implements ProposalCreationContract.Presenter {

    private ProposalCreationContract.View view;

    @Override
    public void init(ProposalCreationContract.View view) {
        this.view = view;
    }

    @Override
    public void createProposal(Proposal proposal) {
        if (!proposal.description.isEmpty() || !proposal.transactionBytecode.isEmpty()) {
            String jsonData = new Gson().toJson(new ProposalDescriptions(proposal.description, proposal.transactionBytecode));
            RestApi.getServerApi().getFullDescriptionHash(jsonData)
                    .map(result -> {
                        proposal.fullDescriptionHash = result.Hash;
                        return proposal;
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getHashSuccessful, this::getHashFailed);
        } else {
            proposal.fullDescriptionHash = "";
            getHashSuccessful(proposal);
        }
    }

    private void getHashSuccessful(Proposal proposal) {
        String amountInWeiStr = new EthereumPrice(String.valueOf(proposal.amount), EthereumPrice.Currency.ETHER).inWeiString();
        byte[] bytes = Numeric.hexStringToByteArray(proposal.transactionBytecode);
        byte[] txData = new GenerateTransactionData()
                .setMethod("addProposal")
                .putAddress(proposal.recipient)
                .putInt(new Uint256(new BigInteger(amountInWeiStr)))
                .putString(proposal.description)
                .putString(proposal.fullDescriptionHash)
                .putTypeData(new DynamicBytes(bytes))
                .build();
        new CryptoUtils().sendTx(txData)
                .map(result-> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.CREATE_PROPOSAL);
                    return result;
                }).subscribe(this::onTransactionSend,this::onTransactionFailed);

    }

    private void onTransactionSend(String string) {

    }

    private void onTransactionFailed(Throwable throwable) {

    }


    @Override
    public boolean isHasNoErrors(String recipient, String amount) {
        Context context = Application.getInstance().getApplicationContext();
        boolean hasNoErrors = true;
        if (recipient.isEmpty()) {
            view.setRecipientError(context.getString(R.string.empty_field));
            hasNoErrors = false;
        } else {
            if (recipient.length() != 42) {
                view.setRecipientError(context.getString(R.string.incorrect_address));
                hasNoErrors = false;
            } else view.setRecipientError("");
        }
        if (amount.isEmpty()) {
            view.setRecipientError(context.getString(R.string.empty_field));
            hasNoErrors = false;
        } else view.setAmountError("");

        return hasNoErrors;
    }

    private void getHashFailed(Throwable throwable) {
        throwable.printStackTrace();
    }
}
