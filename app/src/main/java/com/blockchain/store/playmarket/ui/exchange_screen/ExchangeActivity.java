package com.blockchain.store.playmarket.ui.exchange_screen;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;
import com.blockchain.store.playmarket.ui.exchange_screen.exchange_confirm_fragment.ExchangeConfirmFragment;
import com.blockchain.store.playmarket.ui.exchange_screen.exchange_info_fragment.ExchangeInfoFragment;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends AppCompatActivity implements ExchangeActivityContracts.View, LifecycleOwner {

    private static final String TAG = "ExchangeActivity";
    private static final String DIALOG_TAG = "dialog_tag";
    private static final int DEBOUNCE_INTERVAL_MILLIS = 1000;

    @BindView(R.id.view_pager) NonSwipeableViewPager viewPager;
    @BindView(R.id.exchange_cancel) Button exchangeCancel;
    @BindView(R.id.exchange_continue) Button exchangeContinue;
    @BindView(R.id.progress_holder) LinearLayout progressHolder;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.layout_holder) RelativeLayout layoutHolder;

    private ExchangeActivityViewModel exchangeActivityViewModel;
    private ExchangeActivityPresenter presenter;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        exchangeActivityViewModel = ViewModelProviders.of(this).get(ExchangeActivityViewModel.class);
        attachPresenter();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() called with: position = [" + position + "]");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void attachPresenter() {
        presenter = new ExchangeActivityPresenter();
        presenter.init(this);
        presenter.loadCurrencies();
    }


    @OnClick(R.id.exchange_cancel)
    public void exchange_cancel() {
        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0, true);
        } else {
            this.finish();
        }
    }

    @OnClick(R.id.exchange_continue)
    public void exchange_continue() {
        if (viewPager.getCurrentItem() == 0) {
            /*create transaction*/
            viewPager.setCurrentItem(1, true);
        } else {
            this.finish();
        }

    }

    @OnClick(R.id.error_view_repeat_btn)
    public void onErrorViewRepeatClicked() {
        presenter.loadCurrencies();
    }


    @Override
    public void showLoadCurrenciesProgress(boolean isShown) {
        progressHolder.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoadCurrenciesReady(ArrayList<ChangellyCurrency> changellyCurrencies, ChangellyMinimumAmountResponse minimumAmount) {
        errorHolder.setVisibility(View.GONE);
        layoutHolder.setVisibility(View.VISIBLE);
        initViewPager(changellyCurrencies, minimumAmount);
    }

    @Override
    public void onLoadCurrenciesFailed(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
        errorHolder.setVisibility(View.VISIBLE);
        layoutHolder.setVisibility(View.GONE);
    }


    private void initViewPager(ArrayList<ChangellyCurrency> changellyCurrencies, ChangellyMinimumAmountResponse minimumAmount) {
        exchangeActivityViewModel.changellyCurrencies.setValue(changellyCurrencies);
        exchangeActivityViewModel.minEnteredAmount.setValue(minimumAmount.result);
        if (changellyCurrencies.size() > 0) {
            exchangeActivityViewModel.chosenCurrency.setValue(changellyCurrencies.get(0));
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new ExchangeInfoFragment(), "");
            viewPagerAdapter.addFragment(new ExchangeConfirmFragment(), "");
            viewPager.setAdapter(viewPagerAdapter);
        } else {
            //todo show error
        }

    }
}
