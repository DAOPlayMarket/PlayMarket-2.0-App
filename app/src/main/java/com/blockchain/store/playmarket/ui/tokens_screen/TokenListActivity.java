package com.blockchain.store.playmarket.ui.tokens_screen;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TokenAdapter;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;

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
    @BindView(R.id.token_progress_bar) ProgressBar progressBar;

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
        EditText dialogEditText = bottomSheetDialog.findViewById(R.id.editText);
        Button dialogBtn = bottomSheetDialog.findViewById(R.id.find_btn);
        dialogBtn.setOnClickListener(v -> addTokenClicked(dialogEditText.getText().toString(), dialogEditText));
    }

    private void addTokenClicked(String tokenAddress, EditText dialogEditText) {
        if (tokenAddress.isEmpty()) {
            dialogEditText.setError("Address is empty!");
            return;
        }
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
        presenter.findToken(tokenAddress);

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
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
        adapter = new TokenAdapter(tokenResponse, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNewTokenFailed(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewTokenReady(Token token) {
        adapter.addNewToken(token);
    }

    @Override
    public void showProgress(boolean isShown) {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
        floatingActionButton.setVisibility(isShown ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onTokenClicked(Token token) {
        TransferActivity.startAsTokenTransfer(this, token);
    }

}
