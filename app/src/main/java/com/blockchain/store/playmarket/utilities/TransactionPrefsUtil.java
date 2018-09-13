package com.blockchain.store.playmarket.utilities;

import android.util.Log;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.orhanobut.hawk.Hawk;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;

public class TransactionPrefsUtil {


    public static void addToSharePrefs(TransactionModel transactionModel) {
        if (isDuplicate(transactionModel)) {
            return;
        }
        ArrayList<TransactionModel> storedTransactionModels = getStoredTransactionModels();
        storedTransactionModels.add(0, transactionModel);
        saveModels(storedTransactionModels);


        ArrayList<TransactionModel> storedTransactionModels2 = getStoredTransactionModels();
        Log.d("", "addToSharePrefs: ");
    }


    public static void updateModel(TransactionReceipt transactionReceipt) {
        TransactionModel transactionModel = findTransactionModel(transactionReceipt.getTransactionHash());
        if (transactionModel != null) {
            transactionModel.transactionReceipt = transactionReceipt;
            transactionModel.transactionStatus = (transactionReceipt.getStatus().contains("1")
                    ? Constants.TransactionStatus.SUCCEES : Constants.TransactionStatus.FAILED);
        }
        saveModels(transactionModel);
    }

    public static void updateTransactionStatus(String transactionHash, Constants.TransactionStatus status) {
        TransactionModel transactionModel = findTransactionModel(transactionHash);
        if (transactionModel != null) {
            transactionModel.transactionStatus = status;
        }
        saveModels(transactionModel);
    }

    public static ArrayList<TransactionModel> getStoredTransactionModels() {
        if (Hawk.contains(Constants.TRANSACTION_MODEL_KEY)) {
            return Hawk.get(Constants.TRANSACTION_MODEL_KEY);
        } else {
            return new ArrayList<>();
        }
    }

    private static TransactionModel findTransactionModel(String transactionHash) {
        for (TransactionModel model : getStoredTransactionModels()) {
            if (model.transactionHash.equalsIgnoreCase(transactionHash)) {
                return model;
            }
        }
        return null;
    }

    private static TransactionModel findTransactionModel(TransactionModel transactionModel) {
        return findTransactionModel(transactionModel.transactionHash);
    }

    private static boolean isDuplicate(TransactionModel transactionModel) {
        return findTransactionModel(transactionModel) != null;
    }

    private static void saveModels(ArrayList<TransactionModel> transactionModels) {
        Hawk.put(Constants.TRANSACTION_MODEL_KEY, transactionModels);
    }

    private static void saveModels(TransactionModel updatedTransactionModel) {
        ArrayList<TransactionModel> storedTransactionModels = getStoredTransactionModels();
        for (int i = 0; i < storedTransactionModels.size(); i++) {
            TransactionModel transactionModel = storedTransactionModels.get(i);
            if (transactionModel.transactionHash.equalsIgnoreCase(updatedTransactionModel.transactionHash)) {
                storedTransactionModels.set(i, updatedTransactionModel);
            }
        }
        Hawk.put(Constants.TRANSACTION_MODEL_KEY, storedTransactionModels);
    }
}
