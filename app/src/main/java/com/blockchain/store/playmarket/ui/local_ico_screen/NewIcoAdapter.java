package com.blockchain.store.playmarket.ui.local_ico_screen;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override public int getItemViewType(int position) {
        return position;
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
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_graph, parent, false);
                return new IcoGraphViewHolder(view);
        }


        return null;
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return 5;
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

    public class IcoGraphViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lineChart) LineChart lineChart;
        @BindView(R.id.graph_switcher) SwitchCompat switcher;

        public IcoGraphViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switcher.setOnCheckedChangeListener((compoundButton, b) -> setupChart());
            setupChart();

        }

        private void setupChart() {
            ArrayList<Entry> data = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                data.add(new Entry(i, i + new Random().nextInt(10)));
            }
            LineDataSet dataSet = new LineDataSet(data, "String label");
            dataSet.setGradientColor(
                    appCompatActivity.getResources().getColor(R.color.graph_color_gradient_start_color),
                    appCompatActivity.getResources().getColor(R.color.graph_color_gradient_end_color));

            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            LineData lineData = new LineData(dataSet);


            lineChart.setDrawGridBackground(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setTextColor(appCompatActivity.getResources().getColor(R.color.ico_chart_xaxis_color));
            lineChart.getAxisLeft().setTextColor(appCompatActivity.getResources().getColor(R.color.ico_chart_yaxis_color));

            Description description = new Description();
            description.setText("");
            lineChart.setClickable(false);
            lineChart.setDescription(description);
            lineChart.setData(lineData);
            lineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.animateX(500);
            lineChart.animateY(500);
            lineChart.invalidate();
        }


    }

    public class IcoStepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.arrowLeft) ImageView arrowLeft;
        @BindView(R.id.arrowRight) ImageView arrowRight;
        @BindView(R.id.step_counter) TextView setCounter;
        @BindView(R.id.ico_viewpager) ViewPager viewpager;
        ViewPagerAdapter viewPagerAdapter;

        public IcoStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewPagerAdapter = new ViewPagerAdapter(appCompatActivity.getSupportFragmentManager());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            viewPagerAdapter.addFragment(new IcoStepFragment());
            ViewPager viewPager = itemView.findViewById(R.id.ico_viewpager);
            viewPager.setAdapter(viewPagerAdapter);

            setCounter.setText(String.format(itemView.getContext().getString(R.string.step_counter), 1, viewPagerAdapter.getCount()));

            viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override public void onPageSelected(int position) {
                    arrowLeft.setAlpha(position == 0 ? 0.2f : 1f);
                    arrowRight.setAlpha(position == viewPagerAdapter.getCount() - 1 ? 0.2f : 1f);
                    setCounter.setText(String.format(itemView.getContext().getString(R.string.step_counter), position + 1, viewPagerAdapter.getCount()));
                    super.onPageSelected(position);
                }
            });


        }

        @OnClick(R.id.arrowLeft) void onLeftArrowClicked() {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1, true);
        }

        @OnClick(R.id.arrowRight) void onRightArrowClicked() {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
        }

        public void bind() {

        }
    }
}
