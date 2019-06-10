package com.blockchain;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
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
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;
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
                        return result.getTransactionHash();
                    } else {
                        throw new IllegalArgumentException(result.getError().getMessage());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<EthEstimateGas> mapWithEstimateGas(Transaction notSignedTransaction) {
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        String numericData = null;
        try {
            numericData = Numeric.toHexString(notSignedTransaction.getData());
        } catch (Exception e) {
            Log.d(TAG, "mapWithEstimateGas: ");
        }

        createEthCallTransaction(
                AccountManager.getAddress().getHex(),
                notSignedTransaction.getTo().getHex(),
        numericData);

        org.web3j.protocol.core.methods.request.Transaction tx =  createEthCallTransaction(
                "0xEA674fdDe714fd979de3EdF0F56AA9716B898ec8",
                notSignedTransaction.getTo().getHex(),
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
            Transaction transaction = new Transaction(notSignedTx.getNonce(),
                    notSignedTx.getTo(),
                    notSignedTx.getValue(),
                    gasLimit.longValue(),
                    notSignedTx.getGasPrice(),
                    notSignedTx.getData());


            Account account = AccountManager.getAccount();
            Transaction signedTx = CryptoUtils.keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
            return web3j.ethSendRawTransaction("0x" + CryptoUtils.getRawTransaction(signedTx)).observable();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
