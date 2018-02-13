package com.blockchain.store.playmarket.ui.app_detail_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ImageListAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.CheckPurchaseResponse;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ethmobile.ethdroid.KeyManager;

public class AppDetailActivity extends AppCompatActivity implements AppDetailContract.View, ImageListAdapterCallback {
    private static final String TAG = "AppDetailActivity";
    private static final String APP_EXTRA = "app_extra";

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
    @BindView(R.id.invest_btn) Button investBtn;
    @BindView(R.id.delete_view) TextView deleteBtn;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.price_progress_bar) ProgressBar priceProgressBar;

    private ImageViewer.Builder imageViewerBuilder;
    private AppDetailPresenter presenter;
    private ImageListAdapter imageAdapter;
    private AppInfo appInfo;
    private App app;
    private boolean isUserPurchasedApp;

    public static void start(Context context, App app) {
        Intent starter = new Intent(context, AppDetailActivity.class);
        starter.putExtra(APP_EXTRA, app);
        context.startActivity(starter);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setViews() {
        imageIcon.setImageURI(Uri.parse(app.getIconUrl()));
        toolbarAppName.setText(app.nameApp);
        appName.setText(app.nameApp);
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
        mainLayoutHolder.setVisibility(View.VISIBLE);
        appDescription.setText(Html.fromHtml(appInfo.description));
        setupRecyclerView(appInfo);
        presenter.loadButtonsState(app, isUserPurchasedApp);
    }

    private void setupRecyclerView(AppInfo appInfo) {
        if (appInfo.pictures != null && appInfo.pictures.imageNameList != null) {
            imageViewerBuilder = new ImageViewer.Builder(this, appInfo.getImagePathList());
            imageAdapter = new ImageListAdapter(appInfo.getImagePathList(), this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageAdapter);
        }
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
        new DialogManager().showPurchaseDialog(appInfo, this, () -> {
            presenter.onPurchasedClicked(appInfo);
        });
    }

    @Override
    public void onCheckPurchaseReady(CheckPurchaseResponse checkPurchaseResponse) {
        isUserPurchasedApp = checkPurchaseResponse.isPurchased;
    }

    @Override
    public void onPurchaseSuccessful(PurchaseAppResponse purchaseAppResponse) {
        ToastUtil.showToast(R.string.successfully_paid);

        Log.d(TAG, "onPurchaseSuccessful() called with: purchaseAppResponse = [" + purchaseAppResponse + "]");
    }

    @Override
    public void onPurchaseError(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
        Log.d(TAG, "onPurchaseError() called with: throwable = [" + throwable + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy(app);
    }

    @OnClick(R.id.invest_btn)
    void onInvestBtnClicked() {
        new DialogManager().showInvestDialog(appInfo, this, investAmount -> {
//            presenter.onPurchaseClicked();

        });
        showInvestDialog();
//        presenter.onInvestClicked(appInfo);
    }

    private void showInvestDialog() { // todo need a refactor. Probably create class with all dialogs
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.invest_amount_dialog);

        EditText passwordText = dialog.findViewById(R.id.passwordText);
        EditText investmentAmountText = dialog.findViewById(R.id.investmentAmountText);
        Button continueButton = dialog.findViewById(R.id.continueButton);
        Button closeButton = dialog.findViewById(R.id.continueButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());
        continueButton.setOnClickListener(v -> {
            try {
                KeyManager keyManager = Application.keyManager;
                Account account = keyManager.getAccounts().get(0);
                keyManager.unlockAccountDuring(account, passwordText.getText().toString(), 60);
                Transaction tx = new Transaction(3, new Address(Constants.PLAY_MARKET_ADDRESS), new BigInt(500000000000000000L), new BigInt(250000), new BigInt(60000002359L)/*gas price*/, CryptoUtils.getDataForBuyApp(app.appId, "65D8706C1Ff1f9323272A818C22C1381de6D7556"));
                Transaction signedTransaction = keyManager.getKeystore().signTxPassphrase(account, "", tx, new BigInt(4));

                String rawTransaction = CryptoUtils.getRawTransaction(signedTransaction);

                Log.d(TAG, "showPurchaseDialog: encodeRLP: " + rawTransaction);
                Log.d(TAG, "showPurchaseDialog: encodeJSON: " + signedTransaction.encodeJSON());
                Log.d(TAG, "showPurchaseDialog: String: " + signedTransaction.string());

            } catch (Exception e) {
                e.printStackTrace();

            }
        });
        dialog.show();
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
}
