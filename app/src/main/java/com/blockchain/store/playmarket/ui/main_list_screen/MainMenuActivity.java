package com.blockchain.store.playmarket.ui.main_list_screen;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.dao.ui.dividends_screen.DividendsFragment;
import com.blockchain.store.dao.ui.votes_screen.ProposalDetailsFragment;
import com.blockchain.store.dao.ui.votes_screen.main_votes_screen.MainVotesFragment;
import com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen.ProposalCreationFragment;
import com.blockchain.store.dao.ui.votes_screen.voting_screen.VotingFragment;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.broadcasts.InstallPackageReceiver;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.ReferralData;
import com.blockchain.store.playmarket.interfaces.AppListCallbacks;
import com.blockchain.store.playmarket.interfaces.BackPressedCallback;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.ui.app_detail_screen.AppDetailActivity;
import com.blockchain.store.playmarket.ui.change_account_screen.ChangeAccountFragment;
import com.blockchain.store.playmarket.ui.ico_screen.IcoFragment;
import com.blockchain.store.playmarket.ui.login_screen.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.my_apps_screen.MyAppsActivity;
import com.blockchain.store.playmarket.ui.navigation_view.NavigationViewFragment;
import com.blockchain.store.playmarket.ui.pex_screen.DappActivity;
import com.blockchain.store.playmarket.ui.pex_screen.DappsFragment;
import com.blockchain.store.playmarket.ui.pex_screen.PexActivity;
import com.blockchain.store.playmarket.ui.search_screen.SearchActivity;
import com.blockchain.store.playmarket.ui.token_transfer_screen.TokenTransferFragment;
import com.blockchain.store.playmarket.ui.tokens_screen.TokenListFragment;
import com.blockchain.store.playmarket.ui.wallet_screen.WalletFragment;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ReferralManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.blockchain.store.playmarket.utilities.drawable.HamburgerDrawable;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainMenuContract.Presenter;
import static com.blockchain.store.playmarket.utilities.Constants.OPEN_MY_APPS_EXTRA;

public class MainMenuActivity extends BaseActivity implements AppListCallbacks, MainMenuContract.View, MaterialSearchView.OnQueryTextListener, NavigationCallback {
    private static final String TAG = "MainMenuActivity";
    private static final int DEBOUNCE_INTERVAL_MILLIS = 1000;
    private static final int DOUBLE_TAP_INTERVAL_MILLIS = 2000;

    public static final int CHANGE_ACCOUNT_REQUEST_CODE = 80;
    public static final int IMPORT_ACCOUNT_REQUEST_CODE = 2;

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
    private ViewGroup.LayoutParams appBarLayoutParams;
    ViewPagerAdapter viewPagerAdapter;
    private Presenter presenter;
    private long backPressedLastTime;
    private int tabPosition;
    private int appBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        appBarLayoutParams = appBarLayout.getLayoutParams();
        appBarHeight = appBarLayoutParams.height;
        attachPresenter();
        initViews();
        replaceNavViewFragment(new NavigationViewFragment());
        setSearchViewDebounce();
        initAppInstallReceiver();
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

