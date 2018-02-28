package com.blockchain.store.playmarket.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileManagerRecyclerViewAdapter extends RecyclerView.Adapter<FileManagerRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "FileManagerRecyclerView";

    private ArrayList<File> fileList;
    private String selectedPath;
    private int selectedItemIndex = -1;
    FileManagerCallback fileManagerCallback;

    public FileManagerRecyclerViewAdapter(ArrayList<File> fileList, FileManagerCallback fileManagerCallback){
        this.fileManagerCallback = fileManagerCallback;
        this.fileList = fileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_manager_item, parent, false);
        return new FileManagerRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = fileList.get(position);
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + file.getName() + "], position = [" + position + "]");
        holder.folderTextView.setText(file.getName());

        if (fileList.get(position).isDirectory()) holder.nextFolderImageView.setVisibility(View.VISIBLE);
        else holder.nextFolderImageView.setVisibility(View.INVISIBLE);

        holder.itemLinearLayout.setOnClickListener(v -> {
            if (fileList.get(position).isDirectory()) {
                selectedPath = file.getPath();
                notifyDataSetChanged();
                fileManagerCallback.onClick();
            }
            else{
                selectedItemIndex = position;
                notifyDataSetChanged();
            }
        });

        if (selectedItemIndex == position){
            holder.itemLinearLayout.setBackgroundResource(R.color.colorSelectItem);
        }
        else{
            holder.itemLinearLayout.setBackgroundResource(R.color.Clear);
        }

    }

    @Override
    public long getItemId(int position) {
        return fileList.get(position).hashCode();
    }

    public String getSelectedPath(){
        return selectedPath;
    }

    public int getSelectedItemIndex(){
        return selectedItemIndex;
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.folder_text_view)
        TextView folderTextView;

        @BindView(R.id.next_folder_imageView)
        ImageView nextFolderImageView;

        @BindView(R.id.item_linearLayout)
        LinearLayout itemLinearLayout;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FileManagerCallback{
        void onClick();
    }
}
