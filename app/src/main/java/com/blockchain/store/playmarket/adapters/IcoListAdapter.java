package com.blockchain.store.playmarket.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_content, parent, false);
        IcoAppViewHolder icoAppViewHolder = new IcoAppViewHolder(view);
        return icoAppViewHolder;
    }

    @Override
    public void onBindViewHolder(IcoAppViewHolder holder, int position) {
//        if (position == 0) {
        holder.bindAsCryptoDuel(appList.get(position));
//        } else {
//            holder.bind(appList.get(position));
//        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class IcoAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.ratingText) TextView ratingText;
        @BindView(R.id.ratingStar) ImageView ratingStar;
        @BindView(R.id.no_rating_textView) TextView noRating;
        @BindView(R.id.Price) TextView price;

        private Context context;

        IcoAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("CheckResult")
        public void bind(AppInfo app) {
            content.setText(app.nameApp);
            if (app.rating != null) {
                noRating.setVisibility(View.GONE);
                ratingText.setText(app.getRating());
            } else {
                noRating.setVisibility(View.VISIBLE);
                ratingText.setVisibility(View.GONE);
                ratingStar.setVisibility(View.GONE);
            }
            imageView.setImageURI(Uri.parse(app.getIconUrl()));
            if (app.isFree()) {
                price.setText(R.string.app_free);
            } else {
                price.setText(app.getPrice());
            }
//            cardView.setOnClickListener(v -> mainCallback.onAppClickedWithTransition(app, imageView));
        }


        public void bindAsCryptoDuel(AppInfo appInfo) {
            content.setText("CryptoDuel");
            imageView.setImageResource(R.drawable.cryptoduel_logo);
            imageView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            cardView.setOnClickListener(v -> {
                if (appInfo.appId.equalsIgnoreCase("434")) {
                    appListCallbacks.onCryptoDuelClicked(appInfo);
                } else {
                    appListCallbacks.onAppInfoClicked(appInfo);
                }

            });
            price.setText(R.string.app_free);
        }
    }

}
