package com.blockchain.store.dao.ui.dividends_screen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.DaoTokenAdapter;
import com.blockchain.store.playmarket.interfaces.DaoAdapterCallback;
import com.blockchain.store.playmarket.repositories.TokenRepository;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.dialogs.DialogManager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DividendsFragment extends Fragment implements DividendsContract.View, DaoAdapterCallback {

    private static final String TAG = "DividendsFragment";
    private DividendsPresenter presenter = new DividendsPresenter();
    private DaoTokenAdapter adapter;

    @BindView(R.id.error_view_repeat_btn) Button error_view_repeat_btn;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.background) View background;
    @BindView(R.id.back_arrow) ImageView backArrow;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public DividendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dividends, container, false);
        ButterKnife.bind(this, view);
        presenter.init(this);
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
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @OnClick(R.id.error_view_repeat_btn)
    void onRepeatLayoutClicked() {
        errorHolder.setVisibility(View.GONE);
        getTokens();
    }

    private void getTokens() {
        presenter.getDividendsTokens();
        errorHolder.setVisibility(View.GONE);
    }

    @Override
    public void onTokensComplete() {
        adapter.setLoading(false);
    }

    @Override
    public void onTokenNext(DaoToken token) {
        errorHolder.setVisibility(View.GONE);
        adapter.setToken(token);
    }

    @Override
    public void onTokensError(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
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
        new DialogManager().showDividendsDialog(getActivity(), () -> {
            TokenRepository.addToken(daoToken);
            if (daoToken.isNeedSecondTx()) {
                presenter.runDoubleTx(daoToken);
            } else {
                presenter.runSingleTx(daoToken);
            }
        });
    }

    @Override
    public void transferFailed(Throwable throwable) {
        Log.d(TAG, "transferFailed() called with: throwable = [" + throwable + "]");
    }

    @Override
    public void receive(String result) {
        ToastUtil.showToast(R.string.transaction_sent);
        Log.d(TAG, "receive() called with: o = [" + result + "]");
    }

}
