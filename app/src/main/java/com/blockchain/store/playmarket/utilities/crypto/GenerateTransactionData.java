package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;
import java.util.ArrayList;

public class GenerateTransactionData {
    private String methodName;
    private ArrayList<Type> typeList = new ArrayList<>();
    private ArrayList<TypeReference<?>> typeReferencesList = new ArrayList<>();

    public GenerateTransactionData setMethod(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public GenerateTransactionData putAddress(String addressHex) {
        putAddress(new Address(addressHex));
        return this;
    }

    public GenerateTransactionData putAddress(Address address) {
        putTypeData(address);
        return this;
    }

    public GenerateTransactionData putString(Utf8String string) {
        putTypeData(string);
        return this;
    }

    public GenerateTransactionData putString(String string) {
        putString(new Utf8String(string));
        return this;
    }

    public GenerateTransactionData putInteger(BigInteger integer) {
        putInt(new Uint(integer));
        return this;
    }

    public GenerateTransactionData putInt(Uint uint) {
        putTypeData(uint);
        return this;
    }

    public GenerateTransactionData putTypeData(Type type) {
        this.typeList.add(type);
        this.typeReferencesList.add(TypeReference.create(type.getClass()));
        return this;
    }

    public byte[] build() {
        Function function = new Function(methodName, typeList, typeReferencesList);
        String encode = FunctionEncoder.encode(function);
        if (encode.startsWith("0x")) {
            encode = encode.replaceFirst("0x", "");
        }
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


}
