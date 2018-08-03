package com.blockchain.store.playmarket.ui.activity_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity implements NewsActivityContract.View{
    private static final String TAG = "NewsActivity";

    @BindView(R.id.top_layout_back_arrow) ImageView top_layout_back_arrow;
    @BindView(R.id.top_layout_app_name) TextView top_layout_app_name;
    @BindView(R.id.top_layout_holder) LinearLayout top_layout_holder;
    @BindView(R.id.recyclerView2) RecyclerView recyclerView2;
    @BindView(R.id.error_view_repeat_btn) Button error_view_repeat_btn;
    @BindView(R.id.error_holder) LinearLayout error_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        // TODO
    }

    @Override
    public void onNewsReady(PlaymarketFeed feed) {

    }

    @Override
    public void onNewsError(Throwable throwable) {

    }

    @Override
    public void showProgress(boolean isShow) {

    }
}
