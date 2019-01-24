package com.blockchain.store.playmarket.ui.tokens_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TokenAdapter;
import com.blockchain.store.playmarket.repositories.TokenRepository;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class TokenListFragment extends Fragment implements TokenListContract.View, TokenAdapter.TokenAdapterListener {

    private static final String TAG = "TokenListFragment";


    @BindView(R.id.editText) EditText enterAddress;
    @BindView(R.id.find_btn) Button findBtn;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.find_token_progress_bar) ProgressBar findTokenProgressBar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.error_holder) LinearLayout erroHolder;

    private TokenAdapter adapter;
    private TokenListPresenter presenter;
    private ArrayList<DaoToken> userSavedTokens;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_token_list, container, false);
        ButterKnife.bind(this, view);
        attachPresenter();
        userSavedTokens = TokenRepository.getUserSavedTokens();
        return view;
    }

    @OnTextChanged(value = R.id.editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(Editable editable) {
        String text = editable.toString();
        if (adapter != null)
            adapter.getFilter().filter(text);

    }

    private void attachPresenter() {
        presenter = new TokenListPresenter();
        presenter.init(this);
        getAllTokens();
    }

    public void getAllTokens() {
        presenter.getAllTokens();
        erroHolder.setVisibility(View.GONE);
    }

    @Override
    public void onTokensReady(ArrayList<DaoToken> tokenResponse) {
        erroHolder.setVisibility(View.GONE);
        adapter = new TokenAdapter(tokenResponse, userSavedTokens, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTokensError(Throwable throwable) {
        erroHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFindTokenProgress(boolean isShown) {
        findTokenProgressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
        findBtn.setVisibility(isShown ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onNewTokenFailed(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewTokenReady(DaoToken token) {
        TokenRepository.addToken(token);
        Toast.makeText(getActivity(), "Token " + token.name + " added!", Toast.LENGTH_SHORT).show();
        if (!isDetached()) {
            getActivity().onBackPressed();
        }

    }


    @Override
    public void showProgress(boolean isShown) {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onTokenClicked(DaoToken token) {
//        presenter.getTokenBalance(token);
    }

    @Override
    public void isListEmpty(boolean isEmpty) {
        findBtn.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }


    @OnClick(R.id.find_btn)
    public void find_btn() {
        if (enterAddress.getText().toString().isEmpty()) {
            enterAddress.setError("Empty field");
            return;
        }
        if (enterAddress.getText().toString().startsWith("0x")) {
            enterAddress.setError("Address must start with 0x");
            return;
        }
        if (enterAddress.getText().toString().length() < 40) {
            enterAddress.setError("Wrong address length");
            return;
        }
        presenter.findToken(enterAddress.getText().toString());
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        getAllTokens();
    }

    @OnClick(R.id.close_button)
    public void onCloseButtonClicked() {
        getActivity().onBackPressed();
    }


}
