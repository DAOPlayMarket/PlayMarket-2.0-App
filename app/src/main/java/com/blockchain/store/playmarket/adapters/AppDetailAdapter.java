package com.blockchain.store.playmarket.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;

public class AppDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_details_main_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MainItemViewHolder extends RecyclerView.ViewHolder {

        public MainItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ScreenShotsViewHolder extends RecyclerView.ViewHolder {

        public ScreenShotsViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public ReviewsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
