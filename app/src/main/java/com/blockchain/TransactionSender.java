package com.blockchain;

import android.util.Log;

import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.google.gson.JsonObject;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.TransactionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
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
    public void test(Transaction notSignedTransaction) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> mapWithEstimateGas(result, notSignedTransaction))
                .map(this::mapEstimateResult)
//                .flatMap(result -> {
//
//                    RestApi.getServerApi().deployTransaction(notSignedTransaction)
//                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<EthEstimateGas> mapWithEstimateGas(AccountInfoResponse result, Transaction notSignedTransaction) {
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        String numericData = Numeric.toHexString(notSignedTransaction.getData());
        org.web3j.protocol.core.methods.request.Transaction tx = createFunctionCallTransaction(
                AccountManager.getAddress().getHex(),
                new BigInteger(String.valueOf(result.count)),
                new BigInteger(result.getGasPrice()),
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

    public void mapWithAddGasLimitToTx(BigInteger gasLimit, Transaction notSignedTransaction){
        try {
            String encodedJson = notSignedTransaction.encodeJSON();
            JSONObject json = new JSONObject(encodedJson);
            Log.d(TAG, "mapWithAddGasLimitToTx: before : " + json);
            json.put("GAS_LIMIT", Numeric.toHexStringWithPrefix(gasLimit));
            Log.d(TAG, "mapWithAddGasLimitToTx: after : " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Transaction mapWithSigningTransaction(Transaction notSignedTransaction){
        Account account = AccountManager.getAccount();
        try {
            return AccountManager.getKeyStore().signTx(account, notSignedTransaction, new BigInt(USER_ETHERSCAN_ID));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
