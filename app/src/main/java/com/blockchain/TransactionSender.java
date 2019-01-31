package com.blockchain;

import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import org.ethereum.geth.Transaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction;

public class TransactionSender {
    public void test(Transaction transaction) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
                    String numericData = Numeric.toHexString(transaction.getData());
                    org.web3j.protocol.core.methods.request.Transaction tx = createFunctionCallTransaction(
                            AccountManager.getAddress().getHex(),
                            new BigInteger(String.valueOf(result.count)),
                            new BigInteger(result.getGasPrice()),
                            new BigInteger(String.valueOf(Constants.GAS_LIMIT)),
                            DaoConstants.Repository,
                            numericData);
                    return build.ethEstimateGas(tx).observable();

                })
                .map(this::mapEstimateResult)
//                .flatMap(result -> {
//
//                    RestApi.getServerApi().deployTransaction(transaction)
//                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public EthEstimateGas mapEstimateResult(EthEstimateGas result) {
        if (result.getError() == null) {
            return result;
        } else {
            throw new RuntimeException(result.getError().getMessage());
        }
    }
}
