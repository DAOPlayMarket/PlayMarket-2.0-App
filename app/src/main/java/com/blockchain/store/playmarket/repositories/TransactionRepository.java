package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.data.entities.Token;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.Collections;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class TransactionRepository {
    private static String contractAddress;
    private static String userAddress;
    private static Web3j web3j;

    private static void init(String contractAddress, String userAddress) {
        if (web3j == null) {
            TransactionRepository.web3j = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        }
        TransactionRepository.contractAddress = contractAddress;
        TransactionRepository.userAddress = userAddress;
    }

    public static Observable<Token> getTokenFullInfo(String contractAddress, String userAddress) {
        init(contractAddress, userAddress);
        return Observable.zip(getNameObservable(), getSymbolObservable(), getDecimalsObservable(), getBalanceOfObservable(),
                (nameCall, symbolCall, decimalsCall, balanceOfCall) -> {
                    Token token = new Token();
                    token.name = decodeFunction(nameCall, getNameFunction()).toString();
                    token.symbol = decodeFunction(symbolCall, getSymbolFunction()).toString();
                    token.decimals = decodeFunction(decimalsCall, getDecimalsFunction()).toString();
                    token.contract = TransactionRepository.contractAddress;
                    token.balanceOf = decodeFunction(balanceOfCall, getBalanceOfFunction(TransactionRepository.userAddress)).toString();
                    return token;
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
    }

    public static Observable<EthCall> getTokenBalanceOf(String contractAddress, String userAddress) {
        init(contractAddress, userAddress);
        return getBalanceOfObservable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
    }

    private static Observable<EthCall> getNameObservable() {
        return getEthCallObservable(getNameFunction());
    }

    private static Observable<EthCall> getSymbolObservable() {
        return getEthCallObservable(getSymbolFunction());
    }

    private static Observable<EthCall> getDecimalsObservable() {
        return getEthCallObservable(getDecimalsFunction());
    }

    private static Observable<EthCall> getBalanceOfObservable() {
        return getEthCallObservable(getBalanceOfFunction(userAddress));
    }

    private static Observable<EthCall> getEthCallObservable(Function dataFunction) {
        return web3j.ethCall(createEthCallTransaction(TransactionRepository.userAddress, contractAddress, FunctionEncoder.encode(dataFunction)), DefaultBlockParameterName.LATEST).observable();
    }

    private static Object decodeFunction(EthCall ethCall, Function function) {
        return FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue();
    }

    private static Function getSymbolFunction() {
        return new Function("symbol", new ArrayList<>(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
    }

    private static Function getNameFunction() {
        return new Function("name", new ArrayList<>(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
    }

    private static Function getDecimalsFunction() {
        return new Function("decimals", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint8>() {
        }));
    }

    private static Function getBalanceOfFunction(String userAddress) {
        return new Function("balanceOf", Collections.singletonList(new Address(userAddress)), Collections.singletonList(new TypeReference<Uint>() {
        }));
    }

}
