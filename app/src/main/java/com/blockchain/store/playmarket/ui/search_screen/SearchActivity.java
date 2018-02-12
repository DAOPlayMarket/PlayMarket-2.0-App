package com.blockchain.store.playmarket.ui.search_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.SearchListAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchContract.View, AppListCallbacks {
    private static final String TAG = "SearchActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_view) TextView emptyView;

    private SearchContract.Presenter presenter;
    private SearchListAdapter adapter;
    private String searchQuery;

    public static void start(Context context, String searchQuery) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra(Constants.SEARCH_ACTIVITY_QUERY_EXTRA, searchQuery);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchQuery = getIntent().getStringExtra(Constants.SEARCH_ACTIVITY_QUERY_EXTRA);
        toolbarTitle.setText(searchQuery);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new SearchPresenter();
        presenter.init(this);
        presenter.searchQuery(searchQuery);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }

    @Override
    public void onSearchResultReady(ArrayList<App> apps) {
        setUpAdapter(apps);
    }

    private void setUpAdapter(ArrayList<App> apps) {
        emptyView.setVisibility(apps.isEmpty() ? View.VISIBLE : View.GONE);
        adapter = new SearchListAdapter(apps, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSearchResultFail(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
        ToastUtil.showToast(throwable.getMessage());
    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAppClicked(App app) {
        AppDetailActivity.start(this, app);
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onRepeatBtnClicked() {
        errorHolder.setVisibility(View.GONE);
        presenter.searchQuery(searchQuery);
    }

}
