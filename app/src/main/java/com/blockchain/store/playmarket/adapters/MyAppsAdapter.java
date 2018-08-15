package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAppsAdapter extends RecyclerView.Adapter<MyAppsAdapter.MyAppsViewHolder> {
    private static final String TAG = "MyAppsAdapter";
    private ArrayList<AppLibrary> appLibraries;

    public MyAppsAdapter(ArrayList<AppLibrary> appLibraries) {
        this.appLibraries = appLibraries;
    }

    @NonNull
    @Override
    public MyAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_apps_item, parent, false);
        return new MyAppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppsViewHolder holder, int position) {
        holder.bind(appLibraries.get(position));
    }

    @Override
    public int getItemCount() {
        return appLibraries.size();
    }

    public class MyAppsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_apps_icon) ImageView icon;
        @BindView(R.id.my_apps_title) TextView title;
        @BindView(R.id.my_apps_version) TextView version;
        private Context context;

        public MyAppsViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(AppLibrary appLibrary) {
            title.setText(appLibrary.title);
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(MyPackageManager.getVersionNameByPackageName(appLibrary.applicationInfo.packageName)));
            if (appLibrary.app != null && appLibrary.isHasUpdate) {
                stringBuilder.append(" | ");
                stringBuilder.append(appLibrary.app.version);
            }
            version.setText(stringBuilder.toString());
            Glide.with(context).load(appLibrary.icon).into(icon);
        }
    }
}
