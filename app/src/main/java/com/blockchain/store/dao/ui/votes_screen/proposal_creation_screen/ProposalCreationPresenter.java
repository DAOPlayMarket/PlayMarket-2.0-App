package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import com.blockchain.store.dao.data.entities.ProposalDescriptions;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;
import com.google.gson.Gson;

import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

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
        if (!proposal.description.isEmpty() || !proposal.transactionBytecode.isEmpty()){
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
        new CryptoUtils().sendTx(txData);
    }

    private void getHashFailed(Throwable throwable) {
        throwable.printStackTrace();
    }
}
