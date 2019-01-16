package com.blockchain.store.dao.ui.votes_screen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VotesFragment extends Fragment {

    private NavigationCallback navigationCallback;
    private ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.votes_tabLayout)
    TabLayout votesTabLayout;
    @BindView(R.id.votes_viewPager)
    NonSwipeableViewPager votesViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initTabLayout();
    }

    private void initTabLayout() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new OngoingVotesFragment());
        viewPagerAdapter.addFragment(new ArchiveVotesFragment());

        votesViewPager.setAdapter(viewPagerAdapter);
        votesViewPager.setOffscreenPageLimit(2);

        votesTabLayout.setupWithViewPager(votesViewPager);
        votesTabLayout.getTabAt(0).setText("Ongoing");
        votesTabLayout.getTabAt(1).setText("Archive");
    }

    @OnClick(R.id.addProposal_button)
    void onNewProposalClicked() {
        navigationCallback.onNewProposalClicked();
    }

}
