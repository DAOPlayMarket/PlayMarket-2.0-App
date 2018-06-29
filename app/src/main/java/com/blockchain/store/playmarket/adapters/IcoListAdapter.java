package com.blockchain.store.playmarket.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class IcoListAdapter extends RecyclerView.Adapter<IcoListAdapter.IcoAppViewHolder> {
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


        public IcoAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app) {
        }
    }
}
