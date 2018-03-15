package com.blockchain.store.playmarket.ui.exchange_screen;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;
import com.blockchain.store.playmarket.ui.exchange_screen.exchange_confirm_fragment.ExchangeConfirmFragment;
import com.blockchain.store.playmarket.ui.exchange_screen.exchange_info_fragment.ExchangeInfoFragment;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;

import java.math.BigInteger;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends AppCompatActivity implements ExchangeActivityContracts.View, LifecycleOwner {
    private static final String TAG = "ExchangeActivity";

    @BindView(R.id.view_pager) NonSwipeableViewPager viewPager;
    @BindView(R.id.exchange_cancel) Button exchangeCancel;
    @BindView(R.id.exchange_continue) Button exchangeContinue;
    @BindView(R.id.progress_holder) LinearLayout progressHolder;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.layout_holder) RelativeLayout layoutHolder;
    @BindView(R.id.step_field) TextView stepField;

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
                // use this
                if (position == 0 || position == 1) {
                    stepField.setText(String.format(getString(R.string.exchange_step_field), position + 1));
                }
                Log.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
            }

            @Override
            public void onPageSelected(int position) {
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
        stepField.setText(String.format(getString(R.string.exchange_step_field), 1));
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
            if (isCanCreateTransaction()) {
                createTransaction();
            }
        } else {
            this.finish();
        }

    }

    private void createTransaction() {
        String accountHex = AccountManager.getAddress().getHex();
        String currencyFrom = exchangeActivityViewModel.chosenCurrency.getValue().name;
        String amount = exchangeActivityViewModel.userEnteredAmount.getValue();
        presenter.createTransaction(currencyFrom, accountHex, amount);
    }

    private boolean isCanCreateTransaction() {
        ExchangeInfoFragment exchangeInfoFragment = (ExchangeInfoFragment) viewPagerAdapter.getItem(0);
        if (exchangeInfoFragment != null) {
            return exchangeInfoFragment.isCanCreateTransaction();
        }
        return false;
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

    @Override
    public void onTransactionCreatedSuccessfully(ChangellyCreateTransactionResponse changellyCreateTransactionResponse) {
        exchangeActivityViewModel.payinAddress.setValue(changellyCreateTransactionResponse.result.payinAddress);
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onTransactionCreatedFailed(Throwable throwable) {

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
