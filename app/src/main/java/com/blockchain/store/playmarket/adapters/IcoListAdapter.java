package com.blockchain.store.playmarket.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        if (position == 0) {
            holder.bindAsATest(appList.get(position));
        } else if (position == 1) {
            holder.bindAsCryptoDuel(appList.get(position));
        } else {
            holder.bind(appList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }


    public class IcoAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) ImageView icon;
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
                String totalTokens = String.valueOf(Long.parseLong(app.icoSoftCap) / ((long) Math.pow(10, Long.parseLong(app.icoBalance.decimals))));
                goal.setText(String.format(context.getString(R.string.token_goal),
                        NumberUtils.formatTokenToSpacedNumber(totalTokens),
                        app.icoSymbol));
            }
            if (startBuyingBtn != null) {
                startBuyingBtn.setOnClickListener(v -> appListCallbacks.onAppInvestClicked(app.icoCrowdSaleAddress));
            }

            long timeToFirstStageEnding = app.getUnixTimeToStageEnding();
            if (countDownTimer == null && timeToFirstStageEnding > 0) {
                countDownTimer = initCountDownTimer(timeToFirstStageEnding);
            }
            setTransferButtonEnable();
        }

        private void onBitmapAndPaletteLoaded(Pair<Bitmap, Palette> bitmapPalettePair) {
            icon.setImageBitmap(bitmapPalettePair.first);
            Palette palette = bitmapPalettePair.second;
            if (backImageView != null) {
                backImageView.setBackgroundColor(palette.getDominantColor(Color.WHITE));
            }
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

        public void bindAsATest(AppInfo appInfo) {
            icon.setImageResource(R.drawable.ic_pm_logo);
            title.setText("PlayMarket 2.0");
            tokenBought.setText(appInfo.icoBalance.balanceOf);
            cardView.setOnClickListener(v -> appListCallbacks.onAppInfoClicked(null));

            if (smallDescription != null) {
                smallDescription.setText("The DAO PlayMarket 2.0 platform implies that holders of PMT tokens automatically become co-owners of the platform-based DAO PlayMarket Foundation (PMF). One of the primary functions of the foundation is open management of its resources in conjunction with other members of DAO PlayMarket 2.0. ");
            }
            if (backImageView != null && imageDisposable == null) {
                backImageView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            startBuyingBtn.setVisibility(View.GONE);
            transferBtn.setVisibility(View.GONE);
        }

        private void onPaletteLoaded(Palette palette) {
            if (backImageView != null) {
                backImageView.setBackgroundColor(palette.getDominantColor(Color.WHITE));
            }
        }

        public void bindAsCryptoDuel(AppInfo appInfo) {
            icon.setImageResource(R.drawable.cryptoduel_logo);
            title.setText("CryptoDuel");
            tokenBought.setText(appInfo.icoBalance.balanceOf);
            cardView.setOnClickListener(v -> appListCallbacks.onCryptoDuelClicked());

            if (smallDescription != null) {
                smallDescription.setText("Crypto Duel is a bet between two players, the winner is determined by an independent and open smart contract algorithm ");
            }
            if (backImageView != null && imageDisposable == null) {
                backImageView.setBackgroundColor(context.getResources().getColor(R.color.ico_bg));

            }
            startBuyingBtn.setVisibility(View.GONE);
            transferBtn.setVisibility(View.GONE);
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
