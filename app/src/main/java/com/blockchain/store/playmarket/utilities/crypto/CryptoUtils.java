package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.Constants;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
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
import static com.blockchain.store.playmarket.utilities.Constants.RINKEBY_ID;
import static org.web3j.crypto.Hash.sha3;

/**
 * Created by samsheff on 24/08/2017.
 */

public class CryptoUtils {

    public static KeyManager keyManager;
    public static EthDroid ethdroid;

    public static String CONTRACT_ADDRESS = "0xf18418d6dc1a2278c69968b8b8a2d84b553fba51";
    public static final String TEST_ADDRESS = "0x5e5c1c8e03472666e0b9e218153869dcbc9c1e65";
    public static final String ICO_CONTRACT_ADDRESS = "0xEDC64A365e12054928dAC9bF32F1C1552EE9679F";


    public static KeyManager setupKeyManager(String dataDir) {
        return KeyManager.newKeyManager(dataDir);
    }

    public static void buildEtherNodeTestnet(String datadir) {
        try {
            keyManager = KeyManager.newKeyManager(datadir);

            ethdroid = new EthDroid.Builder(datadir)
                    .onTestnet()
                    .withDatadirPath(datadir)
                    .withKeyManager(KeyManager.newKeyManager(datadir))
                    .withDefaultContext()
                    .build();
            ethdroid.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRawTransaction(Transaction transaction) {
        String transactionInfo = transaction.toString();
        Pattern pattern = Pattern.compile("Hex:.*");
        Matcher matcher = pattern.matcher(transactionInfo);
        if (matcher.find()) {
            return matcher.group(0).replaceAll("Hex:\\s*", "");
        } else {
            return "";
        }
    }

    public static byte[] getDataForBuyApp(String appId, String address) {
        byte[] hash = sha3("buyApp(uint256,address)".getBytes());
        appId = Integer.toHexString(Integer.valueOf(appId));
        String hashString = bytesToHexString(hash);
        String functionHash = hashString.substring(0, 8);

        Log.d("Ether", functionHash);
        Log.d("Ether", hashString);

        String appIdEnc = String.format("%64s", appId).replace(' ', '0');
        String catIdEnc = String.format("%64s", address).replace(' ', '0');

        Log.d("Ether", appIdEnc);
        Log.d("Ether", catIdEnc);

        String data = functionHash + appIdEnc + catIdEnc;
        Log.d("oldMethod", "result = " + data);

        return hexStringToByteArray(data);
    }

    public static byte[] getDataForBuyAppWithWeb3(String appId, String address) {
        if (address.startsWith("0x")) {
            address = address.replaceFirst("0x", "");
        }
        ArrayList<Type> valueList = new ArrayList<>();
        valueList.add(new Uint(new BigInteger(appId)));
        valueList.add(new org.web3j.abi.datatypes.Address(address));

        List<TypeReference<?>> typeReferences = Arrays.asList(
                new TypeReference<org.web3j.abi.datatypes.Uint>() {
                },
                new TypeReference<org.web3j.abi.datatypes.Address>() {
                });
        Function function = new Function("buyApp",
                valueList, typeReferences);

        String encode = FunctionEncoder.encode(function);

        if (encode.startsWith("0x")) {
            encode = encode.replaceFirst("0x", "");
        }
        Log.d("newMethod", "result = " + encode);
        return hexStringToByteArray(encode);
    }

    public static byte[] getDataForReviewAnApp(String appId, String address, String vote, String description, String txIndex) {
        // function pushFeedbackRating(uint idApp, uint vote, string description, bytes32 txIndex)
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

        Function function = new Function("pushFeedbackRating",
                arrayList, typeReferences);

        String encode = FunctionEncoder.encode(function);
        Log.d("test", "generateAppBuyTransaction: " + encode);
        if (encode.startsWith("0x")) {
            encode = encode.replaceFirst("0x", "");
        }
        return hexStringToByteArray(encode);
    }

    public static byte[] createSentTokenTransactionBytes(String address, String tokenNumber) {
        // function transfer (address,uint256)
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

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
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

    public static String generateAppBuyTransaction(int nonce, BigInt gasPrice, App app, String adrNode) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);

        BigInt price = new BigInt(0);
        price.setString(app.price, 10);
        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS),
                price, GAS_LIMIT, gasPrice,
                getDataForBuyAppWithWeb3(app.appId, adrNode));

        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateTransferTransaction(int nonce, String gasPrice, String transferAmount, String recipientAddress) throws Exception {
        BigInt price = new BigInt(0);
        price.setString(transferAmount, 10);

        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);

        Transaction transaction = new Transaction(nonce, new Address(recipientAddress),
                price, GAS_LIMIT, new BigInt(Long.parseLong(gasPrice)), null);
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateSendReviewTransaction(int nonce, BigInt gasPrice, App app, String vote, String description, String txIndex) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);
        BigInt price = new BigInt(0);

        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS),
                price, GAS_LIMIT, gasPrice,
                getDataForReviewAnApp(app.appId, account.getAddress().getHex(), vote, description, txIndex));
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateSendTokenTransaction(int nonce, BigInt gasPrice, String transferAmount, String recipientAddress, String icoAddress) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);

        BigInt price = new BigInt(0);
        Transaction transaction = new Transaction(nonce, new Address(icoAddress),
                price, GAS_LIMIT, gasPrice,
                createSentTokenTransactionBytes(recipientAddress, transferAmount));

        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);

    }




    /*Transactions need to add:
    * buyAppSub(unit256 _app, address _node, uint256, obj, unit256, price)
    * buy(uint256 app. address node, uint256 obj )
    * buyAppObj(uint245 app, address node, uint256 obj)
    * buyAppObj(uint256 app. address node, uint256 obj, _price)
    * buyAppSub(uint256 app, address node, uint256 obj)
    * obj = 0 - если купить
    * obg = 1 - если подписка. ,Вроде как
    * */

}

