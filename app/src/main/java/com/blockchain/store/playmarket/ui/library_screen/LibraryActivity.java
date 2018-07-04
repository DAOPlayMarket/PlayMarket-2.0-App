package com.blockchain.store.playmarket.ui.library_screen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.SearchListAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.ui.ico_screen.IcoFragment;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuFragment;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LibraryActivity extends AppCompatActivity implements AppListCallbacks {
    private static final String TAG = "LibraryActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    private SearchListAdapter adapter;

    private ArrayList<App> app = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.library_screen_title);
        initViewPager();
//        loadSavedApps();
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new IcoFragment(), getString(R.string.fragment_ico_title));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadSavedApps() {
        if (Hawk.contains(Constants.DOWNLOADED_APPS_LIST)) {
            app = Hawk.get(Constants.DOWNLOADED_APPS_LIST);
        }
        populateRecyclerView(app);
    }

    private void populateRecyclerView(ArrayList<App> apps) {
        if (apps.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            adapter = new SearchListAdapter(apps, this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onAppClicked(App app) {
        AppDetailActivity.start(this, app);
    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackArrowClicked() {
        this.finish();
    }
}
