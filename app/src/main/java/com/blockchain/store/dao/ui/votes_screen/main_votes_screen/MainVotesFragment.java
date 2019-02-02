package com.blockchain.store.dao.ui.votes_screen.main_votes_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.ui.votes_screen.VotesFragment;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainVotesFragment extends Fragment implements MainVotesContract.View, SwipeRefreshLayout.OnRefreshListener {

    private NavigationCallback navigationCallback;
    private ViewPagerAdapter viewPagerAdapter;
    private MainVotesPresenter presenter = new MainVotesPresenter();
    public static String BROADCAST_ACTION = "SyncState";

    @BindView(R.id.votes_tabLayout)
    TabLayout votesTabLayout;
    @BindView(R.id.votes_viewPager)
    NonSwipeableViewPager votesViewPager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.addProposal_button)
    Button addProposalButton;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_votes, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar.setVisibility(View.VISIBLE);
        presenter.init(this, getContext());
        presenter.startDaoService();

        votesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (addProposalButton.getVisibility() == View.GONE) showCreationButton();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopDaoService();
        presenter.unregisterBroadcastReceiver();
    }

    @Override
    public void initTabLayout(Pair<ArrayList<Proposal>, ArrayList<Proposal>> proposalsPair) {
        if (viewPagerAdapter != null) {
            ((VotesFragment) viewPagerAdapter.getItem(0)).showProposals(proposalsPair.first);
            ((VotesFragment) viewPagerAdapter.getItem(1)).showProposals(proposalsPair.second);
            return;
        }
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(VotesFragment.newInstance(proposalsPair.first));
        viewPagerAdapter.addFragment(VotesFragment.newInstance(proposalsPair.second));

        votesViewPager.setAdapter(viewPagerAdapter);
        votesViewPager.setOffscreenPageLimit(1);

        votesTabLayout.setupWithViewPager(votesViewPager);
        votesTabLayout.getTabAt(0).setText("Ongoing");
        votesTabLayout.getTabAt(1).setText("Archive");
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.addProposal_button)
    void onNewProposalClicked() {
        navigationCallback.onNewProposalClicked();
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    private int position = 0;
    public void onScroll(int dy) {
        if (dy >= 5) {
            if (addProposalButton.getVisibility() != View.GONE) hideCreationButton();
        } else if (dy <= -5) {
            if (addProposalButton.getVisibility() != View.VISIBLE) showCreationButton();
        } else {
            position += dy;
            if (position > 30) {
                position = 0;
                if (addProposalButton.getVisibility() != View.GONE) hideCreationButton();
            } else if (position < -20) {
                position = 0;
                if (addProposalButton.getVisibility() != View.VISIBLE) showCreationButton();
            }
        }
    }

    private void hideCreationButton() {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, 250);
        animate.setDuration(200);
        addProposalButton.startAnimation(animate);
        addProposalButton.setVisibility(View.GONE);
    }

    private void showCreationButton() {
        addProposalButton.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0, 0, 250, 0);
        animate.setDuration(200);
        addProposalButton.startAnimation(animate);
    }

    @Override
    public void onRefresh() {
        viewPagerAdapter.getItem(0).onDestroy();
        viewPagerAdapter.getItem(1).onDestroy();
        progressBar.setVisibility(View.VISIBLE);
        presenter.startDaoService();
    }
}
