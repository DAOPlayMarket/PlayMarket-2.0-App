package com.blockchain.store.playmarket.ui.main_list_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.ui.dividends_screen.DividendsFragment;
import com.blockchain.store.dao.ui.votes_screen.NewProposalFragment;
import com.blockchain.store.dao.ui.votes_screen.main_votes_screen.MainVotesFragment;
import com.blockchain.store.dao.ui.votes_screen.proposal_details_screen.ProposalDetailsFragment;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.ui.ico_screen.IcoFragment;
import com.blockchain.store.playmarket.ui.my_apps_screen.MyAppsActivity;
import com.blockchain.store.playmarket.ui.navigation_view.NavigationViewFragment;
import com.blockchain.store.playmarket.ui.pex_screen.PexActivity;
import com.blockchain.store.playmarket.ui.search_screen.SearchActivity;
import com.blockchain.store.playmarket.ui.wallet_screen.TokenTransferFragment;
import com.blockchain.store.playmarket.ui.wallet_screen.WalletFragment;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.blockchain.store.playmarket.utilities.drawable.HamburgerDrawable;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainMenuContract.Presenter;
import static com.blockchain.store.playmarket.utilities.Constants.OPEN_MY_APPS_EXTRA;

public class MainMenuActivity extends AppCompatActivity implements AppListCallbacks, MainMenuContract.View, MaterialSearchView.OnQueryTextListener, NavigationCallback {
    private static final String TAG = "MainMenuActivity";
    private static final int DEBOUNCE_INTERVAL_MILLIS = 1000;
    private static final int DOUBLE_TAP_INTERVAL_MILLIS = 2000;
    private int tabPosition;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.error_holder)
    View errorHolder;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;

    private BehaviorSubject<String> userInputSubject = BehaviorSubject.create();
    private ArrayList<App> searchListResult = new ArrayList<>();
    private Presenter presenter;
    private long backPressedLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        attachPresenter();
        initViews();
        replaceNavViewFragment(new NavigationViewFragment());
        setSearchViewDebounce();
    }

    private void attachPresenter() {
        presenter = new MainMenuPresenter();
        presenter.init(this);
        presenter.requestUpdateListener(this);
        presenter.loadCategories();

    }

    private void replaceNavViewFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_view_holder, fragment).addToBackStack("").commitAllowingStateLoss();
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.navigation_view_holder, fragment).addToBackStack("").commitAllowingStateLoss();
    }

    private void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerArrowDrawable(new HamburgerDrawable(this));
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        searchView.setOnItemClickListener((parent, view, position, id) -> {
            String query = (String) parent.getItemAtPosition(position);
            onSuggestionClicked(query);
            searchView.closeSearch();
        });
        searchView.setOnQueryTextListener(this);

    }


    private void setSearchViewDebounce() {
        userInputSubject.debounce(DEBOUNCE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(text -> !text.isEmpty())
                .subscribe(text -> {
                    presenter.searchQuery(text);
                });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_view_holder);

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (backPressedLastTime + DOUBLE_TAP_INTERVAL_MILLIS > System.currentTimeMillis()) {
                this.finish();
            } else {
                backPressedLastTime = System.currentTimeMillis();
                ToastUtil.showToast(R.string.double_tap_msg);
            }
        }

    }


    @OnClick(R.id.toolbar_title_holder)
    void onToolbarTitleClicked() {
        if (!searchView.isSearchOpen()) {
            searchView.showSearch();
        }
    }

    @Override
    public void setProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCategoryLoaded(ArrayList<Category> categories) {
        bottomLayout.setVisibility(View.VISIBLE);
        errorHolder.setVisibility(View.GONE);
        initViewPager(categories);

    }

    private void initViewPager(ArrayList<Category> categories) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Category category : categories) {
            String capitalizeCategoryName = category.name.substring(0, 1).toUpperCase() + category.name.substring(1);
            viewPagerAdapter.addFragment(MainMenuFragment.newInstance(category), capitalizeCategoryName);
        }
        viewPagerAdapter.addFragment(new IcoFragment(), getString(R.string.fragment_ico_title));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.apps_icon));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.games_icon));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ico_icon));
        removeIconMargin(tabLayout);
    }

    private void removeIconMargin(TabLayout tabLayout) {
        for (int tabCount = 0; tabCount < tabLayout.getTabCount(); tabCount++) {
            for (int firstChildIterator = 0; firstChildIterator < tabLayout.getChildCount(); firstChildIterator++) {
                int childCount = ((ViewGroup) tabLayout.getChildAt(firstChildIterator)).getChildCount();
                for (int secondChildIterator = 0; secondChildIterator < childCount; secondChildIterator++) {
                    int childCount1 = ((ViewGroup) ((ViewGroup) tabLayout.getChildAt(firstChildIterator)).getChildAt(secondChildIterator)).getChildCount();
                    for (int thirdChildIterator = 0; thirdChildIterator < childCount1; thirdChildIterator++) {
                        View childAt = ((ViewGroup) ((ViewGroup) tabLayout.getChildAt(firstChildIterator)).getChildAt(secondChildIterator)).getChildAt(thirdChildIterator);
                        if (childAt instanceof AppCompatImageView) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                            layoutParams.setMargins(0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCategoryLoadFailed(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
        ToastUtil.showToast("Category load failed! " + throwable.getMessage());
    }

    @Override
    public void onSearchResultReady(ArrayList<App> apps) {
        searchListResult = apps;
    }

    @Override
    public void onSearchResultFail(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }

    @Override
    public void onSearchSuggestionReady(String[] strings) {
    }

    @OnClick(R.id.exchange_tab)
    void onExchangeTabClicked() {
        startActivity(new Intent(this, PexActivity.class));
    }

    @OnClick(R.id.error_view_repeat_btn)
    void onRepeatBtnClicked() {
        errorHolder.setVisibility(View.GONE);
        presenter.loadCategories();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ToastUtil.showToast(item.getTitle().toString());
        return super.onOptionsItemSelected(item);
    }

    private void showCopiedAlert() {
        ToastUtil.showToast(R.string.address_copied);
    }

    @Override
    public void onAppClicked(App app) {
        AppDetailActivity.start(this, app);
    }

    @Override
    public void onAppClickedWithTransition(App app, View view) {
        AppDetailActivity.start(this, app);
    }

    private void onSuggestionClicked(String query) {
        for (App app : searchListResult) {
            if (app.nameApp.equals(query)) {
                AppDetailActivity.start(this, app);
                return;
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.trim().length() > 0) {
            SearchActivity.start(this, query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty() /*|| searchText.equals(newText)*/) return false;
//        searchText = newText;
        userInputSubject.onNext(newText);
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(OPEN_MY_APPS_EXTRA)) {
            startActivity(new Intent(this, MyAppsActivity.class));
        }
        Log.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application.stopAnalytic();
    }

    @Override
    public void onVotesClicked() {
        replaceNavViewFragment(new MainVotesFragment());
    }

    @Override
    public void onWalletClicked() {
        replaceNavViewFragment(new WalletFragment());
    }

    @Override
    public void onDividendsClicked() {
        replaceNavViewFragment(new DividendsFragment());

    }

    @Override
    public void onNewProposalClicked() {
        addFragment(new NewProposalFragment());
    }

    @Override
    public void onProposalDetailsClicked(Proposal proposal) {
        addFragment(ProposalDetailsFragment.newInstance(proposal));
    }

    @Override
    public void onTokenTransferClicked(DaoToken daoToken) {
        addFragment(TokenTransferFragment.newInstance(daoToken));
    }
}
