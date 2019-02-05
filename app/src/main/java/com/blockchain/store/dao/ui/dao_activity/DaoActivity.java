package com.blockchain.store.dao.ui.dao_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.DaoTokenTransfer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        ButterKnife.bind(this);
        DaoTokenTransfer.start(DaoActivity.this, null);
    }

    public interface DaoAdapterCallback {
        void onPmTokenClicked(DaoToken daoToken);
        void onDaoTokenClicked(DaoToken daoToken);
    }

}
