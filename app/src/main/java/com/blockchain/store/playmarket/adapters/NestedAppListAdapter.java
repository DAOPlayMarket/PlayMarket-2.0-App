package com.blockchain.store.playmarket.adapters;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.SubCategory;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Crypton04 on 25.01.2018.
 */

public class NestedAppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SubCategory subCategory;
    private AppDispatcherType dispatcherType;

    public NestedAppListAdapter(SubCategory subCategory, AppDispatcherType dispatcherType) {
        this.subCategory = subCategory;
        this.dispatcherType = dispatcherType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_content, parent, false);
        NestedAppListViewHolder nestedAppListViewHolder = new NestedAppListViewHolder(view);
        return new NestedAppListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NestedAppListViewHolder) {
            ((NestedAppListViewHolder) holder).bind(dispatcherType.apps.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return dispatcherType.apps.size();
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
            dots.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, dots);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.main_menu, popup.getMenu());
                popup.show();
            });

        }
    }

}
