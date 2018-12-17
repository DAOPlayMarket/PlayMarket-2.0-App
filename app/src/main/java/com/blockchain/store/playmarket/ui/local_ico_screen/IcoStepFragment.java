package com.blockchain.store.playmarket.ui.local_ico_screen;


import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IcoStepFragment extends Fragment {

    @BindView(R.id.earned_tv) TextView earned_tv;
    @BindView(R.id.token_price_tv) TextView token_price_tv;
    @BindView(R.id.textView8) TextView textView8;
    @BindView(R.id.ico_sold_tokens_holder) LinearLayout ico_sold_tokens_holder;

    private static final String TAG = "IcoStepFragment";
    private static final String TIME_KEY = "timeInMillis";

    @BindView(R.id.earned_eth) TextView earnedEth;
    @BindView(R.id.token_price) TextView tokenPrice;
    @BindView(R.id.ico_day_left) TextView dayLeft;
    @BindView(R.id.ico_hour_left) TextView hourLeft;
    @BindView(R.id.ico_minutes_left) TextView minutesLeft;
    @BindView(R.id.ico_day_tv) TextView dayTv;
    @BindView(R.id.ico_hour_tv) TextView hourTv;
    @BindView(R.id.ico_minutes_tv) TextView minutesTv;
    @BindView(R.id.ico_sell_count_max) TextView tokensCount;
    @BindView(R.id.ico_sell_count) TextView tokensSold;
    @BindView(R.id.ico_purchase_token_group) Group purchaseTokenGroup;
    @BindView(R.id.ico_actual_price) TextView actualPriceTv;
    @BindView(R.id.ico_purchase_button) Button purchaseButton;

    private CountDownTimer countDownTimer;
    private long timeToStartInMillis;

    public static IcoStepFragment newInstance(Long timeToStartInMillis) {
        Bundle args = new Bundle();
        args.putLong(TIME_KEY, timeToStartInMillis);
        IcoStepFragment fragment = new IcoStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico_step, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null)
            timeToStartInMillis = getArguments().getLong(TIME_KEY);
        purchaseButton.setEnabled(isStageIsActive());
        tokensCount.setText(isStageIsActive() ? "/1000" : "/0");
        tokensSold.setText(isStageIsActive() ? "681" : "0");
        actualPriceTv.setText(isStageIsActive() ? R.string.ico_actual_price : R.string.ico_time_to_start);
        initTimer();
        return view;

    }

    private boolean isStageIsActive() {
        return timeToStartInMillis < System.currentTimeMillis();
    }

    private void initTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(timeToStartInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(millisUntilFinished);
                    dayLeft.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                    hourLeft.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
                    minutesLeft.setText(String.valueOf(cal.get(Calendar.MINUTE)));

                    Resources resources = getActivity().getBaseContext().getResources();

                    dayTv.setText(resources.getQuantityString(R.plurals.days, cal.get(Calendar.DAY_OF_MONTH)));
                    hourTv.setText(resources.getQuantityString(R.plurals.hours, cal.get(Calendar.HOUR_OF_DAY)));
                    minutesTv.setText(resources.getQuantityString(R.plurals.minutes, cal.get(Calendar.MINUTE)));

                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
    }

    @OnClick(R.id.ico_purchase_button)
    public void ico_purchase_button() {
        // TODO
    }

}
