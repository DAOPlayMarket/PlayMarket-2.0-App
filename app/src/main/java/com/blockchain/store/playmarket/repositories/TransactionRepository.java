package com.blockchain.store.playmarket.repositories;

import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.data.entities.IcoLocalData;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.WebAppInterface;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.StaticArray8;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA_RINKEBY;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class TransactionRepository {
    private static String contractAddress;
    private static String userAddress;
    private static Web3j web3j;

    private static void init(String contractAddress, String userAddress) {
        TransactionRepository.web3j = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
        TransactionRepository.contractAddress = contractAddress;
        TransactionRepository.userAddress = userAddress;
    }

    private static void initAsRinkeby(String contractAddress, String userAddress) {
        TransactionRepository.web3j = Web3jFactory.build(new HttpService(BASE_URL_INFURA_RINKEBY));
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
                    token.address = contractAddress;

                    return token;
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
    }

    public static Observable<String> getUserTokenBalance(String contractAddress, String userAddress) {
        init(contractAddress, userAddress);
        return getBalanceOfObservable().map(result -> decodeFunction(result, getBalanceOfFunction(userAddress))
                .toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    public static Observable<IcoBalance> getIcoBalance(String contractAddress, String userAddress) {
        init(contractAddress, userAddress);
        return Observable.zip(getBalanceOfObservable(), getDecimalsObservable(), (balance, decimals) -> {
            IcoBalance icoBalance = new IcoBalance();
            icoBalance.decimals = decodeFunction(decimals, getDecimalsFunction()).toString();
            icoBalance.balanceOf = decodeFunction(balance, getBalanceOfFunction(userAddress)).toString();
            icoBalance.address = contractAddress;
            return icoBalance;
        });
    }

    public static Observable<Boolean> getCheckBuy(int appId, String objectId) {
        init(Constants.PLAY_MARKET_ADDRESS, AccountManager.getAddress().getHex());
        return getCheckBuyObservable(appId, objectId)
                .map(result -> (Boolean) decodeFunction(result, getCheckBuyFunction(appId, objectId)));
    }

    public static Observable<BigInteger> getSubscriptionTime(int appId, String objectId) {
        init(Constants.PLAY_MARKET_ADDRESS, AccountManager.getAddress().getHex());
        return getSubscriptionObservable(appId, objectId)
                .map(result -> (BigInteger) decodeFunction(result, getTimeSubscriptionFunction(appId, objectId)));
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

    private static Observable<EthCall> getCheckBuyObservable(int appId, String objectId) {
        return getEthCallObservable(getCheckBuyFunction(appId, objectId));
    }

    private static Observable<EthCall> getSubscriptionObservable(int appId, String objectId) {
        return getEthCallObservable(getTimeSubscriptionFunction(appId, objectId));
    }

    private static Object decodeFunction(EthCall ethCall, Function function) {
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (decode.size() > 0) {
            return decode.get(0).getValue();
        } else {
            return "0";
        }
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

    private static Function getCheckBuyFunction(int appId, String objectId) {
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(appId));
        inputParameters.add(new Address(userAddress));
        inputParameters.add(new Uint256(Long.valueOf(objectId)));
        return new Function("getBuyObject", inputParameters, Collections.singletonList(new TypeReference<Bool>() {
        }));
    }

    private static Function getTimeSubscriptionFunction(int appId, String objectId) {
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(appId));
        inputParameters.add(new Address(userAddress));
        inputParameters.add(new Uint256(Long.valueOf(objectId)));
        return new Function("getTimeSubscription", inputParameters, Collections.singletonList(new TypeReference<Uint>() {
        }));
    }

    public static Observable<IcoLocalData> getLocalIcoData(String contractAddress, String userAddress) {
        initAsRinkeby(contractAddress, userAddress);

        return Observable.zip(
                getEthCallObservable(startsAtFunction()),
                getEthCallObservable(durationOfPeriodFunction()),
                getEthCallObservable(tokensInPeriodFunction()),
                getEthCallObservable(numberOfPeriodsFunction()), (startsAt, durationOfPeriod, tokensInPeriod, numberOfPeriods) -> {
                    IcoLocalData icoLocalData = new IcoLocalData();
                    icoLocalData.startsAt = decodeFunction(startsAt, startsAtFunction()).toString();
                    icoLocalData.durationOfPeriod = decodeFunction(durationOfPeriod, durationOfPeriodFunction()).toString();
                    icoLocalData.tokensInPeriod = decodeFunction(tokensInPeriod, tokensInPeriodFunction()).toString();
                    icoLocalData.numberOfPeriods = decodeFunction(numberOfPeriods, numberOfPeriodsFunction()).toString();
                    return icoLocalData;
                }).flatMap(result -> getEthCallObservable(pricesFunction(result.getCurrentPeriod())), Pair::new)
                .map(result -> {
                    result.first.price.add(decodeFunction(result.second, pricesFunction(result.first.getCurrentPeriod())).toString());
                    return result.first;
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
    }


    private static Function startsAtFunction() {
        return new Function("startsAt", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function pricesFunction(int pricePosition) {
        return new Function("price", Collections.singletonList(new Uint256(pricePosition)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function durationOfPeriodFunction() {
        return new Function("durationOfPeriod", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function tokensInPeriodFunction() {
        return new Function("TokensInPeriod", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function numberOfPeriodsFunction() {
        return new Function("numberOfPeriods", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }


}
