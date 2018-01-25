package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.KeyManager;

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
        if (matcher.find())
        {
            return matcher.group(0).replaceAll("Hex:\\s*", "");
        } else {
            return "";
        }
    }

    public static byte[] getDataForBuyApp(String appId, String idCat) {
        byte[] hash = sha3("buyApp(uint256,uint256)".getBytes());
        String hashString = bytesToHexString(hash);

        String functionHash = hashString.substring(0, 8);

        Log.d("Ether", functionHash);
        Log.d("Ether", hashString);

        String appIdEnc = String.format("%64s", appId).replace(' ', '0');
        String catIdEnc = String.format("%64s", idCat).replace(' ', '0');

        Log.d("Ether", appIdEnc);
        Log.d("Ether", catIdEnc);

        String data = functionHash + appIdEnc + catIdEnc;
        Log.d("Ether", data);

        return hexStringToByteArray(data);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b&0xff));
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
        return b;
    }

    public static void generateTestTransaction() {
        BigInt value = new BigInt(0);
        value.setInt64((long) 1100000000000000.0);

        Transaction tx = new Transaction(
                3, new Address(TEST_ADDRESS),
                value, new BigInt(200000), new BigInt((long) 30000000000.0), null);
        try {
            Transaction transaction = keyManager.getKeystore().signTxPassphrase(keyManager.getAccounts().get(0), "Test", tx, new BigInt(3));

            Log.d("Ether", CryptoUtils.getRawTransaction(transaction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

