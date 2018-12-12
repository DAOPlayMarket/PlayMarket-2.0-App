package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.AppDetailAdapter;
import com.blockchain.store.playmarket.adapters.ImageListAdapter;
import com.blockchain.store.playmarket.adapters.UserReviewAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.AppReviewsData;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.interfaces.AppDetailsImpl;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.ui.invest_screen.InvestActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.FrescoUtils;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.views.FadingTextView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.blockchain.store.playmarket.utilities.Constants.PLAY_MARKET_ADDRESS;

public class AppDetailActivity extends AppCompatActivity implements AppDetailContract.View, ImageListAdapterCallback, UserReviewAdapter.UserReviewCallback {
    private static final String TAG = "AppDetailActivity";
    private static final String APP_EXTRA = "app_extra";
    private static final String APP_INFO_EXTRA = "app_info_extra";
    private static final String EXTRA_TRANSITION_NAME = "transition_name_extra";

    private static final int DEFAULT_MAX_LINES = 3;
    private static final int LIMIT_MAX_LINES = 150;
    private static final int ANIMATOR_DURATION = 400;
    private static final int PURCHASE_RESULT_CODE = 111;

    @BindView(R.id.top_layout_app_name) TextView toolbarAppName;
    @BindView(R.id.top_layout_holder) LinearLayout topLayoutHolder;
    @BindView(R.id.top_layout_back_arrow) TextView topLayoutBackArrow;
    @BindView(R.id.action_btn) Button actionBtn;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.main_layout_holder) View mainLayoutHolder;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.image_icon) ImageView imageIcon;
    @BindView(R.id.app_name) TextView appName;
    @BindView(R.id.app_description) FadingTextView appDescription;
    @BindView(R.id.app_description_short) TextView appDescriptionShort;
    @BindView(R.id.rating_textView) TextView appRating;
    @BindView(R.id.marks_count_textView) TextView marksCountTextView;
    @BindView(R.id.age_restrictions_textView) TextView ageRestrictionsTextView;
    @BindView(R.id.no_marks_textView) TextView noMarksTextView;
    @BindView(R.id.rating_materialRatingBar) MaterialRatingBar ratingBar;
    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView;

    @BindView(R.id.invest_btn) Button investBtn;
    @BindView(R.id.delete_view) TextView deleteBtn;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.price_progress_bar) ProgressBar priceProgressBar;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private boolean isUserPurchasedApp;

    private ObjectAnimator textDescriptionAnimator;
    private ImageViewer.Builder imageViewerBuilder;
    private ImageListAdapter imageAdapter;
    private AppDetailPresenter presenter;
    private AppInfo appInfo;
    private App app;

    private AppDetailAdapter appDetailAdapter;

    public static void start(Context context, App app) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_EXTRA, app);
        context.startActivity(starter);
    }

    public static void start(Context context, AppInfo app) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_INFO_EXTRA, app);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            appInfo = getIntent().getParcelableExtra(APP_INFO_EXTRA);
            if (appInfo != null) {
                app = appInfo.convertToApp(appInfo);
            } else {
                app = getIntent().getParcelableExtra(APP_EXTRA);
            }

        }
        attachPresenter();
        setViews();
        generateToolbarColor();
    }

    private void attachPresenter() {
        presenter = new AppDetailPresenter();
        presenter.init(this);
        if (appInfo != null) {
            onDetailedInfoReady(appInfo);
        } else {
            presenter.getDetailedInfo(app);
        }
        presenter.getReviews(app.appId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setViews() {
        toolbarAppName.setText(app.nameApp);
        appName.setText(app.nameApp);

        String ageRestrictions = app.ageRestrictions + " +";
        ageRestrictionsTextView.setText(ageRestrictions);

        if (app.rating == null || app.rating.ratingCount == 0) {
            marksCountTextView.setVisibility(View.GONE);
        } else {
            marksCountTextView.setText(app.rating.ratingCount + " marks");
        }
    }

    @SuppressLint("CheckResult")
    private void generateToolbarColor() {
        FrescoUtils.getBitmapDataSource(this, app.getIconUrl())
                .flatMap(FrescoUtils::getPalleteFromBitemap, Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBitmapAndPaletteLoaded, this::onBitmapAndPaletteFailed);
    }

    private void onBitmapAndPaletteFailed(Throwable throwable) {

    }

    private void onBitmapAndPaletteLoaded(Pair<Bitmap, Palette> bitmapPalettePair) {
        Bitmap bitmap = bitmapPalettePair.first;
        Palette p = bitmapPalettePair.second;
        imageIcon.setImageBitmap(bitmap);
        supportStartPostponedEnterTransition();
        int lightVibrantColor = p.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int darkMutedColor = p.getDarkMutedColor(getResources().getColor(android.R.color.black));
        topLayoutHolder.setBackgroundColor(lightVibrantColor);
        toolbarAppName.setTextColor(darkMutedColor);
        topLayoutBackArrow.setTextColor(darkMutedColor);
    }


    @Override
    public void onDetailedInfoReady(AppInfo appInfo) {
        this.appInfo = appInfo;
        setInvestButtonVisibility(appInfo);
        mainLayoutHolder.setVisibility(View.VISIBLE);
        if (appInfo.description != null)
            appDescription.setText(Html.fromHtml(app.description));
        if (appInfo.shortDescription != null)
            appDescriptionShort.setText(Html.fromHtml(app.shortDescription));
        if (appInfo.rating != null) {
            noMarksTextView.setVisibility(View.GONE);
            appRating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            appRating.setText(String.valueOf(appInfo.getRating()));
            ratingBar.setRating(Float.valueOf(String.valueOf(appInfo.getRating())));
        } else {
            noMarksTextView.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.GONE);
        }

        //setupScreenshotRecyclerView(appInfo);
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setInvestButtonVisibility(AppInfo appInfo) {
        investBtn.setVisibility(View.INVISIBLE);
        if (this.appInfo.isIco || app.appId.equalsIgnoreCase("434")) {
            investBtn.setVisibility(View.INVISIBLE);
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
        ArrayList<AppDetailsImpl> appDetails = new ArrayList<>();
        appDetails.add(app);
        appDetails.add(new AppReviewsData(userReviews));
        if (app.appId.equalsIgnoreCase("434")) {
            appDetails.add(() -> AppDetailAdapter.ViewTypes.VIEW_TYPE_BUDGET);
            appDetails.add(() -> AppDetailAdapter.ViewTypes.VIEW_TYPE_DESCRIPTION);
            appDetails.add(() -> AppDetailAdapter.ViewTypes.VIEW_TYPE_STEP);
            appDetails.add(() -> AppDetailAdapter.ViewTypes.VIEW_TYPE_GRAPH);
            appDetails.add(() -> AppDetailAdapter.ViewTypes.ABOUT);
        }
        appDetailAdapter = new AppDetailAdapter(appDetails, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(appDetailAdapter);
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
        TransferActivity.startWithResult(this, PLAY_MARKET_ADDRESS, app, Constants.TransactionTypes.BUY_APP, PURCHASE_RESULT_CODE);
    }

    @Override
    public void onCheckPurchaseReady(boolean isPurchased) {
        isUserPurchasedApp = isPurchased;
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
//        InvestActivity.start(this, appInfo);
        nestedScrollView.scrollTo(1500, 1500);
        recyclerView.smoothScrollToPosition(3);
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
        this.onBackPressed();
    }


    @Override
    public void onImageGalleryItemClicked(int position) {
        imageViewerBuilder.setStartPosition(position).show();
    }

    @OnClick(R.id.app_description)
    void onDescriptionClicked() {
        if (appDescription.getMaxLines() == DEFAULT_MAX_LINES) {
            textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", LIMIT_MAX_LINES);
            appDescription.setNeedToDrawFading(true);
        } else if (appDescription.getMaxLines() == LIMIT_MAX_LINES) {
            textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", DEFAULT_MAX_LINES);
            appDescription.setNeedToDrawFading(false);
        }

        if (textDescriptionAnimator != null && !textDescriptionAnimator.isStarted()) {
            textDescriptionAnimator.setDuration(ANIMATOR_DURATION).start();
        }

    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        onReplyClicked();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PURCHASE_RESULT_CODE && resultCode == RESULT_OK) {
            presenter.getDetailedInfo(app);
        }
    }
}
