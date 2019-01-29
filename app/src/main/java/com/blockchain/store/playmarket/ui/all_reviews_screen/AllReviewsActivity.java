package com.blockchain.store.playmarket.ui.all_reviews_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.UserReviewAdapter;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailContract;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailPresenter;
import com.blockchain.store.playmarket.utilities.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllReviewsActivity extends AppCompatActivity implements UserReviewAdapter.UserReviewCallback{
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private UserReviewAdapter adapter;
    private ArrayList<UserReview> userReviews;
    private AppDetailPresenter presenter;

    public static void start(Context context, ArrayList<UserReview> userReviews) {
        Intent starter = new Intent(context, AllReviewsActivity.class);
        starter.putExtra("reviews", userReviews);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);
        ButterKnife.bind(this);
        attachPresenter();
        userReviews = getIntent().getParcelableArrayListExtra("reviews");
        initRecyclerView();
    }

    private void attachPresenter() {
        presenter =new AppDetailPresenter();
        presenter.init(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserReviewAdapter(userReviews, this, true);

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
}
