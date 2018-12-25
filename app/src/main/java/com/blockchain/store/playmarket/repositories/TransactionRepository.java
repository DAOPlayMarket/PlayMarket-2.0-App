package com.blockchain.store.playmarket.repositories;

import android.util.Pair;

import com.blockchain.store.playmarket.data.entities.IcoBalance;
import com.blockchain.store.playmarket.data.entities.IcoLocalData;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor);
        OkHttpClient build = builder.build();
        Web3jService web3jService = new HttpService(BASE_URL_INFURA, build, false);

        TransactionRepository.web3j = Web3jFactory.build(web3jService);
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
        return getEthCallObservable(getBalanceOfFunction(Constants.CRYPTO_DUEL_CONTRACT_FOR_ADVER_BUDGET));
    }

    private static Observable<EthCall> getEthCallObservable(Function dataFunction) {
        return web3j.ethCall(createEthCallTransaction(TransactionRepository.userAddress, contractAddress, FunctionEncoder.encode(dataFunction)), DefaultBlockParameterName.LATEST).observable();
    }

    private static Observable<EthGetBalance> getBalanceObservable(String address) {
        return web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).observable();
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
                getBalanceObservable(userAddress),
                getEthCallObservable(startsAtFunction()),
                getEthCallObservable(durationOfPeriodFunction()),
                getEthCallObservable(tokensInPeriodFunction()),
                getEthCallObservable(numberOfPeriodsFunction()), (contractBalance, startsAt, durationOfPeriod, tokensInPeriod, numberOfPeriods) -> {
                    IcoLocalData icoLocalData = new IcoLocalData();
                    icoLocalData.startsAt = decodeFunction(startsAt, startsAtFunction()).toString();
                    icoLocalData.durationOfPeriod = decodeFunction(durationOfPeriod, durationOfPeriodFunction()).toString();
                    icoLocalData.tokensInPeriod = decodeFunction(tokensInPeriod, tokensInPeriodFunction()).toString();
                    icoLocalData.numberOfPeriods = decodeFunction(numberOfPeriods, numberOfPeriodsFunction()).toString();
                    icoLocalData.adverBudget = contractBalance.getBalance();
                    return icoLocalData;
                }).flatMap(result -> mapGetPrice(result.getCurrentPeriod()), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.second.size(); i++) {
                        result.first.earnedInPeriod.add(decodeFunction(result.second.get(i), pricesFunction(result.first.getCurrentPeriod())).toString());
                    }
                    return result.first;
                }).flatMap(result -> mapTokenAmountOfPeriod(result.getCurrentPeriod()), Pair::new)
                .map(result -> {
                    for (int i = 0; i < result.second.size(); i++) {
                        result.first.tokenEarnedInPeriod.add(decodeFunction(result.second.get(i), tokenAmountOfPeriodFunction(result.first.getCurrentPeriod())).toString());
                    }
                    return result.first;
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
    }

    private static Observable<List<EthCall>> mapGetPrice(int currentPeriod) {
        currentPeriod++;
        ArrayList<Observable<EthCall>> pricesList = new ArrayList<>();
        for (int i = 1; i <= currentPeriod; i++) {
            pricesList.add(getCustomEthCall(rewardFunction(i), Constants.CRYPTO_DUEL_CONTRACT));
        }
        return Observable.from(pricesList).flatMap(result -> result).toList();
    }

    private static Observable<List<EthCall>> mapEarned(int currentPeriod) {
        ArrayList<Observable<EthCall>> pricesList = new ArrayList<>();
        for (int i = 0; i <= currentPeriod; i++) {
            pricesList.add(getEthCallObservable(earnedFunction(i)));
        }
        return Observable.from(pricesList).flatMap(result -> result).toList();
    }

    private static Observable<List<EthCall>> mapTokenAmountOfPeriod(int currentPeriod) {
        ArrayList<Observable<EthCall>> pricesList = new ArrayList<>();
        for (int i = 0; i <= currentPeriod; i++) {
            pricesList.add(getEthCallObservable(tokenAmountOfPeriodFunction(i)));
        }
        return Observable.from(pricesList).flatMap(result -> result).toList();
    }


    /*
    *       ArrayList<Observable<String>> obsList = new ArrayList<>();
        for (Token userSavedToken : userSavedTokens) {
            obsList.add(TransactionRepository.getUserTokenBalance(userSavedToken.address, AccountManager.getAddress().getHex()));
        }
        return Observable.from(obsList).flatMap(result -> result.observeOn(Schedulers.newThread())).toList().map(result -> {
    * */

    private static Function startsAtFunction() {
        return new Function("startsAt", new ArrayList<>(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function pricesFunction(int stageNumber) {
        return new Function("price", Collections.singletonList(new Uint256(stageNumber)), Collections.singletonList(new TypeReference<Uint256>() {
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

    private static Function tokenAmountOfPeriodFunction(int stageNumber) {
        return new Function("tokenAmountOfPeriod", Collections.singletonList(new Uint256(stageNumber)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function earnedFunction(int stageNumber) {
        return new Function("earned", Collections.singletonList(new Uint256(stageNumber)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Function rewardFunction(int previousStageNumber) {
        return new Function("reward", Collections.singletonList(new Uint256(previousStageNumber)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
    }

    private static Observable<EthCall> getCustomEthCall(Function dataFunction, String contractAddress) {
        return web3j.ethCall(createEthCallTransaction(TransactionRepository.userAddress, contractAddress, FunctionEncoder.encode(dataFunction)), DefaultBlockParameterName.LATEST).observable();
    }


}
