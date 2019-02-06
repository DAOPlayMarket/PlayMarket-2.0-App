package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.blockchain.store.playmarket.utilities.FrescoUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Crypton04 on 05.02.2018.
 */

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ImageListAdapter";

    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<Integer> imageIds;
    private ImageListAdapterCallback callback;

    public ImageListAdapter(ArrayList<String> imagePaths, ImageListAdapterCallback callback) {
        this.callback = callback;
        this.imagePaths = imagePaths;
    }

    public ImageListAdapter(ImageListAdapterCallback callback, ArrayList<Integer> imagePaths) {
        this.callback = callback;
        this.imageIds = imagePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_adapter_item, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (imageIds != null) {
            ((ImageViewHolder) holder).bind(null, position);
        } else {
            ((ImageViewHolder) holder).bind(imagePaths.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        if (imageIds != null) return imageIds.size();
        return imagePaths.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycled() called with: holder = [" + holder + "]");
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;
        private Context context;

        ImageViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(String imagePath, int position) {

//            ImageRequest request = ImageRequestBuilder
//                    .newBuilderWithSource(uri)
//                    .disableDiskCache()
//                    .build();
            if (imageIds != null) {
                Integer imageId = imageIds.get(position);
                imageView.setImageResource(imageId);
            } else {
                imageView.setImageURI(Uri.parse(imagePath));
            }
            imageView.setOnClickListener(v -> callback.onImageGalleryItemClicked(position));
        }
    }
}
