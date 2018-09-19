package com.blockchain.store.playmarket.ui.transaction_history_screen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TransactionHistoryAdapter;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.blockchain.store.playmarket.views.FonAwesomeTextViewSolid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionHistoryActivity extends AppCompatActivity {

    private static final String TAG = "TransactionHistoryActiv";

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.title) TextView title;
    TransactionHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);
        setViews();
        initViewPager();
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(TransactionHistoryFragment.instance(true), "History");
        viewPagerAdapter.addFragment(TransactionHistoryFragment.instance(Constants.TransactionStatus.PENDING), "Pending");
        viewPagerAdapter.addFragment(TransactionHistoryFragment.instance(Constants.TransactionStatus.SUCCEES), "Succeed");
        viewPagerAdapter.addFragment(TransactionHistoryFragment.instance(Constants.TransactionStatus.FAILED), "Failed");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViews() {
        title.setText(R.string.transaction_history_title);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.onBackPressed();
    }


}
