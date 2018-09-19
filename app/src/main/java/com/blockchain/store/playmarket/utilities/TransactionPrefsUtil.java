package com.blockchain.store.playmarket.utilities;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.orhanobut.hawk.Hawk;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;

public class TransactionPrefsUtil {
    private static final String TAG = "TransactionPrefsUtil";

    public static void addToSharePrefs(TransactionModel transactionModel) {
        if (isDuplicate(transactionModel)) {
            return;
        }
        ArrayList<TransactionModel> storedTransactionModels = getStoredTransactionModels();
        storedTransactionModels.add(0, transactionModel);
        saveModels(storedTransactionModels);
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

    public static ArrayList<TransactionModel> getStoredTransactionModels() {
        if (Hawk.contains(Constants.TRANSACTION_MODEL_KEY)) {
            return Hawk.get(Constants.TRANSACTION_MODEL_KEY);
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<TransactionModel> getTransactionByStatus(Constants.TransactionStatus status) {
        ArrayList<TransactionModel> storedTransactionModels = getStoredTransactionModels();
        ArrayList<TransactionModel> transactionByStatus = new ArrayList<>();

        for (TransactionModel storedTransactionModel : storedTransactionModels) {
            if (storedTransactionModel.transactionStatus == status) {
                transactionByStatus.add(storedTransactionModel);
            }
        }
        return transactionByStatus;
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
