package com.blockchain.store.playmarket.adapters;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 12.02.2018.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder> {
    private ArrayList<App> appList = new ArrayList<App>();
    private AppListCallbacks appListCallbacks;

    public SearchListAdapter(ArrayList<App> appList, AppListCallbacks appListCallbacks) {
        this.appList = appList;
        this.appListCallbacks = appListCallbacks;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bind(appList.get(position));
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.ratingStar) ImageView ratingStar;
        @BindView(R.id.ratingText) TextView ratingText;
        @BindView(R.id.Price) TextView price;
        @BindView(R.id.etherIcon) ImageView etherIcon;
        @BindView(R.id.cardView) CardView cardView;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app) {
            content.setText(app.nameApp);
            ratingText.setText(app.getRating());
            imageView.setImageURI(Uri.parse(app.getIconUrl()));
            etherIcon.setVisibility(app.isFree ? View.GONE : View.VISIBLE);
            if (app.isFree) {
                price.setText(R.string.app_free);
            } else {
                String priceInEther = new EthereumPrice(app.price).inEther().toString();
                price.setText(priceInEther);

            }
            cardView.setOnClickListener(v -> appListCallbacks.onAppClicked(app));
        }
    }
}
