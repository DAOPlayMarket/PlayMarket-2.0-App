package com.blockchain.store.playmarket.ui.local_ico_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcoStepFragment extends Fragment {


    public IcoStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico_step, container, false);
        return view;
    }

}
