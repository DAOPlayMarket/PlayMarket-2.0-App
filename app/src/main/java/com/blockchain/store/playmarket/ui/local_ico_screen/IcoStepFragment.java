package com.blockchain.store.playmarket.ui.local_ico_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcoStepFragment extends Fragment {
    private static final String TAG = "IcoStepFragment";

    @BindView(R.id.earned_eth) TextView earnedEth;
    @BindView(R.id.token_price) TextView tokenPrice;
    @BindView(R.id.ico_day_left) TextView dayLeft;
    @BindView(R.id.ico_hour_left) TextView hourLeft;
    @BindView(R.id.ico_minutes_left) TextView minutesLeft;


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
