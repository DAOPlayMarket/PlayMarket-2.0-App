package com.blockchain.store.playmarket.ui.local_ico_screen;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

/**
 * Created by Igor.Sakovich on 08.12.2018.
 */

public class NewIcoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SPLASH = 0;
    private static final int VIEW_TYPE_BUDGET = 1;
    private static final int VIEW_TYPE_DESCRIPTION = 2;
    private static final int VIEW_TYPE_STEP = 3;
    private static final int VIEW_TYPE_GRAPH = 4;

    private AppCompatActivity appCompatActivity;

    public NewIcoAdapter(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        //bindAsCrypoDuelTest
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;


        switch (viewType) {
            case VIEW_TYPE_SPLASH:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_splash, parent, false);
                return new IcoSplashViewHolder(view);
            case VIEW_TYPE_BUDGET:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_budget, parent, false);
                return new IcoBudgetViewHolder(view);
            case VIEW_TYPE_DESCRIPTION:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_description, parent, false);
                return new IcoDescriptionViewHolder(view);
            case VIEW_TYPE_STEP:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_step, parent, false);
                return new IcoStepViewHolder(view);
            case VIEW_TYPE_GRAPH:

        }


        return null;
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return 0;
    }

    public class IcoSplashViewHolder extends RecyclerView.ViewHolder {

        public IcoSplashViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class IcoBudgetViewHolder extends RecyclerView.ViewHolder {

        public IcoBudgetViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class IcoDescriptionViewHolder extends RecyclerView.ViewHolder {

        public IcoDescriptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class IcoStepViewHolder extends RecyclerView.ViewHolder {
        ViewPagerAdapter viewPagerAdapter;

        public IcoStepViewHolder(View itemView) {
            super(itemView);

            viewPagerAdapter = new ViewPagerAdapter(appCompatActivity.getSupportFragmentManager());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            ViewPager viewPager = itemView.findViewById(R.id.ico_viewpager);
            viewPager.setAdapter(viewPagerAdapter);


        }

        public void bind() {

        }
    }
}
