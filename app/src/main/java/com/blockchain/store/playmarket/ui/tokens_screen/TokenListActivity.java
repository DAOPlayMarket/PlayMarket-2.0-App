package com.blockchain.store.playmarket.ui.tokens_screen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.View;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.views.FonAwesomeTextViewSolid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenListActivity extends AppCompatActivity {
    private static final String TAG = "TokenListActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_list);
        ButterKnife.bind(this);
        initTitle();

    }

    private void initTitle() {
        toolbarTitle.setText("Token list");
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
        ;
    }

    @OnClick(R.id.floatingActionButton)
    void onFabClicked() {
        Toast.makeText(this, "fab", Toast.LENGTH_SHORT).show();
    }

}
