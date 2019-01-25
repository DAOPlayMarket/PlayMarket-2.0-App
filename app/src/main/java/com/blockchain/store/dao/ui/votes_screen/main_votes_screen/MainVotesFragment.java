package com.blockchain.store.dao.ui.votes_screen.main_votes_screen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainVotesFragment extends Fragment implements MainVotesContract.View {

    private NavigationCallback navigationCallback;
    private ViewPagerAdapter viewPagerAdapter;
    private MainVotesPresenter presenter = new MainVotesPresenter();
    public static String BROADCAST_ACTION = "SyncState";

    @BindView(R.id.votes_tabLayout) TabLayout votesTabLayout;
    @BindView(R.id.votes_viewPager) NonSwipeableViewPager votesViewPager;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.addProposal_button) Button addProposalButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        presenter.init(this, getContext());
        presenter.startDaoService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopDaoService();
        presenter.unregisterBroadcastReceiver();
    }

    @Override
    public void initTabLayout(Pair<ArrayList<Proposal>, ArrayList<Proposal>> proposalsPair) {
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
    }

    @OnClick(R.id.addProposal_button)
    void onNewProposalClicked() {
        navigationCallback.onNewProposalClicked();
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    public void setButtonVisibility(int visibility) {
        addProposalButton.setVisibility(visibility);
    }

    public void onScroll(int dy) {
        if (dy >= 3) {
            if (addProposalButton.getVisibility() != View.GONE) {
                TranslateAnimation animate = new TranslateAnimation(0, 0, 0, 250);
                animate.setDuration(200);
                animate.setFillAfter(true);
                addProposalButton.startAnimation(animate);
                setButtonVisibility(View.GONE);
            }
        } else if (dy <= -3) {
            if (addProposalButton.getVisibility() != View.VISIBLE) {
                setButtonVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(0, 0, 250, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                addProposalButton.startAnimation(animate);
            }
        }
    }
}
