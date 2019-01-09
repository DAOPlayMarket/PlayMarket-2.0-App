package com.blockchain.store.dao.repository;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;

public class DaoTransactionRepository {
    private static String contractAddress;
    private static String userAddress;
    private static Web3j web3j;
    private static boolean isHasLogs = true;


    private static void init(String contractAddress, String userAddress) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        if (isHasLogs) {
            builder.addInterceptor(interceptor);
        }
        OkHttpClient build = builder.build();
        Web3jService web3jService = new HttpService(BASE_URL_INFURA, build, false);

        DaoTransactionRepository.web3j = Web3jFactory.build(web3jService);
        DaoTransactionRepository.contractAddress = contractAddress;
        DaoTransactionRepository.userAddress = userAddress;
    }

    private static Function makeDepositRepository(int appId, String objectId) {
        ArrayList<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Uint256(appId));
        inputParameters.add(new Address(userAddress));
        inputParameters.add(new Uint256(Long.valueOf(objectId)));
        return new Function("makeDeposit", inputParameters, Collections.singletonList(new TypeReference<Utf8String>() {
        }));
    }


}
