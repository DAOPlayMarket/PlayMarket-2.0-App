package com.blockchain.store.playmarket.ui.my_apps_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blockchain.store.playmarket.R;

import butterknife.ButterKnife;

public class MyAppsActivity extends AppCompatActivity implements MyAppsContract.View {
    private static final String TAG = "MyAppsActivity";

    private MyAppsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apps);
        ButterKnife.bind(this);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new MyAppsPresenter();
        presenter.init(this);
    }
}
