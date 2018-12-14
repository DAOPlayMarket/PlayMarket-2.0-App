package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.interfaces.AppsAdapterCallback;
import com.blockchain.store.playmarket.utilities.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAppsAdapter extends RecyclerView.Adapter<MyAppsAdapter.MyAppsViewHolder> {
    private static final String TAG = "MyAppsAdapter";
    private ArrayList<AppLibrary> appLibraries;
    private AppsAdapterCallback callback;

    public MyAppsAdapter(ArrayList<AppLibrary> appLibraries, AppsAdapterCallback callback) {
        this.appLibraries = appLibraries;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_apps_item, parent, false);
        MyAppsViewHolder myAppsViewHolder = new MyAppsViewHolder(view);
        myAppsViewHolder.actionAreaHolder.setOnClickListener(v -> {
            appLibraries.get(myAppsViewHolder.getAdapterPosition()).isSelected = false;
            callback.onActionButtonClicked(appLibraries.get(myAppsViewHolder.getAdapterPosition()));
        });
        myAppsViewHolder.layoutHolder.setOnClickListener(v -> {
            int clickPosition = myAppsViewHolder.getAdapterPosition();
            boolean isWasSelected = appLibraries.get(clickPosition).isSelected;
            appLibraries.get(clickPosition).isSelected = !isWasSelected;
            callback.onLayoutClicked(getSelectedItems().size());
            notifyDataSetChanged();
        });
        return myAppsViewHolder;
    }

    private ArrayList<AppLibrary> getSelectedItems() {
        ArrayList<AppLibrary> selectedItems = new ArrayList<>();
        for (AppLibrary library : appLibraries) {
            if (library.isSelected) {
                selectedItems.add(library);
            }
        }
        return selectedItems;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppsViewHolder holder, int position) {
        holder.bind(appLibraries.get(position));
    }

    @Override
    public int getItemCount() {
        return appLibraries.size();
    }


    @Override
    public long getItemId(int position) {
        return appLibraries.get(position).hashCode();
    }

    public void reportAppStateChanged(App app, int progress, Constants.APP_STATE appState) {
        if (getItemByApp(app) != null) {
            getItemByApp(app).downloadProgress = String.valueOf(progress);
            getItemByApp(app).appState = appState;
            if (appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED) {
                getItemByApp(app).isHasUpdate = false;
            }
            notifyDataSetChanged();
        }
    }


    private AppLibrary getItemByApp(App app) {
        for (int i = 0; i < appLibraries.size(); i++) {
            if (appLibraries.get(i).app != null && appLibraries.get(i).app.appId.equals(app.appId)) {
                return appLibraries.get(i);
            }
        }
        return null;
    }

    public ArrayList<AppLibrary> getAllItems() {
        return appLibraries;
    }

    public ArrayList<AppLibrary> getAllItemsWithUpdate() {
        ArrayList<AppLibrary> apps = new ArrayList<>();
        for (AppLibrary appLibrary : appLibraries) {
            if (appLibrary.isHasUpdate || appLibrary.appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED) {
                apps.add(appLibrary);
            }
        }
        return apps;
    }

    public void performActionOnSelectedItems() {
        for (AppLibrary appLibrary : appLibraries) {
            if (appLibrary.isSelected) {
                appLibrary.isSelected = false;
                callback.onActionButtonClicked(appLibrary);

            }
        }
    }

    public void performUpdateAll() {
        for (AppLibrary appLibrary : appLibraries) {
            if (appLibrary.isHasUpdate && appLibrary.app != null)
                callback.onActionButtonClicked(appLibrary);
        }
    }

    public void refreshItemStatus(ArrayList<AppLibrary> updatedItems) {
        for (AppLibrary updatedItem : updatedItems) {
            AppLibrary localItem = getItemByApp(updatedItem.app);
            if (localItem != null) {
                localItem.isHasUpdate = updatedItem.isHasUpdate;
                localItem.appState = updatedItem.appState;
            }
        }
        notifyDataSetChanged();
    }

    public class MyAppsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_apps_icon) ImageView icon;
        @BindView(R.id.my_apps_title) TextView title;
        @BindView(R.id.my_apps_version) TextView version;
        @BindView(R.id.my_apps_action_btn) ImageView actionBtn;
        @BindView(R.id.my_apps_status) TextView status;
        @BindView(R.id.my_apps_holder) CardView layoutHolder;
        @BindView(R.id.my_apps_action_btn_area) LinearLayout actionAreaHolder;

        private Context context;

        public MyAppsViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(AppLibrary appLibrary) {
            setUiByAppState(appLibrary);
            if (appLibrary.isSelected) {
                layoutHolder.setBackgroundColor(context.getResources().getColor(R.color.my_apps_layout_color_selected));
            } else {
                layoutHolder.setBackgroundColor(context.getResources().getColor(R.color.my_apps_layout_color));
            }
            title.setText(appLibrary.title);
            version.setText(appLibrary.getVersionsAsString());
            Glide.with(context).load(appLibrary.icon).into(icon);

        }

        private void setUiByAppState(AppLibrary appLibrary) {
            layoutHolder.setClickable(appLibrary.isHasUpdate && appLibrary.appState == Constants.APP_STATE.STATE_UNKNOWN);
            switch (appLibrary.appState) {
                case STATE_DOWNLOAD_STARTED:
                case STATE_DOWNLOADING:
                    status.setTextColor(context.getResources().getColor(R.color.action_btn_bg));
                    actionBtn.setVisibility(View.GONE);
                    status.setVisibility(View.VISIBLE);
                    status.setText(appLibrary.downloadProgress + "%");
                    break;
                case STATE_DOWNLOAD_ERROR:
                    actionBtn.setVisibility(View.VISIBLE);
                    status.setVisibility(View.GONE);
                    break;
                case STATE_UNKNOWN:
                    actionBtn.setVisibility(appLibrary.isHasUpdate ? View.VISIBLE : View.GONE);
                    status.setVisibility(appLibrary.isHasUpdate ? View.VISIBLE : View.GONE);
                    status.setText(context.getString(R.string.has_update));

                    if (appLibrary.isSelected) {
                        status.setText(R.string.chosed);
                        status.setTextColor(context.getResources().getColor(R.color.action_btn_bg));
                    } else {
                        status.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    break;
                case STATE_DOWNLOADED_NOT_INSTALLED:
                    status.setVisibility(View.GONE);
                    break;
                case STATE_UPDATE_DOWNLOADED_NOT_INSTALLED:
                    actionBtn.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);
                    status.setText("Install update");
                    break;

            }
        }
    }
}
