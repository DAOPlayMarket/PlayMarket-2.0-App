package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

public class DappTransaction implements Parcelable {
    private static final String TAG = "DappTransaction";
    public String nonce;
    public String gasPrice;
    public String gasLimit;
    public String from;
    public String to;
    public String value = "0";
    public String chainId;
    public String gas;
    public String data = "";


    public String getNonce() {
        return String.valueOf(Long.parseLong(nonce.replace("0x", ""), 16));
    }

    public String getGasPrice() {
        return String.valueOf(Long.parseLong(gasPrice.replace("0x", ""), 16));
    }

    public String getGasLimit() {
        return String.valueOf(Long.parseLong(gasLimit.replace("0x", ""), 16));
    }

    public String getValue() {
        return String.valueOf(Long.parseLong(value.replace("0x", ""), 16));

    }

    public String getChainId() {
        return String.valueOf(Long.parseLong(chainId.replace("0x", ""), 16));
    }

    public String getGas() {
        return String.valueOf(Long.parseLong(gas.replace("0x", ""), 16));
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Transaction createTx() throws Exception {
        byte[] decode = Hex.decode(data.replaceFirst("0x", ""));
        String s2 = hexToString(data.replaceFirst("0x", ""));
        String s3 = fromHex(data.replaceFirst("0x", ""));
        String test = new String(data.getBytes());
        String nonce = getNonce();
        String gasPrice = getGasPrice();
        String gasLimit = getGasLimit();
        String value = getValue();
        String chainId = getChainId();
        String gas = getGas();
        Transaction tx = new Transaction(Long.valueOf(nonce), new Address(to), new BigInt(Long.valueOf(value)), Long.valueOf(gasLimit), new BigInt(Long.valueOf(gasPrice)), decode);
        Account account = AccountManager.getAccount();
        return CryptoUtils.keyManager.getKeystore().signTx(account, tx, new BigInt(Long.valueOf(chainId)));
//        CryptoUtils.getRawTransaction(signedTx);

    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    public String fromHex(String hex) {
        hex = hex.replaceAll("^(00)+", "");
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(bytes);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nonce);
        dest.writeString(this.gasPrice);
        dest.writeString(this.gasLimit);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.value);
        dest.writeString(this.chainId);
        dest.writeString(this.gas);
        dest.writeString(this.data);
    }

    public DappTransaction() {
    }

    protected DappTransaction(Parcel in) {
        this.nonce = in.readString();
        this.gasPrice = in.readString();
        this.gasLimit = in.readString();
        this.from = in.readString();
        this.to = in.readString();
        this.value = in.readString();
        this.chainId = in.readString();
        this.gas = in.readString();
        this.data = in.readString();
    }

    public static final Creator<DappTransaction> CREATOR = new Creator<DappTransaction>() {
        @Override
        public DappTransaction createFromParcel(Parcel source) {
            return new DappTransaction(source);
        }

        @Override
        public DappTransaction[] newArray(int size) {
            return new DappTransaction[size];
        }
    };
}
