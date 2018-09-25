package com.blockchain.store.playmarket.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.interfaces.AppInfoCallback;
import com.blockchain.store.playmarket.utilities.FrescoUtils;
import com.blockchain.store.playmarket.utilities.NumberUtils;
import com.blockchain.store.playmarket.utilities.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IcoListAdapter extends RecyclerView.Adapter<IcoListAdapter.IcoAppViewHolder> {
    private static final String TAG = "IcoListAdapter";

    private ArrayList<AppInfo> appList;
    private AppInfoCallback appListCallbacks;
    private boolean isUsedAlternativeDesign = false;

    public IcoListAdapter(ArrayList<AppInfo> appList, AppInfoCallback appListCallbacks) {
        this.appList = appList;
        this.appListCallbacks = appListCallbacks;
    }

    public IcoListAdapter(ArrayList<AppInfo> appList, AppInfoCallback appListCallbacks, boolean isUsedAlternativeDesign) {
        this.appList = appList;
        this.appListCallbacks = appListCallbacks;
        this.isUsedAlternativeDesign = isUsedAlternativeDesign;
    }


    @Override
    public IcoAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (isUsedAlternativeDesign) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ico_app_list_item_new, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ico_app_list_item, parent, false);
        }
        IcoAppViewHolder icoAppViewHolder = new IcoAppViewHolder(view);
        return icoAppViewHolder;
    }

    @Override
    public void onBindViewHolder(IcoAppViewHolder holder, int position) {
        holder.bind(appList.get(position));
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class IcoAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) SimpleDraweeView icon;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.tokens_bought) TextView tokenBought;
        @BindView(R.id.time_remains) TextView timeRemains;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.invest_clock_image) ImageView clockIcon;
        @BindView(R.id.transfer_token) Button transferBtn;

        private CountDownTimer countDownTimer;
        private Context context;
        private Disposable imageDisposable;
        @BindView(R.id.view3) @Nullable View backImageView;
        @BindView(R.id.small_description) @Nullable TextView smallDescription;
        @BindView(R.id.goal) @Nullable TextView goal;
        @BindView(R.id.start_buying) @Nullable Button startBuyingBtn;


        IcoAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("CheckResult")
        public void bind(AppInfo app) {
            icon.setImageURI(Uri.parse(app.getIconUrl()));
            title.setText(app.nameApp);
            tokenBought.setText(app.icoBalance.getTokenCount());
            cardView.setOnClickListener(v -> appListCallbacks.onAppInfoClicked(app));
            transferBtn.setOnClickListener(v -> appListCallbacks.onAppTransferTokenClicked(app));

            if (smallDescription != null) {
                smallDescription.setText(app.shortDescription);
            }
            if (backImageView != null && imageDisposable == null) {
                imageDisposable = FrescoUtils.getBitmapDataSource(context, app.getIconUrl())
                        .flatMap(FrescoUtils::getPalleteFromBitemap, Pair::new)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onBitmapAndPaletteLoaded, this::onBitmapAndPaletteFailed);
            }
            if (goal != null) {
                goal.setText(String.format(context.getString(R.string.token_goal),
                        NumberUtils.formatStringToSpacedNumber(app.icoTotalSupply, NumberUtils.TOKEN_NUMBER_OF_CHARACTER),
                        app.icoSymbol));
            }
            if (startBuyingBtn != null) {
                startBuyingBtn.setOnClickListener(v -> appListCallbacks.onAppInvestClicked(app.adrICO));
            }

            long timeToFirstStageEnding = app.getUnixTimeToFirstStageEnding();
            if (countDownTimer == null && timeToFirstStageEnding > 0) {
                countDownTimer = initCountDownTimer(timeToFirstStageEnding);
            }
            setTransferButtonEnable();
        }

        private void onBitmapAndPaletteLoaded(Pair<Bitmap, Palette> bitmapPalettePair) {
            icon.setImageBitmap(bitmapPalettePair.first);
            Palette palette = bitmapPalettePair.second;
            backImageView.setBackgroundColor(palette.getDominantColor(Color.WHITE));
        }

        private void onBitmapAndPaletteFailed(Throwable throwable) {

        }

        private void setTransferButtonEnable() {
            transferBtn.setEnabled(true);
        }

        private CountDownTimer initCountDownTimer(long timeToFirstStageEnding) {
            return new CountDownTimer(timeToFirstStageEnding * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String formattedString = TimeUtils.unixTimeToDays(millisUntilFinished);
                    timeRemains.setText(formattedString);
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }

    }


    private String tokenTransform(String tokensStr, String decimalsStr) {
        long tokensNum = Long.valueOf(tokensStr);
        short decimalsNum = Short.valueOf(decimalsStr);
        double transformedTokensNum = tokensNum * Math.pow(10, -decimalsNum);
        transformedTokensNum = Math.round(transformedTokensNum * 10000.0) / 10000.0;
        return String.valueOf(transformedTokensNum);
    }
}
