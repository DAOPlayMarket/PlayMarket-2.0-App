package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.Constants;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Transaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.rx.Web3jRx;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.KeyManager;

import static com.blockchain.store.playmarket.utilities.Constants.GAS_LIMIT;
import static com.blockchain.store.playmarket.utilities.Constants.NODE_ADDRESS;
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
        Log.d("Ether", data);

        return hexStringToByteArray(data);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        Log.d("Ether", "hexStringToByteArray: " + b);
        return b;
    }

    public static String generateAppBuyTransaction(int nonce, BigInt gasPrice, App app) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);
        Transaction transaction = new Transaction(nonce, new Address(Constants.PLAY_MARKET_ADDRESS),
                new BigInt(Long.parseLong(app.price)), new BigInt(GAS_LIMIT), gasPrice,
                getDataForBuyApp(app.appId, NODE_ADDRESS));
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateInvestTransaction(int nonce, BigInt gasPrice, String investPrice) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);
//        RawTransaction rawTransaction = new RawTransaction()
        Transaction transaction = new Transaction(nonce, new Address(Constants.INVEST_ADDRESS),
                new BigInt(Long.parseLong(investPrice)), new BigInt(GAS_LIMIT), gasPrice, null);
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
//        TransactionEncoder.signMessage(transaction1)

        return getRawTransaction(signedTransaction);
    }

    // temp
    public static String generateInvestTransactionWithAddress(int nonce, BigInt gasPrice, String investPrice, String address) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);
        Transaction transaction = new Transaction(nonce, new Address(Constants.INVEST_ADDRESS),
                new BigInt(Long.parseLong(investPrice)), new BigInt(GAS_LIMIT), gasPrice, null);
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }

    public static String generateTransferTransaction(int nonce, String gasPrice, String transferAmount, String recipientAddress) throws Exception {
        KeyManager keyManager = Application.keyManager;
        Account account = keyManager.getAccounts().get(0);
        Transaction transaction = new Transaction(nonce, new Address(recipientAddress),
                new BigInt(Long.parseLong(transferAmount)), new BigInt(GAS_LIMIT), new BigInt(Long.parseLong(gasPrice)), null);
        Transaction signedTransaction = keyManager.getKeystore().signTx(account, transaction, new BigInt(RINKEBY_ID));
        return getRawTransaction(signedTransaction);
    }
}

