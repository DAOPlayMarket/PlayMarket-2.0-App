package com.blockchain.store.dao.ui.dao_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.DaoTokenTransfer;
import com.blockchain.store.dao.ui.services.DaoContractService;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.DaoTokenAdapter;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DaoActivity extends AppCompatActivity {
    private static final String TAG = "DaoActivity";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    DaoTokenAdapter adapter;

    List<DaoToken> daoTokens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        ButterKnife.bind(this);
        startService(new Intent(this, DaoContractService.class));
        DaoTransactionRepository.getTokens().subscribe(this::onOk, this::onError);

    }

    private void onOk(List<DaoToken> daoTokens) {
        Log.d(TAG, "onOk: ");
        this.daoTokens = daoTokens;
        initAdapter(daoTokens);
    }

    private void initAdapter(List<DaoToken> daoTokens) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DaoTokenAdapter(daoTokens, new DaoAdapterCallback() {
            @Override
            public void onPmTokenClicked(DaoToken daoToken) {
                DaoTokenTransfer.start(DaoActivity.this, daoToken);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "onError() called with: throwable = [" + throwable + "]");
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, DaoContractService.class));
        super.onStop();
    }

    @OnClick(R.id.checkTransaction)
    public void checkTransaction() {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    try {
                        Pair<Transaction, Transaction> stringStringPair = CryptoUtils.test(result.count, result.gasPrice);
                        String rawTransaction = CryptoUtils.getRawTransaction(stringStringPair.first);
                        String rawSecondTransaction = CryptoUtils.getRawTransaction(stringStringPair.second);
                        TransactionInteractor.addToJobSchedule(stringStringPair.first.getHash().getHex(), stringStringPair.second.getHash().getHex(), rawSecondTransaction);
                        return RestApi.getServerApi().deployTransaction(rawTransaction);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);

    }

    private void transferFailed(Throwable throwable) {
        Log.d(TAG, "transferFailed: ");
    }

    private void transferSuccess(Object o) {
        Log.d(TAG, "transferSuccess: ");
    }

    public interface DaoAdapterCallback {
        void onPmTokenClicked(DaoToken daoToken);
    }

}
