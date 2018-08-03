package com.blockchain.store.playmarket.ui.activity_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.NewsAdapter;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity implements NewsActivityContract.View {
    private static final String TAG = "NewsActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recyclerView2) RecyclerView recyclerView2;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    private NewsAdapter adapter;
    private NewsActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        attachPresenter();
        setViews();
    }

    private void attachPresenter() {
        presenter = new NewsActivityPresenter();
        presenter.init(this);
        presenter.getNews();
    }

    private void setViews() {
        toolbarTitle.setText(R.string.news);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        presenter.getNews();
        errorHolder.setVisibility(View.GONE);
    }

    @Override
    public void onNewsReady(PlaymarketFeed feed) {
        errorHolder.setVisibility(View.GONE);
    }

    @Override
    public void onNewsError(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean isShow) {

    }
}
