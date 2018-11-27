package com.blockchain.store.playmarket.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// Класс адаптера для списка "JsonKeystoreFileList".
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private ArrayList<File> usersArrayList;


    private int selectedItemIndex = 0;

    public UserListAdapter(ArrayList<File> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileUtils fileUtils = new FileUtils();
        File file = usersArrayList.get(position);
        holder.accountName.setText("0x" + fileUtils.readJsonKeystoreFile(file, "address"));

        holder.jsonKeystoreLayout.setOnClickListener(v -> {
            selectedItemIndex = position;
            notifyDataSetChanged();
        });

        if (selectedItemIndex == position) holder.checkJsonKeystore.setChecked(true);
        else holder.checkJsonKeystore.setChecked(false);
    }

    // Метод получения позиции выбранного элемента в "JsonKeystoreFileList".
    public int getSelectedItem() {
        return selectedItemIndex;
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.json_keystore_layout)
        LinearLayout jsonKeystoreLayout;
        @BindView(R.id.json_keystore_file_textView)
        TextView accountName;
        @BindView(R.id.json_keystore_check_radioButton)
        RadioButton checkJsonKeystore;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
