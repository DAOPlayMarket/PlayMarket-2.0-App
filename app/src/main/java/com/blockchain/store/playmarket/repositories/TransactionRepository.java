package com.blockchain.store.playmarket.repositories;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class TransactionRepository {


    public static void test(String contractAddress, String userAddress) {
        contractAddress = "0x0af44e2784637218dd1d32a322d44e603a8f0c6a";
        Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));

        List<TypeReference<?>> typeReferences = Arrays.asList(new TypeReference<Utf8String>() {
        });

        ArrayList<Type> valueList = new ArrayList<>();
        valueList.add(new org.web3j.abi.datatypes.Address(""));
        //
        Function function = new Function("name", new ArrayList<>(), typeReferences);
//        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getVale(), function.getOutputParameters());

        String encode = FunctionEncoder.encode(function);
        build.ethCall(
                createEthCallTransaction("", contractAddress, encode), DefaultBlockParameterName.LATEST).observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    private static void mapWithGetSymbol(EthCall ethCall) {
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        String name = (String) decode.get(0).getValue();

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
