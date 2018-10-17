package com.blockchain.store.playmarket.utilities.crypto;

import android.util.Log;

import com.blockchain.store.PurchaseSDK.entities.TransferObject;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

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

     /*Transactions need to add:
    По цене:
    Сама транзакция платная.
    в дата - цену в УЕ.
     * buy(uint256 app. address node, uint256 obj )
     * buyAppObj(uint245 app, address node, uint256 obj)
     * buyAppObj(uint256 app. address node, uint256 obj,unit256  _price)
     * buyAppSub(unit256 _app, address _node, uint256 obj, unit256 _price)
     * buyAppSub(uint256 app, address node, uint256 obj)
    * obj = 0 - если приложение
    * obg = 1 - идентификатор
    * */

    public byte[] setAsBuyTransaction(TransferObject transferObject, AccountInfoResponse accountInfo) {
        setMethod("buy");
        putTypeData(new Uint256(transferObject.getAppId()));
        putTypeData(new org.web3j.abi.datatypes.Address(accountInfo.adrNode));
        putTypeData(new Uint256(Long.parseLong(transferObject.getObjectId())));
        return build();
    }

    public byte[] setAsBuyObjTransaction(TransferObject transferObject, AccountInfoResponse accountInfo) {
        setMethod("buyAppObj");
        putTypeData(new Uint256(transferObject.getAppId()));
        putTypeData(new org.web3j.abi.datatypes.Address(accountInfo.adrNode));
        putTypeData(new Uint256(Long.parseLong(transferObject.getObjectId())));
        return build();
    }

    public byte[] setAsBuyObjWithPriceCheckTransaction(TransferObject transferObject, AccountInfoResponse accountInfo) {
        setMethod("buyAppObj");
        putTypeData(new Uint256(transferObject.getAppId()));
        putTypeData(new org.web3j.abi.datatypes.Address(accountInfo.adrNode));
        putTypeData(new Uint256(Long.parseLong(transferObject.getObjectId())));
        putTypeData(new Uint256(Long.parseLong(transferObject.getTransferPrice())));
        return build();
    }

    public byte[] setAsBuySubTransaction(TransferObject transferObject, AccountInfoResponse accountInfo) {
        setMethod("buyAppSub");
        putTypeData(new Uint256(transferObject.getAppId()));
        putTypeData(new org.web3j.abi.datatypes.Address(accountInfo.adrNode));
        putTypeData(new Uint256(Long.parseLong(transferObject.getObjectId())));
        return build();
    }

    public byte[] setAsBuySubWithPriceCheckTransaction(TransferObject transferObject, AccountInfoResponse accountInfo) {
        setMethod("buyAppSub");
        putTypeData(new Uint256(transferObject.getAppId()));
        putTypeData(new org.web3j.abi.datatypes.Address(accountInfo.adrNode));
        putTypeData(new Uint256(Long.parseLong(transferObject.getObjectId())));
        putTypeData(new Uint256(Long.parseLong(transferObject.getTransferPrice())));
        return build();
    }



}
