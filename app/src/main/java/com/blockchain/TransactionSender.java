package com.blockchain;

import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
import org.json.JSONObject;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static com.blockchain.store.playmarket.utilities.Constants.USER_ETHERSCAN_ID;
import static org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction;

public class TransactionSender {
    private static final String TAG = "TransactionSender";
    private Web3j web3j;

    public Observable<String> send(Transaction notSignedTransaction) {
        web3j = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        return mapWithEstimateGas(notSignedTransaction)
                .map(this::mapEstimateResult)
                .map(result -> mapWithAddGasLimitToTx(result, notSignedTransaction))
                .flatMap(result -> result)
                .map(result -> {
                    if (result.getError() == null) {
                        throw new IllegalArgumentException(result.getError().getMessage());
                    } else {
                        return result.getTransactionHash();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<EthEstimateGas> mapWithEstimateGas(Transaction notSignedTransaction) {
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        String numericData = Numeric.toHexString(notSignedTransaction.getData());
        org.web3j.protocol.core.methods.request.Transaction tx = createFunctionCallTransaction(
                AccountManager.getAddress().getHex(),
                new BigInteger(String.valueOf(notSignedTransaction.getNonce())),
                new BigInteger(String.valueOf(notSignedTransaction.getGasPrice())),
                new BigInteger(String.valueOf(Constants.GAS_LIMIT)),
                DaoConstants.Repository,
                numericData);
        return build.ethEstimateGas(tx).observable();
    }


    public BigInteger mapEstimateResult(EthEstimateGas result) {
        if (result.getError() == null) {
            BigInteger amountUsed = result.getAmountUsed();
            amountUsed = amountUsed.add(new BigInteger(String.valueOf(Constants.GAS_LIMIT_ADDITION)));
            return amountUsed;
        } else {
            throw new RuntimeException(result.getError().getMessage());
        }
    }

    public Observable<EthSendTransaction> mapWithAddGasLimitToTx(BigInteger gasLimit, Transaction notSignedTx) {
        try {
            String encodedJson = notSignedTx.encodeJSON();
            JSONObject json = new JSONObject(encodedJson);
            json.put("gas", Numeric.toHexStringWithPrefix(gasLimit));
            Transaction transaction = new Transaction(json.toString());
            Account account = AccountManager.getAccount();
            Transaction signedTx = CryptoUtils.keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
            return web3j.ethSendRawTransaction("0x" + CryptoUtils.getRawTransaction(signedTx)).observable();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction mapWithSigningTransaction(Transaction notSignedTransaction) {
        Account account = AccountManager.getAccount();
        try {
            return AccountManager.getKeyStore().signTx(account, notSignedTransaction, new BigInt(USER_ETHERSCAN_ID));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
