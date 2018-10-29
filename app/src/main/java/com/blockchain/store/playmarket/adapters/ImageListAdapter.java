package com.blockchain.store.playmarket.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.ImageListAdapterCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        return new ImageViewHolder(view);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String imagePath, int position) {
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
