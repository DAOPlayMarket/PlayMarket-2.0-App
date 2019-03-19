package com.blockchain.store.playmarket.ui.all_reviews_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.UserReviewAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.dialogs.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllReviewsActivity extends BaseActivity implements UserReviewAdapter.UserReviewCallback, AllReviewsContract.View {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private UserReviewAdapter adapter;
    private ArrayList<UserReview> userReviews;
    private AllReviewPresenter presenter;
    private App app;

    public static void start(Context context, ArrayList<UserReview> userReviews, App app) {
        Intent starter = new Intent(context, AllReviewsActivity.class);
        starter.putExtra("reviews", userReviews);
        starter.putExtra("app", app);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);
        ButterKnife.bind(this);
        attachPresenter();
        userReviews = getIntent().getParcelableArrayListExtra("reviews");
        app = getIntent().getParcelableExtra("app");
        initRecyclerView();
    }

    private void attachPresenter() {
        presenter = new AllReviewPresenter();
        presenter.init(this, app);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserReviewAdapter(userReviews, this, false);

        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onReplyClicked() {
        new DialogManager().showReviewDialog(null, this, (review, rating) -> {
            presenter.onSendReviewClicked(review, rating);
        });
    }

    @Override
    public void onReplyOnReviewClicked(UserReview userReview) {
        new DialogManager().showReviewDialog(userReview, this, (review, rating) -> {
            presenter.onSendReviewClicked(review, userReview.rating, userReview.hashTx);
        });
    }


    @Override
    public void onReadMoreClicked(ArrayList<UserReview> userReviews) {
    }

    @Override
    public void onReviewError(Throwable throwable) {

    }

    @Override
    public void onReviewSendSuccessfully() {
        ToastUtil.showToast(R.string.successfully_review_send);
    }
}
