package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ImageListAdapter;
import com.blockchain.store.playmarket.adapters.UserReviewAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.ui.invest_screen.InvestActivity;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AppDetailActivity extends AppCompatActivity implements AppDetailContract.View, ImageListAdapterCallback, UserReviewAdapter.UserReviewCallback {
    private static final String TAG = "AppDetailActivity";
    private static final String APP_EXTRA = "app_extra";
    private static final int DEFAULT_MAX_LINES = 3;
    private static final int LIMIT_MAX_LINES = 150;
    private static final int ANIMATOR_DURATION = 400;

    @BindView(R.id.top_layout_app_name) TextView toolbarAppName;
    @BindView(R.id.top_layout_holder) LinearLayout topLayoutHolder;
    @BindView(R.id.top_layout_back_arrow) ImageView topLayoutBackArrow;
    @BindView(R.id.action_btn) Button actionBtn;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.main_layout_holder) View mainLayoutHolder;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.image_icon) SimpleDraweeView imageIcon;
    @BindView(R.id.app_name) TextView appName;
    @BindView(R.id.app_description) TextView appDescription;
    @BindView(R.id.rating_textView) TextView appRating;
    @BindView(R.id.marks_count_textView) TextView marksCountTextView;
    @BindView(R.id.age_restrictions_textView) TextView ageRestrictionsTextView;
    @BindView(R.id.no_marks_textView) TextView noMarksTextView;
    @BindView(R.id.rating_materialRatingBar) MaterialRatingBar ratingBar;

    @BindView(R.id.invest_btn) Button investBtn;
    @BindView(R.id.delete_view) TextView deleteBtn;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.price_progress_bar) ProgressBar priceProgressBar;
    @BindView(R.id.reviews_recycler_view) RecyclerView reviewsRecyclerView;

    private boolean isUserPurchasedApp;

    private ObjectAnimator textDescriptionAnimator;
    private ImageViewer.Builder imageViewerBuilder;
    private ImageListAdapter imageAdapter;
    private UserReviewAdapter userReviewAdapter;
    private AppDetailPresenter presenter;
    private AppInfo appInfo;
    private App app;

    public static void start(Context context, App app) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_EXTRA, app);
        context.startActivity(starter);
    }

    public static void start(Context context, App app, ActivityOptionsCompat options) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_EXTRA, app);
        context.startActivity(starter/*, options.toBundle()*/);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            app = getIntent().getParcelableExtra(APP_EXTRA);
        }
        attachPresenter();
        setViews();
        generateToolbarColor();
    }

    private void attachPresenter() {
        presenter = new AppDetailPresenter();
        presenter.init(this);
        presenter.getDetailedInfo(app);
        presenter.getReviews(app.appId);
        //if (app.adrICO!=null) presenter.getTokens(app);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setViews() {
        imageIcon.setImageURI(Uri.parse(app.getIconUrl()));
        startPostponedEnterTransition();
        toolbarAppName.setText(app.nameApp);
        appName.setText(app.nameApp);

        String ageRestrictions = app.ageRestrictions + " +";
        ageRestrictionsTextView.setText(ageRestrictions);

        String ratingCount = (app.rating == null)? "0 marks" : app.rating.ratingCount + " marks";
        marksCountTextView.setText(ratingCount);
    }

    private void generateToolbarColor() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(app.getIconUrl()))
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        DataSource<CloseableReference<CloseableImage>> closeableReferenceDataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        closeableReferenceDataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (bitmap == null) {
                    Log.d(TAG, "Bitmap data source returned success, but bitmap null.");
                    return;
                }
                Palette p = Palette.from(bitmap).generate();
                runOnUiThread(() -> {
                    int lightVibrantColor = p.getLightVibrantColor(getResources().getColor(android.R.color.white));
                    int darkMutedColor = p.getDarkMutedColor(getResources().getColor(android.R.color.black));
                    topLayoutHolder.setBackgroundColor(lightVibrantColor);
                    toolbarAppName.setTextColor(darkMutedColor);
                    topLayoutBackArrow.setColorFilter(darkMutedColor);
                });
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                // No cleanup required here
            }
        }, CallerThreadExecutor.getInstance());


    }

    @Override
    public void onDetailedInfoReady(AppInfo appInfo) {
        this.appInfo = appInfo;
        setInvestButtonVisibility(appInfo);
        mainLayoutHolder.setVisibility(View.VISIBLE);
        if (app.description != null)
            appDescription.setText(Html.fromHtml(app.description));
        if (app.rating != null) {
            noMarksTextView.setVisibility(View.GONE);
            appRating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);

            double rating = ((double) app.rating.ratingSum / app.rating.ratingCount);
            rating = Math.round(rating * 10.0) / 10.0;
            appRating.setText(String.valueOf(rating));
            ratingBar.setRating(Float.valueOf(String.valueOf(rating)));
        }
        else {
            noMarksTextView.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.GONE);
        }

        setupScreenshotRecyclerView(appInfo);
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setInvestButtonVisibility(AppInfo appInfo) {
        investBtn.setVisibility(View.INVISIBLE);
        if (this.appInfo.isIco) {
                investBtn.setVisibility(View.VISIBLE);

        } else {
            investBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void setupScreenshotRecyclerView(AppInfo appInfo) {
        if (app.files != null && app.getImages() != null) {
            imageViewerBuilder = new ImageViewer.Builder(this, app.getImages());
            imageAdapter = new ImageListAdapter(app.getImages(), this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageAdapter);
        }
    }

    @Override
    public void onReviewsReady(ArrayList<UserReview> userReviews) {
        setupReviewsRecyclerView(userReviews);
    }


    private void setupReviewsRecyclerView(ArrayList<UserReview> userReviews) {
        userReviewAdapter = new UserReviewAdapter(userReviews, this);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new LinearLayoutManager(this);
//        layout.setAutoMeasureEnabled(false);
        reviewsRecyclerView.setLayoutManager(layout);
        reviewsRecyclerView.setAdapter(userReviewAdapter);
//        reviewsRecyclerView
    }


    @Override
    public void setActionButtonText(String string) {
        runOnUiThread(() -> actionBtn.setText(string));
    }

    @Override
    public void setInvestDeleteButtonText(String string) {
        runOnUiThread(() -> investBtn.setText(string));
    }

    @Override
    public void onDetailedInfoFailed(Throwable throwable) {
        throwable.printStackTrace();
        mainLayoutHolder.setVisibility(View.GONE);
        showErrorView(true);
    }

    @Override
    public void setProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showErrorView(boolean isShow) {
        errorHolder.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDeleteButtonVisibility(boolean isShow) {
        deleteBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPurchaseDialog() {
        new DialogManager().showPurchaseDialog(app, this, () -> {
            presenter.onPurchasedClicked(appInfo);
        });
    }

    @Override
    public void onCheckPurchaseReady(boolean isPurchased) {
        isUserPurchasedApp = isPurchased;
    }

    @Override
    public void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse) {
        ToastUtil.showToast(R.string.successfully_paid);
    }

    @Override
    public void onReviewSendSuccessfully() {
        ToastUtil.showToast(R.string.successfully_review_send);
    }


    @Override
    public void onPurchaseError(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy(app);
    }

    @OnClick(R.id.invest_btn)
    void onInvestBtnClicked() {
        InvestActivity.start(this, appInfo);
    }


    @OnClick(R.id.action_btn)
    public void onActionBtnClicked() {
        presenter.onActionButtonClicked(app);
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorViewRepeatClicked() {
        presenter.getDetailedInfo(app);
    }

    @OnClick(R.id.delete_view)
    void onDeleteButtonClicked() {
        presenter.onDeleteButtonClicked(app);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }


    @Override
    public void onImageGalleryItemClicked(int position) {
        imageViewerBuilder.setStartPosition(position).show();
    }

    @OnClick(R.id.app_description)
    void onDescriptionClicked() {
        if (appDescription.getMaxLines() == DEFAULT_MAX_LINES) {
            textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", LIMIT_MAX_LINES);
        } else if (appDescription.getMaxLines() == LIMIT_MAX_LINES) {
            textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", DEFAULT_MAX_LINES);
        }

        if (textDescriptionAnimator != null && !textDescriptionAnimator.isStarted()) {
            textDescriptionAnimator.setDuration(ANIMATOR_DURATION).start();
        }


    }

    @Override
    public void onReplyClicked(String message, String vote) {
        new DialogManager().showPurchaseDialog(app, this, () -> {
            presenter.onSendReviewClicked(message, vote);
        });
    }

    @Override
    public void onReplyOnReviewClicked(UserReview userReview, String message) {
        new DialogManager().showPurchaseDialog(app, this, () -> {
            presenter.onSendReviewClicked(message, "5", userReview.txIndexOrigin);
        });
    }
}
