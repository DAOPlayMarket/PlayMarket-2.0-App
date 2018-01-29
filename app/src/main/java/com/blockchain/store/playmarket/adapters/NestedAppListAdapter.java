package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class NestedAppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MAIN_ITEM = 0;
    private static final int TYPE_LOADING = 1;
    private AppDispatcherType dispatcherType;
    private boolean isLoading = true;
    private AppListCallbacks mainCallback;

    public NestedAppListAdapter(AppListCallbacks mainCallback) {
        this.mainCallback = mainCallback;
    }

    public void setItemsDispatcher(AppDispatcherType dispatcherType) {
        this.dispatcherType = dispatcherType;
        notifyItemRangeChanged(0, dispatcherType.apps.size(), null);
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
//            nestedAppListViewHolder.cardView.setOnClickListener(v -> {
//                int position = nestedAppListViewHolder.getAdapterPosition();
//                mainCallback.onAppClicked(dispatcherType.apps.get(position));
//            });
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
            ((LoadViewHolder) holder).bind(isLoading);
        }
    }

    @Override
    public int getItemCount() {
        return dispatcherType.apps.size() + 1;
    }

    class NestedAppListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) ImageView imageView;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.ratingText) TextView ratingText;
        @BindView(R.id.ratingStar) ImageView ratingStar;
        @BindView(R.id.Price) TextView Price;
        @BindView(R.id.etherIcon) ImageView etherIcon;
        private Context context;

        public NestedAppListViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app, int position) {
            content.setText(app.nameApp);
            Glide.with(context)
                    .load(app.getIconUrl())
                    .into(imageView);
            cardView.setOnClickListener(v -> mainCallback.onAppClicked(app));
            dots.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, dots);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.main_menu, popup.getMenu());
                popup.show();
            });

        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) ProgressBar progressBar;

        public LoadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(boolean isLoading) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

}
