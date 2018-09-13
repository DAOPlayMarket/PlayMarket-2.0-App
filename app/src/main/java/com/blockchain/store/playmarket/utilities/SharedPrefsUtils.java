package com.blockchain.store.playmarket.utilities;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.orhanobut.hawk.Hawk;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;

public class SharedPrefsUtils {


    public static void addToSharePrefs(TransactionModel transactionModel) {
        if (isDuplicate(transactionModel)) {
            return;
        }
        ArrayList<TransactionModel> storedTransactionModels = getStoredTransactionModels();
        storedTransactionModels.add(transactionModel);
        saveModels(storedTransactionModels);
    }


    public static void updateModel(TransactionReceipt transactionReceipt) {
        TransactionModel transactionModel = findTransactionModel(transactionReceipt.getTransactionHash());
        if (transactionModel != null) {
            transactionModel.transactionReceipt = transactionReceipt;
        }
        saveModels(transactionModel);
    }

    private static ArrayList<TransactionModel> getStoredTransactionModels() {
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
