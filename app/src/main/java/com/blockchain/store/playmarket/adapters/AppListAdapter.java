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
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.SubCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<AppDispatcherType> appDispatcherTypes;

    public AppListAdapter(ArrayList<SubCategory> subCategories, ArrayList<AppDispatcherType> appDispatcherTypes) {
        this.subCategories = subCategories;
        this.appDispatcherTypes = appDispatcherTypes;
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
            ((AppListViewHolder) holder).bind(subCategories.get(position), appDispatcherTypes.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return appDispatcherTypes.size();
    }

    public void addNewItems(AppDispatcherType updatedDispatherType) {
        for (AppDispatcherType type : appDispatcherTypes) {
            if (updatedDispatherType.subCategoryId == type.subCategoryId && updatedDispatherType.categoryId.equalsIgnoreCase(type.categoryId)) {
                type.apps = updatedDispatherType.apps;
            }
        }
        notifyDataSetChanged();

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

        public void bind(SubCategory subCategory, AppDispatcherType dispatcherType, int position) {
            categoryTitle.setText(subCategory.name);
            adapter = new NestedAppListAdapter(subCategory, dispatcherType);
            recyclerViewNested.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewNested.setAdapter(adapter);

        }
    }

}