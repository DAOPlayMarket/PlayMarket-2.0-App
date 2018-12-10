package com.blockchain.store.playmarket.ui.local_ico_screen;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IcoStepFragment extends Fragment {

    private static final String TAG = "IcoStepFragment";

    @BindView(R.id.earned_eth) TextView earnedEth;
    @BindView(R.id.token_price) TextView tokenPrice;
    @BindView(R.id.ico_day_left) TextView dayLeft;
    @BindView(R.id.ico_hour_left) TextView hourLeft;
    @BindView(R.id.ico_minutes_left) TextView minutesLeft;
    @BindView(R.id.ico_day_tv) TextView dayTv;
    @BindView(R.id.ico_hour_tv) TextView hourTv;
    @BindView(R.id.ico_minutes_tv) TextView minutesTv;

    private CountDownTimer countDownTimer;

    public IcoStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico_step, container, false);
        ButterKnife.bind(this, view);
        initTimer();
        return view;

    }

    private void initTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(2505600000L + 39600000 + 1320000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(millisUntilFinished);
                    dayLeft.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                    hourLeft.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
                    minutesLeft.setText(String.valueOf(cal.get(Calendar.MINUTE)));

                    Date date = new Date(millisUntilFinished);
                    
                    dayTv.setText("");
                    hourTv.setText("");
                    minutesTv.setText("");

                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
    }

}
