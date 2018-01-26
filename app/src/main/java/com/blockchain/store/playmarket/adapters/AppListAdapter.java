package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.SubCategory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.ViewHolder;
import static com.blockchain.store.playmarket.data.content.AppContent.AppItem;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AppListAdapter";
    private ArrayList<SubCategory> subCategories;

    public AppListAdapter(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new AppListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof AppListViewHolder) {
            ((AppListViewHolder) holder).bind(subCategories.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class AppListViewHolder extends ViewHolder {
        @BindView(R.id.id_category_title) TextView categoryTitle;
        @BindView(R.id.id_category_arrow) TextView categoryArrow;
        @BindView(R.id.recycler_view_nested) RecyclerView recyclerViewNested;
        private NestedAppListAdapter adapter;
        private Context context;

        public AppListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bind(SubCategory subCategory, int position) {
            categoryTitle.setText(subCategory.name);
            adapter = new NestedAppListAdapter(subCategory);
            recyclerViewNested.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewNested.setAdapter(adapter);

        }
    }

}