package com.blockchain.store.playmarket.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.SubCategory;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuFragment;
import com.blockchain.store.playmarket.utilities.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AppListAdapter";
    private ArrayList<SubCategory> subCategories;
    private ArrayList<AppDispatcherType> appDispatcherTypes = new ArrayList<>();
    private RecyclerView.RecycledViewPool recycledViewPool;
    private EndlessRecyclerOnScrollListener.EndlessCallback endlessCallback;
    private AppListCallbacks mainCallback;

    public AppListAdapter(ArrayList<SubCategory> subCategories, ArrayList<AppDispatcherType> appDispatcherTypes, EndlessRecyclerOnScrollListener.EndlessCallback endlessCallback, AppListCallbacks mainCallback) {
        this.subCategories = subCategories;
        this.appDispatcherTypes.addAll(appDispatcherTypes);
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
        this.endlessCallback = endlessCallback;
        this.mainCallback = mainCallback;
    }

    public AppListAdapter(ArrayList<SubCategory> subCategories, EndlessRecyclerOnScrollListener.EndlessCallback endlessCallback, AppListCallbacks mainCallback) {
        this.subCategories = subCategories;
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
        this.endlessCallback = endlessCallback;
        this.mainCallback = mainCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        AppListViewHolder appListViewHolder = new AppListViewHolder(view, endlessCallback);
        appListViewHolder.recyclerViewNested.setRecycledViewPool(recycledViewPool);
        appListViewHolder.adapter = new NestedAppListAdapter(mainCallback);
        appListViewHolder.recyclerViewNested.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        appListViewHolder.recyclerViewNested.setAdapter(appListViewHolder.adapter);
        return appListViewHolder;
    }

    @Override
    public long getItemId(int position) {
        return appDispatcherTypes.get(position).hashCode();
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

    public void addNewItems(AppDispatcherType updatedDispatcherType) {
        for (int i = 0; i < appDispatcherTypes.size(); i++) {
            AppDispatcherType type = appDispatcherTypes.get(i);
            if (updatedDispatcherType.subCategoryId == type.subCategoryId && updatedDispatcherType.categoryId.equalsIgnoreCase(type.categoryId)) {
                type = updatedDispatcherType;
                notifyDataSetChanged();
                Log.d(TAG, "addNewItems: total count " + type.apps.size());
            }
        }
    }

    public void setData(ArrayList<AppDispatcherType> appDispatcherTypes) {
        this.appDispatcherTypes = appDispatcherTypes;
        notifyItemRangeInserted(0, appDispatcherTypes.size());
    }


    public class AppListViewHolder extends ViewHolder {
        @BindView(R.id.id_category_title) TextView categoryTitle;
        @BindView(R.id.id_category_arrow) TextView categoryArrow;
        @BindView(R.id.recycler_view_nested) RecyclerView recyclerViewNested;
        private NestedAppListAdapter adapter;
        private EndlessRecyclerOnScrollListener.EndlessCallback endlessCallback;

        public AppListViewHolder(View itemView, EndlessRecyclerOnScrollListener.EndlessCallback endlessCallback) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.endlessCallback = endlessCallback;
        }

        public void bind(SubCategory subCategory, AppDispatcherType dispatcherType, int position) {
            if (!categoryTitle.getText().equals(subCategory.name))
                categoryTitle.setText(subCategory.name);

            adapter.setItemsDispatcher(dispatcherType);

            if (!dispatcherType.apps.isEmpty()) {
                recyclerViewNested.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) recyclerViewNested.getLayoutManager(), dispatcherType, this.endlessCallback));
            }


        }

    }

}