    private void addNavViewFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.navigation_view_holder, fragment).addToBackStack("").commitAllowingStateLoss();
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

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (isFragmentOverrideBackPressed()) {
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

    private boolean isFragmentOverrideBackPressed() {
        Fragment currentFragmnet = viewPagerAdapter.getItem(tabPosition);
        if (currentFragmnet instanceof BackPressedCallback) {
            ((BackPressedCallback) currentFragmnet).isUserCanHandleBackPressed();
            return true;
        }
        return false;
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
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Category category : categories) {
            String capitalizeCategoryName = category.name.substring(0, 1).toUpperCase() + category.name.substring(1);
            viewPagerAdapter.addFragment(MainMenuFragment.newInstance(category), capitalizeCategoryName);
        }
        viewPagerAdapter.addFragment(new IcoFragment(), getString(R.string.fragment_ico_title));
        viewPagerAdapter.addFragment(new DappsFragment(), getString(R.string.dapps));
        viewPagerAdapter.addFragment(DappsFragment.newInstance(), getString(R.string.fragment_exchange_title));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.apps_icon)).setText(R.string.category_apps);
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.games_icon)).setText(R.string.category_games);
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ico_icon)).setText(R.string.category_sto);
        tabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.drawable.dapps)).setText(R.string.dapps);
        tabLayout.getTabAt(4).setIcon(getResources().getDrawable(R.drawable.icon_exchange)).setText(R.string.fragment_exchange_title);


        removeIconMargin(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i > 2) {
                    appBarLayoutParams.height = 0;
                    appBarLayout.setLayoutParams(appBarLayoutParams);
                } else if (i > 1) {
                    appBarLayoutParams.height = Math.round(appBarHeight - appBarHeight * v);
                    appBarLayout.setLayoutParams(appBarLayoutParams);
                } else {
                    appBarLayoutParams.height = appBarHeight;
                    appBarLayout.setLayoutParams(appBarLayoutParams);
                }
            }

            @Override
            public void onPageSelected(int i) {
                tabPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
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
        ToastUtil.showToast(getString(R.string.load_failed) + throwable.getMessage());
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

    @OnClick(R.id.dapps_tab)
    void onDappsTabClicked() {
        startActivity(new Intent(this, DappActivity.class));
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
        if (newText.isEmpty()) return false;
        userInputSubject.onNext(newText);
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(OPEN_MY_APPS_EXTRA)) {
            startActivity(new Intent(this, MyAppsActivity.class));
        }
        String referralLink = intent.getDataString();
        if (referralLink != null && referralLink.contains("referral")) {
            String packageName = referralLink.substring(referralLink.indexOf("=") + 1, referralLink.indexOf("&"));
            String payload = referralLink.substring(referralLink.lastIndexOf("=") + 1);
            new ReferralManager().addReferralData(new ReferralData(packageName, payload));
            RestApi.getServerApi().getAppsByPackage(packageName)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(this::onRefferalAppListen, this::onReferralNotFound);
            ToastUtil.showToast("Referral data received " + payload);
        }
    }

    private void onRefferalAppListen(ArrayList<App> apps) {
        if (apps.get(0) != null) {
            AppDetailActivity.start(this, apps.get(0));
        }
    }

    private void onReferralNotFound(Throwable throwable) {

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
        addNavViewFragment(new ProposalCreationFragment());
    }

    @Override
    public void onProposalClicked(Proposal proposal) {
        addNavViewFragment(VotingFragment.newInstance(proposal));
    }

    @Override
    public void onProposalDetailsClicked(Proposal proposal) {
        addNavViewFragment(ProposalDetailsFragment.newInstance(proposal));
    }

    @Override
    public void onTokenTransferClicked(DaoToken daoToken) {
        if (daoToken.address.equalsIgnoreCase(DaoConstants.CRYPTO_DUEL_CONTRACT)) {
            replaceNavViewFragment(TokenTransferFragment.openAsCryptoDuelToken(daoToken));
        } else {
            replaceNavViewFragment(TokenTransferFragment.newInstance(daoToken));
        }

    }

    @Override
    public void onChangeAccountClicked() {
        replaceNavViewFragment(new ChangeAccountFragment());
    }

    @Override
    public void onAddTokenClicked() {
        replaceNavViewFragment(new TokenListFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_ACCOUNT_REQUEST_CODE) {

            updateChangeAccountFragment();
        }
        if (requestCode == IMPORT_ACCOUNT_REQUEST_CODE) {
            if (data != null) {
                String jsonData = data.getStringExtra("json_data");
                LoginPromptActivity.startAsImportccount((AppCompatActivity) this, CHANGE_ACCOUNT_REQUEST_CODE, jsonData);
            }
        }
    }

    private void updateChangeAccountFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_view_holder);
        if (fragment != null && fragment instanceof ChangeAccountFragment) {
            ((ChangeAccountFragment) fragment).updateAdapter();
        }
    }

    private void initAppInstallReceiver() {
        BroadcastReceiver br = new InstallPackageReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        this.registerReceiver(br, filter);

        if (Hawk.get(Constants.IPFS_AUTO_START, false)) {
            IPFSDaemon.getInstance().initDaemon();
        }

    }
}
