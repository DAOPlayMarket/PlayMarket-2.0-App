package com.blockchain.store.dao.ui.dividends_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.blockchain.TransactionSender;
import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.repository.ContractReader;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.DaoTokenAdapter;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.interfaces.DaoAdapterCallback;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.dialogs.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class DividendsFragment extends Fragment implements DaoAdapterCallback{

    private static final String TAG = "DividendsFragment";

    @BindView(R.id.error_view_repeat_btn) Button error_view_repeat_btn;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.background) View background;
    @BindView(R.id.back_arrow) ImageView backArrow;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    DaoTokenAdapter adapter;

    public DividendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dividends, container, false);
        ButterKnife.bind(this, view);
        initAdapter();
        setErrorHolderBg();
        getTokens();
        return view;
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DaoTokenAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }


    private void setErrorHolderBg() {
        errorHolder.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick(R.id.back_arrow)
    void onBackArrowClicked() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.error_view_repeat_btn)
    void onRepeatLayotClicked() {
        errorHolder.setVisibility(View.GONE);
        getTokens();
    }

    private void getTokens() {
        errorHolder.setVisibility(View.GONE);
        PublishSubject<DaoToken> publishSubject = PublishSubject.create();

        publishSubject.observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(this::onNextValue, this::onTokensError, this::onTokensComplete);

        ContractReader.getDividendsTokens(publishSubject);
        //DaoTransactionRepository.getTokens().subscribe(this::onTokensReady, this::onTokensError);
//        progressBar.setVisibility(View.VISIBLE);
    }

    private void onTokensComplete() {
        adapter.setLoading(false);
    }

    private void onNextValue(DaoToken token) {
        errorHolder.setVisibility(View.GONE);
        adapter.setToken(token);
    }

    private void onTokensError(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
    }

    private void transferFailed(Throwable throwable) {
        Log.d(TAG, "transferFailed() called with: throwable = [" + throwable + "]");
    }

    @Override
    public void onPmTokenClicked(DaoToken daoToken) {
        ((MainMenuActivity) Objects.requireNonNull(getActivity())).onTokenTransferClicked(daoToken);
    }

    @Override
    public void onDaoTokenClicked(DaoToken daoToken) {
        if (!AccountManager.getUserBalance().equalsIgnoreCase("-1") && Long.valueOf(AccountManager.getUserBalance()) == 0) {
            ToastUtil.showToast("Not enough balance to send transaction");
            return;
        }
        new DialogManager().showDividendsDialog(getActivity(), new DialogManager.DividendCallback() {
            @Override
            public void onAccountUnlocked() {
                TokenRepository.addToken(daoToken);
                if (daoToken.isNeedSecondTx()) {
                    runDoubleTx(daoToken);
                } else {
                    runSingleTx(daoToken);
                }
            }
        });
    }

    private void runSingleTx(DaoToken daoToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    Transaction tx = CryptoUtils.generateDaoWithdraw(result, daoToken);
                    return tx;
                })
                .flatMap(result -> new TransactionSender().send(result))
                .map(result -> {
                    TransactionInteractor.addToJobSchedule(result, Constants.TransactionTypes.GET_DIVIDENDS);
                    return result;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::receive, this::transferFailed);
    }

    private void receive(String result) {
        ToastUtil.showToast(R.string.transaction_sent);
        Log.d(TAG, "receive() called with: o = [" + result + "]");
    }

    private void runDoubleTx(DaoToken daoToken) {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .map(result -> {
                    try {
                        Pair<Transaction, Transaction> stringStringPair = CryptoUtils.generateDaoTransferTransactions(result, daoToken);
                        return new Pair<>(stringStringPair.first, stringStringPair.second);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .flatMap(result -> new TransactionSender().send(result.first), Pair::new)
                .map(result -> {
                    String rawSecondTransaction = CryptoUtils.getRawTransaction(result.first.second);
                    TransactionInteractor.addToJobSchedule(result.second, result.first.second.getHash().getHex(), rawSecondTransaction, Constants.TransactionTypes.GET_DIVIDENDS);
                    return result.second;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::receive, this::transferFailed);
    }
}
