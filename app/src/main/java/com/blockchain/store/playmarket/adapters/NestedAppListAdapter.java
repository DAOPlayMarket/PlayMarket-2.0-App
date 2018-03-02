package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class NestedAppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NestedAppListAdapter";
    private static final int TYPE_MAIN_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private AppDispatcherType dispatcherType;
    private AppListCallbacks mainCallback;
    private boolean isLoading = true;

    public NestedAppListAdapter(AppListCallbacks mainCallback) {
        this.mainCallback = mainCallback;
    }

    public void setItemsDispatcher(AppDispatcherType dispatcherType) {
        if (dispatcherType != null && dispatcherType.previousItemCount == dispatcherType.apps.size()) {
            this.isLoading = false;
        }
        this.dispatcherType = dispatcherType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dispatcherType.apps.size()) {
            return TYPE_MAIN_ITEM;
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < dispatcherType.apps.size()) {
            return dispatcherType.apps.get(position).hashCode();
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MAIN_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_list_content, parent, false);
            NestedAppListViewHolder nestedAppListViewHolder = new NestedAppListViewHolder(view);
            return nestedAppListViewHolder;
        }
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_list_state_loading, parent, false);
            return new LoadViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NestedAppListViewHolder) {
            ((NestedAppListViewHolder) holder).bind(dispatcherType.apps.get(position), position);
        }
        if (holder instanceof LoadViewHolder) {
            ((LoadViewHolder) holder).bind(isLoading, dispatcherType.apps.size());
        }
    }

    @Override
    public int getItemCount() {
        return dispatcherType.apps.size() + 1;
    }

    class NestedAppListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.ratingText) TextView ratingText;
        @BindView(R.id.ratingStar) ImageView ratingStar;
        @BindView(R.id.Price) TextView price;
        @BindView(R.id.etherIcon) ImageView etherIcon;
        private Context context;

        public NestedAppListViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app, int position) {
            content.setText(app.nameApp);
            imageView.setImageURI(Uri.parse(app.getIconUrl()));
            if (app.isFree) {
                price.setText(R.string.app_free);
            } else {
                String priceInEther = new EthereumPrice(app.price).inEther().toString();
                price.setText(priceInEther);
            }
            cardView.setOnClickListener(v -> mainCallback.onAppClicked(app));
//            dots.setOnClickListener(v -> {
//                PopupMenu popup = new PopupMenu(context, dots);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.main_menu, popup.getMenu());
//                popup.show();
//            });

        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.cardView) View cardView;
        @BindView(R.id.no_item_view) TextView textView;

        public LoadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(boolean isLoading, int size) {
            if (!isLoading && size == 0) {
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                cardView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }

        }
    }

}
