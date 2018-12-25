package com.blockchain.store.playmarket.ui.local_ico_screen;


import android.content.Intent;
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
import com.blockchain.store.playmarket.data.entities.IcoLocalData;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.MAX_VALUE_ARG;
import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.MIN_VALUE_ARG;
import static com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity.RECIPIENT_ARG;

public class IcoStepFragment extends Fragment {

    private static final String TAG = "IcoStepFragment";
    private static final String ICO_DATA_KEY = "ico_data_key";
    private static final String POSITION_KEY = "position_key";

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
    @BindView(R.id.earned_tv) TextView earned_tv;
    @BindView(R.id.token_price_tv) TextView token_price_tv;
    @BindView(R.id.textView8) TextView textView8;
    @BindView(R.id.ico_sold_tokens_holder) LinearLayout ico_sold_tokens_holder;

    private CountDownTimer countDownTimer;
    private int position;
    private IcoLocalData icoLocalData;
    private long timeInMillis;

    public static Fragment newInstance(IcoLocalData icoLocalData, int position) {
        Bundle args = new Bundle();
        args.putParcelable(ICO_DATA_KEY, icoLocalData);
        args.putInt(POSITION_KEY, position);
        IcoStepFragment fragment = new IcoStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico_step, container, false);
        ButterKnife.bind(this, view);

        icoLocalData = getArguments().getParcelable(ICO_DATA_KEY);
        position = getArguments().getInt(POSITION_KEY);

        timeInMillis = icoLocalData.getTimeToStartStage(position);

        earnedEth.setText(String.valueOf(icoLocalData.getEarnedInPeriod(position)));

        tokensSold.setText(String.valueOf(icoLocalData.getTokensEarnedInPeriod(position)));
        tokensCount.setText("/" + String.valueOf(icoLocalData.getTokensInPeriod()));

        if (isStageIsActive()) {
            actualPriceTv.setText(R.string.ico_actual_price);
        } else if (position < icoLocalData.getCurrentPeriod()) {
            actualPriceTv.setText(R.string.ico_company_time_from_ends);
        } else {
            actualPriceTv.setText(R.string.ico_time_to_start);
        }

        tokenPrice.setText(String.format("%.8f", icoLocalData.getPrice(position)));

        purchaseButton.setEnabled(isStageIsActive() || icoLocalData.getEarnedInPeriod(position) == icoLocalData.getTokensInPeriod());
        initTimer();
        return view;

    }

    private boolean isStageIsActive() {
        return icoLocalData.getCurrentPeriod() == position;
    }

    private void initTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(System.currentTimeMillis()+15000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(millisUntilFinished);

                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);

                    dayLeft.setText(String.valueOf(days));
                    hourLeft.setText(String.valueOf(hours));
                    minutesLeft.setText(String.valueOf(minutes));

                    Resources resources = getActivity().getBaseContext().getResources();

                    dayTv.setText(resources.getQuantityString(R.plurals.days, cal.get(Calendar.DAY_OF_MONTH)));
                    hourTv.setText(resources.getQuantityString(R.plurals.hours, cal.get(Calendar.HOUR_OF_DAY)));
                    minutesTv.setText(resources.getQuantityString(R.plurals.minutes, cal.get(Calendar.MINUTE)));

                }

                @Override
                public void onFinish() {
                    try {
                        ((AppDetailActivity) getActivity()).reloadCryptonDataSilently();
                    } catch (Exception e) {
                    }

                }
            }.start();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelTimer();
    }

    public void cancelTimer() {
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
    }

    @OnClick(R.id.ico_purchase_button)
    public void ico_purchase_button() {
        long totalTokensLeft = icoLocalData.getTokensInPeriod() - icoLocalData.getTokensEarnedInPeriod(position);
        long priceOfOneToken = icoLocalData.getPriceAsWei(position);

        Intent intent = new Intent(getActivity(), TransferActivity.class);
        intent.putExtra(RECIPIENT_ARG, Constants.CRYPTO_DUEL_CONTRACT_CROWDSALE);
        intent.putExtra(MAX_VALUE_ARG, priceOfOneToken * totalTokensLeft);
        intent.putExtra(MIN_VALUE_ARG, priceOfOneToken);
        startActivity(intent);
    }

}
