package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import com.blockchain.store.playmarket.PurchaseSDK.entities.TransferObject;
import com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.CryptoPriceResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Hash;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Transaction;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.KeyManager;

import static com.blockchain.store.playmarket.utilities.Constants.GAS_LIMIT;
import static com.blockchain.store.playmarket.utilities.Constants.USER_ETHERSCAN_ID;

/**
 * Created by samsheff on 24/08/2017.
 */

public class CryptoUtils {

    public static KeyManager keyManager;
    public static EthDroid ethdroid;

    public static KeyManager setupKeyManager(String dataDir) {
        return KeyManager.newKeyManager(dataDir);
    }

    public static String getRawTransaction(Transaction transaction) {
        String transactionInfo = transaction.toString();
        Pattern pattern = Pattern.compile("Hex:.*");
        Matcher matcher = pattern.matcher(transactionInfo);
        if (matcher.find()) {
            String s = matcher.group(0).replaceAll("Hex:\\s*", "");
            return s;
        } else {
            return "";
        }
    }

    public static byte[] getDataForReviewAnApp(String appId, String address, String vote, String description, String txIndex) {
        ArrayList<Type> arrayList = new ArrayList<>();
        arrayList.add(new Uint(new BigInteger(appId)));
        arrayList.add(new Uint(new BigInteger(vote)));
        arrayList.add(new Utf8String(description));
        if (txIndex.isEmpty()) {
            arrayList.add(Bytes32.DEFAULT);
        } else {
            byte[] bytes = Numeric.hexStringToByteArray(txIndex);
            arrayList.add(new Bytes32(bytes));
        }

        List<TypeReference<?>> typeReferences = Arrays.asList(
                new TypeReference<Uint>() {
                },
                new TypeReference<Uint>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Bytes32>() {
                }
        );

        Function function = new Function("feedbackRating",
                arrayList, typeReferences);

        String encode = FunctionEncoder.encode(function);
        Log.d("test", "generateAppBuyTransaction: " + encode);
        if (encode.startsWith("0x")) {
            encode = encode.replaceFirst("0x", "");
        }
        return hexStringToByteArray(encode);
    }

    public static byte[] createSentTokenTransactionBytes(String address, String tokenNumber) {
        if (address.startsWith("0x")) {
            address = address.replaceFirst("0x", "");
        }
        ArrayList<Type> valueList = new ArrayList<>();
        valueList.add(new org.web3j.abi.datatypes.Address(address));
        valueList.add(new Uint256(new BigInteger(tokenNumber)));

        List<TypeReference<?>> typeReferences = Arrays.asList(
                new TypeReference<org.web3j.abi.datatypes.Address>() {
                }, new TypeReference<org.web3j.abi.datatypes.Uint>() {
                });
        Function function = new Function("transfer",
                valueList, typeReferences);

        String encode = FunctionEncoder.encode(function);

        if (encode.startsWith("0x")) {
            encode = encode.replaceFirst("0x", "");
        }
        Log.d("token transfer", "result = " + encode);
        return hexStringToByteArray(encode);
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        Log.d("Ether", "hexStringToByteArray: " + b);
        return b;
    }

