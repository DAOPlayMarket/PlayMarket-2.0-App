package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.ViewHolder;
import static com.blockchain.store.playmarket.data.content.AppContent.AppItem;

/**
 * Created by Crypton04 on 24.01.2018.
 */

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AppListAdapter";

    public AppListAdapter() {
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
            ((AppListViewHolder) holder).bind(null, position);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
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

        public void bind(AppItem appItem, int position) {
            adapter = new NestedAppListAdapter();
            recyclerViewNested.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewNested.setAdapter(adapter);
        }
    }

//    public class AppListViewHolder extends ViewHolder {
//        private  final View mView;
//        @BindView(R.id.imageView) ImageView mIconView;
//        @BindView(R.id.Price) TextView mPriceView;
//        @BindView(R.id.content) TextView mContentView;
//        private AppItem mItem;
//        private Context context;
//
//        private AppListViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//            context = view.getContext();
//            mView = view;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//
//        public void bind(AppItem appItem, int position) {
//            mItem = mValues.get(position);
//            mIconView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_snapchat));
//            mContentView.setText(mValues.get(position).name);
//            mIconView.setImageBitmap(ImageUtils.getBitmapFromBase64(mValues.get(position).icon));
//            mPriceView.setText(String.valueOf(new EthereumPrice(mValues.get(position).price).getDisplayPrice(false)));
//
//            mView.setOnClickListener(v -> {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, AppDetailActivity.class);
//                intent.putExtra("item", mItem);
//
//                context.startActivity(intent);
//
//            });
//
//        }
//    }
}