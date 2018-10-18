package com.blockchain.store.playmarket.ui.transfer_screen;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.blockchain.store.playmarket.utilities.Constants;

public class TransferViewModel extends ViewModel {

    public MutableLiveData<String> senderAddress = new MutableLiveData<>();

    public MutableLiveData<String> senderPassword = new MutableLiveData<>();

    public MutableLiveData<String> recipientAddress = new MutableLiveData<>();

    public MutableLiveData<String> transferAmount = new MutableLiveData<>();

    public MutableLiveData<String> balance = new MutableLiveData<>();

    public MutableLiveData<String> dimension = new MutableLiveData<>();

    public MutableLiveData<String> tokenName = new MutableLiveData<>();

    public MutableLiveData<Boolean> isEth = new MutableLiveData<>();

    public MutableLiveData<Boolean> isBlockEthIcon = new MutableLiveData<>();

    public MutableLiveData<String> totalBalance = new MutableLiveData<>();

    public MutableLiveData<Constants.TransactionTypes> transactionType = new MutableLiveData<>();

    public MutableLiveData<String> priceInLocalCurrency = new MutableLiveData<>();


}
