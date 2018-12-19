package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.interfaces.AppListHolderCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class NestedAppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NestedAppListAdapter";
    private static final int TYPE_MAIN_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private AppDispatcherType dispatcherType;
    private AppListCallbacks mainCallback;
    private AppListHolderCallback holderCallback;
    private boolean isLoading = true;

    public NestedAppListAdapter(AppListCallbacks mainCallback, AppListHolderCallback holderCallback) {
        this.mainCallback = mainCallback;
        this.holderCallback = holderCallback;
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
            ((LoadViewHolder) holder).bind(isLoading, dispatcherType.apps.size(), dispatcherType);
        }
    }

    @Override
    public int getItemCount() {
        return dispatcherType.apps.size() + 1;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof NestedAppListViewHolder) {
            ((NestedAppListViewHolder) holder).imageView.setImageURI("");
        }
    }

    class NestedAppListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.dots) ImageView dots;
        @BindView(R.id.ratingText) TextView ratingText;
        @BindView(R.id.ratingStar) ImageView ratingStar;
        @BindView(R.id.no_rating_textView) TextView noRating;
        @BindView(R.id.Price) TextView price;

        private Disposable imageDisposable;
        private Context context;

        public NestedAppListViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app, int position) {
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
            cardView.setOnClickListener(v -> mainCallback.onAppClickedWithTransition(app, imageView));
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) ProgressBar progressBar;
        @BindView(R.id.cardView) View cardView;
        @BindView(R.id.no_item_view) TextView noItemView;
        @BindView(R.id.repeat_btn) Button repeatBtn;

        private Context context;

        public LoadViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(boolean isLoading, int size, AppDispatcherType appDispatcherType) {
            boolean isContainsError = appDispatcherType.isContainsError;
            repeatBtn.setVisibility(isContainsError ? View.VISIBLE : View.GONE);
            noItemView.setText(isContainsError ?
                    context.getString(R.string.load_failed)
                    : context.getString(R.string.no_items));
            if (isContainsError) {
                repeatBtn.setOnClickListener(v -> holderCallback.onItemRepeatRequestClicked(appDispatcherType));
                progressBar.setVisibility(View.GONE);
            } else if (!isLoading && size == 0) {
                noItemView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                cardView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }

        }
    }

}
