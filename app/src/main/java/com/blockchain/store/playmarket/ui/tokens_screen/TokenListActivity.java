package com.blockchain.store.playmarket.ui.tokens_screen;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TokenAdapter;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenListActivity extends AppCompatActivity implements TokenListContract.View, TokenAdapter.TokenAdapterListener {
    private static final String TAG = "TokenListActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.token_progress_bar) ProgressBar progressBar;
    @BindView(R.id.progress_bar) ProgressBar progressBarToken;
    @BindView(R.id.bottom_contraint) ConstraintLayout bottomLayout;

    private TokenAdapter mainAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private TokenListPresenter presenter;
    private AlertDialog alertDialog;

    /*BottomSheetDialog's view*/

    private RecyclerView bottomSheetRecyclerView;
    private BottomSheetDialog bottomSheetDialog;
    private TokenAdapter bottomSheetAdapter;
    private View errorHolder;
    private Button errorHolderButton;
    private ImageView searchIcon;
    private MaterialSearchView materialSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_list);
        ButterKnife.bind(this);
        initTitle();
        setUpBottomDialog();
        attachPresenter();
        setUpDialog();
    }


    private void setUpDialog() {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("Requesting token balance...")
                .setCancelable(true)
                .create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    private void setUpBottomDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setContentView(R.layout.token_bottom_sheet);

        EditText dialogEditText = bottomSheetDialog.findViewById(R.id.editText);
        Button dialogBtn = bottomSheetDialog.findViewById(R.id.find_btn);
        errorHolder = bottomSheetDialog.findViewById(R.id.error_holder);
        errorHolderButton = bottomSheetDialog.findViewById(R.id.error_view_repeat_btn);
        searchIcon = bottomSheetDialog.findViewById(R.id.search_icon);
        materialSearchView = bottomSheetDialog.findViewById(R.id.search_view);
        materialSearchView.closeSearch();
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (materialSearchView.isSearchOpen()) {
                materialSearchView.showSearch();
//                }
            }
        });

        dialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bottomSheetAdapter.getFilter().filter(s.toString());
            }
        });

        errorHolderButton.setOnClickListener(v -> {
            errorHolder.setVisibility(View.GONE);
            presenter.getBottomSheetTokens();
        });
        bottomSheetRecyclerView = bottomSheetDialog.findViewById(R.id.recycler_view);
        bottomSheetAdapter = new TokenAdapter(new TokenAdapter.TokenAdapterListener() {
            @Override
            public void onTokenClicked(Token token) {
                presenter.addToken(token);
            }

            @Override
            public void onTokenDeleteClicked(Token token) {

            }
        });
        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetRecyclerView.setAdapter(bottomSheetAdapter);
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
        mainAdapter = new TokenAdapter(tokenResponse, this);
//        mainAdapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onNewTokenFailed(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewTokenReady(Token token) {
        emptyView.setVisibility(View.GONE);
        mainAdapter.addNewToken(token);
    }

    @Override
    public void showBottomProgress(boolean isShown) {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
        floatingActionButton.setVisibility(isShown ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showProgress(boolean isShown) {
        progressBarToken.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTokenBalanceReady(Token token) {
        alertDialog.dismiss();
        TransferActivity.startAsTokenTransfer(this, token);
    }

    @Override
    public void onTokenClicked(Token token) {
        alertDialog.show();
        presenter.getTokenBalance(token);

    }

    @Override
    public void onTokenDeleteClicked(Token token) {
        presenter.deleteToken(token);
    }


    @Override
    public void onBottomSheetTokensReady(ArrayList<Token> tokens) {
        bottomSheetAdapter.setTokens(tokens);
    }

    @Override
    public void updateBottomSheetAdapter() {
        bottomSheetAdapter.updateAllItems();
    }

    @Override
    public void updateMainAdapter(ArrayList<Token> tokens) {
        mainAdapter.setTokens(tokens);
    }

    @Override
    public void onBottomSheetTokensFailed(Throwable throwable) {
        bottomSheetRecyclerView.setVisibility(View.GONE);
        errorHolder.setVisibility(View.VISIBLE);

    }

}
