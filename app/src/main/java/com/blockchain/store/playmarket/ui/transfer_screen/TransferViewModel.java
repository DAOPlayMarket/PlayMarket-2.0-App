package com.blockchain.store.playmarket.ui.transfer_screen;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.math.BigDecimal;

public class TransferViewModel extends ViewModel {

    public MutableLiveData<String> senderAddress = new MutableLiveData<>();

    public MutableLiveData<String> recipientAddress = new MutableLiveData<>();

    public MutableLiveData<String> transferAmount = new MutableLiveData<>();

    public MutableLiveData<String> balance = new MutableLiveData<>();
}
