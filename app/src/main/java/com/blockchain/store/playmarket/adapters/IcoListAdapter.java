package com.blockchain.store.playmarket.adapters;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IcoListAdapter extends RecyclerView.Adapter<IcoListAdapter.IcoAppViewHolder> {
    private static final String TAG = "IcoListAdapter";

    private ArrayList<App> appList = new ArrayList<App>();
    private AppListCallbacks appListCallbacks;

    public IcoListAdapter(ArrayList<App> appList, AppListCallbacks appListCallbacks) {
        this.appList = appList;
        this.appListCallbacks = appListCallbacks;
    }

    @Override
    public IcoAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ico_app_list_item, parent, false);
        return new IcoAppViewHolder(view);
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
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.tokens_bought) TextView tokenBought;
        @BindView(R.id.time_remains) TextView timeRemains;
        @BindView(R.id.cardView) CardView cardView;

        IcoAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(v -> appListCallbacks.onAppClickedWithTransition(appList.get(getAdapterPosition()), itemView));
        }

        public void bind(App app) {
            icon.setImageURI(Uri.parse(app.getIconUrl()));
            title.setText(app.nameApp);
            tokenBought.setText(String.valueOf(tokenTransform(app.icoBalance.balanceOf, app.icoBalance.decimals)));
            cardView.setOnClickListener(v -> appListCallbacks.onAppClicked(app));
        }
    }

    private String tokenTransform(String tokensStr, String decimalsStr){
        long tokensNum = Long.valueOf(tokensStr);
        short decimalsNum = Short.valueOf(decimalsStr);
        double transformedTokensNum = tokensNum * Math.pow(10, -decimalsNum);
        transformedTokensNum = Math.round(transformedTokensNum * 10000.0) / 10000.0;
        return String.valueOf(transformedTokensNum);
    }
}