    public static String generateAppBuyTransaction(int nonce, BigInt gasPrice, App app, String adrNode, CryptoPriceResponse cryptoPriceResponse) throws Exception {
        Account account = AccountManager.getAccount();

        BigInt price = new BigInt(0);
        byte[] transactionData = new GenerateTransactionData().setMethod("buyAppObj")
                .putTypeData(new Uint256(Long.parseLong(app.appId)))
                .putTypeData(new org.web3j.abi.datatypes.Address(adrNode))
                .putTypeData(new Uint256(0))
                .putTypeData(new Uint256(Long.parseLong(app.getCountOfPmcWithDecimals())))
                .build();

        price.setString(cryptoPriceResponse.price, 10);

        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS),
                price, GAS_LIMIT, gasPrice,
                transactionData);

        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateTransferTransaction(int nonce, String gasPrice, String transferAmount, String recipientAddress) throws Exception {
        BigInt price = new BigInt(0);
        price.setString(transferAmount, 10);

        Account account = AccountManager.getAccount();

        Transaction transaction = new Transaction(nonce, new Address(recipientAddress),
                price, GAS_LIMIT, new BigInt(Long.parseLong(gasPrice)), null);
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
        String hash = signedTransaction.getHash().getHex();
        String sigHash = signedTransaction.getSigHash().getHex();
        return getRawTransaction(signedTransaction);
    }

    public static String generateSendReviewTransaction(int nonce, BigInt gasPrice, App app, String vote, String description, String txIndex) throws Exception {
        Account account = AccountManager.getAccount();
        BigInt price = new BigInt(0);

        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS),
                price, GAS_LIMIT, gasPrice,
                getDataForReviewAnApp(app.appId, account.getAddress().getHex(), vote, description, txIndex));
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
        String hash = signedTransaction.getHash().getHex();
        String sigHash = signedTransaction.getSigHash().getHex();
        return getRawTransaction(signedTransaction);
    }

    public static String generateSendTokenTransaction(int nonce, BigInt gasPrice, String transferAmount, String recipientAddress, String icoAddress) throws Exception {
        Account account = AccountManager.getAccount();

        BigInt price = new BigInt(0);
        Transaction transaction = new Transaction(nonce, new Address(icoAddress),
                price, GAS_LIMIT, gasPrice,
                createSentTokenTransactionBytes(recipientAddress, transferAmount));

        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
        return getRawTransaction(signedTransaction);

    }

    public static String generateRemoteBuyTransaction(AccountInfoResponse accountInfo, TransferObject transferObject) throws Exception {
        KeyStore keystore = AccountManager.getKeyManager().getKeystore();
        Account account = AccountManager.getAccount();
        keystore.timedUnlock(account, transferObject.getPassword(), 20_000);
        double priceDouble = Double.parseDouble(transferObject.getTransferPrice());
        double priceInWei = priceDouble * accountInfo.getCurrentStock();
        BigInt price = new BigInt(0);
        price.setString("" + priceInWei, 10);

        BigInt gasPrice = new BigInt(Long.parseLong(accountInfo.gasPrice));
        int nonce = accountInfo.count;

        byte[] transactionData = null;
        switch (transferObject.getTransactionType()) {
            case RemoteConstants.TRANSACTION_BUY:
                transactionData = new GenerateTransactionData().setAsBuyTransaction(transferObject, accountInfo);
                break;
            case RemoteConstants.TRANSACTION_BUY_OBJECT:
                transactionData = new GenerateTransactionData().setAsBuyObjTransaction(transferObject, accountInfo);
                break;
            case RemoteConstants.TRANSACTION_BUY_OBJECT_WITH_PRICE_CHECK:
                transactionData = new GenerateTransactionData().setAsBuyObjWithPriceCheckTransaction(transferObject, accountInfo);
                break;
            case RemoteConstants.TRANSACTION_BUY_SUB:
                transactionData = new GenerateTransactionData().setAsBuySubTransaction(transferObject, accountInfo);
                break;
            case RemoteConstants.TRANSACTION_BUY_SUB_WITH_PRICE:
                transactionData = new GenerateTransactionData().setAsBuySubWithPriceCheckTransaction(transferObject, accountInfo);
                break;

            default:
        }


        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS), price, GAS_LIMIT, gasPrice, transactionData);
        transaction = keystore.signTx(account, transaction, new BigInt(USER_ETHERSCAN_ID));
        return getRawTransaction(transaction);

    }

    //     * buyAppObj(uint245 app, address node, uint256 obj)
   /*Transactions need to add:
    По цене:
    Сама транзакция платная.
    в дата - цену в УЕ.
     * buy(uint256 app. address node, uint256 obj ) // 50 цена id 0
     * buyAppObj(uint245 app, address node, uint256 obj)
     * buyAppObj(uint256 app. address node, uint256 obj,unit256  _price)
     * buyAppSub(unit256 _app, address _node, uint256 obj, unit256 _price)
     * buyAppSub(uint256 app, address node, uint256 obj)
    * obj = 0 - если приложение
    * obg = 1 - идентификатор
    * */


}

