package com.blockchain.store.playmarket.ui.local_ico_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IcoLocalActivity extends BaseActivity {
    private static final String TAG = "IcoLocalActivity";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private NewIcoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ico_local);
        ButterKnife.bind(this);
        adapter = new NewIcoAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}
