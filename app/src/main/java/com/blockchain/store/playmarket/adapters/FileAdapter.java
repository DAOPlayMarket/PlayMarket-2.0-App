package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Crypton07 on 18.02.2018.
 */

public class FileAdapter extends ArrayAdapter<File> {

    public FileAdapter(Context context, ArrayList<File> files) {
        super(context, android.R.layout.simple_list_item_1, files);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        File file = getItem(position);
        view.setText(file.getName());
        return view;
    }
}
