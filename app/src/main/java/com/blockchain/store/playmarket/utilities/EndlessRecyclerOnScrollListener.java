package com.blockchain.store.playmarket.utilities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockchain.store.playmarket.data.entities.AppDispatcherType;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private AppDispatcherType dispatcherType;
    private boolean isStopLoading = false;
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;
    private EndlessCallback callback;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, AppDispatcherType dispatcherType, EndlessCallback callback) {
        this.linearLayoutManager = linearLayoutManager;
        this.dispatcherType = dispatcherType;
        this.callback = callback;
    }

    public void stopLoading() {
        isStopLoading = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (isStopLoading)
            return;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            currentPage++;
            callback.onLoadMore(currentPage, dispatcherType);
            loading = true;
        }
    }

    public interface EndlessCallback {
        void onLoadMore(int currentPage, AppDispatcherType dispatcherType);
    }

}
