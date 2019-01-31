package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;

import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class ProposalCreationPresenter implements ProposalCreationContract.Presenter {

    private ProposalCreationContract.View view;

    @Override
    public void init(ProposalCreationContract.View view) {
        this.view = view;
    }

    @Override
    public void createProposal(String recipient, String amount, String description, String fullDescHash, String transactionByteCode) {
        String amountInWeiStr = new EthereumPrice(amount, EthereumPrice.Currency.ETHER).inWeiString();
        byte[] bytes = Numeric.hexStringToByteArray(transactionByteCode);
        byte[] txData = new GenerateTransactionData()
                .setMethod("addProposal")
                .putAddress(recipient)
                .putInt(new Uint256(new BigInteger(amountInWeiStr)))
                .putString(description)
                .putString(fullDescHash)
                .putTypeData(new DynamicBytes(bytes))
                .build();
        new CryptoUtils().sendTx(txData);
    }
}
