package com.blockchain.store.playmarket.ui.tokens_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TokenAdapter;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenListActivity extends AppCompatActivity implements TokenListContract.View, TokenAdapter.TokenAdapterListener {

    private static final String TAG = "TokenListActivity";
    private static final int ADD_TOKEN_RESPONSE = 10;

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.empty_view) TextView emptyView;

    private TokenAdapter adapter;
    private TokenListPresenter presenter;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_list);
        ButterKnife.bind(this);
        initTitle();
        setUpBottomDialog();
        attachPresenter();
    }

    private void setUpBottomDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.token_bottom_sheet);
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

    @OnClick(R.id.empty_view)
    void onEmptyViewClicked() {
        presenter.getAllTokens();
    }

    @OnClick(R.id.floatingActionButton)
    void onFabClicked() {
        bottomSheetDialog.show();
    }

    @Override
    public void onTokensReady(ArrayList<Token> tokenResponse) {
        if (!tokenResponse.isEmpty()) {
            emptyView.setVisibility(View.GONE);

            adapter = new TokenAdapter(tokenResponse, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            emptyView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onTokenClicked(Token token) {
        TransferActivity.startAsTokenTransfer(this, token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TOKEN_RESPONSE && resultCode == RESULT_OK) {
            Token token = data.getParcelableExtra(Constants.TOKEN_ARGS);
            adapter.addNewToken(token);
        }
    }
}
