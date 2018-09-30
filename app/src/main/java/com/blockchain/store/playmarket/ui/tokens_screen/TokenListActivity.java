package com.blockchain.store.playmarket.ui.tokens_screen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TokenAdapter;
import com.blockchain.store.playmarket.data.entities.TokenResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenListActivity extends AppCompatActivity implements TokenListContract.View {
    private static final String TAG = "TokenListActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;

    private TokenAdapter adapter;
    private TokenListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_list);
        ButterKnife.bind(this);
        initTitle();
        attachPresenter();

    }

    private void attachPresenter() {
        presenter = new TokenListPresenter();
        presenter.init(this);
        presenter.getAllTokens();
    }

    private void initTitle() {
        toolbarTitle.setText(R.string.token_activity_title);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
    }

    @OnClick(R.id.floatingActionButton)
    void onFabClicked() {
        Toast.makeText(this, "fab", Toast.LENGTH_SHORT).show();
    }

    @Override public void showProgress(boolean b) {

    }

    @Override public void onTokensReady(TokenResponse tokenResponse) {
        adapter = new TokenAdapter(tokenResponse.tokens);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override public void onTokensError(Throwable throwable) {

    }
}
