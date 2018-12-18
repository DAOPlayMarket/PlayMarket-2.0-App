package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppReviewsData;
import com.blockchain.store.playmarket.interfaces.AppDetailsImpl;
import com.blockchain.store.playmarket.ui.local_ico_screen.IcoStepFragment;
import com.blockchain.store.playmarket.ui.local_ico_screen.NewIcoAdapter;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum ViewTypes {
        MAIN,
        SCREENSHOTS,
        REVIEWS,
        VIEW_TYPE_BUDGET,
        VIEW_TYPE_DESCRIPTION,
        VIEW_TYPE_STEP,
        VIEW_TYPE_TOKEN_DESCRIPTION,
        VIEW_TYPE_GRAPH, ABOUT
    }

    private static final int VIEW_TYPE_BUDGET = 3;
    private static final int VIEW_TYPE_DESCRIPTION = 4;
    private static final int VIEW_TYPE_STEP = 5;
    private static final int VIEW_TYPE_TOKEN_DESCRIPTION = 6;
    private static final int VIEW_TYPE_GRAPH = 7;
    private static final int VIEW_TYPE_ABOUT = 8;

    ArrayList<AppDetailsImpl> items;
    private AppCompatActivity activity;

    public AppDetailAdapter(ArrayList<AppDetailsImpl> items, AppCompatActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    public void addItems(ArrayList<AppDetailsImpl> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_details_main_item, parent, false);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_details_screenshots_item, parent, false);
                return new ScreenShotsViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_details_reviews_item, parent, false);
                return new ReviewsViewHolder(view);

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
            case VIEW_TYPE_TOKEN_DESCRIPTION:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_token_description, parent, false);
                return new IcoDescriptionViewHolder(view);
            case VIEW_TYPE_GRAPH:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_graph, parent, false);
                return new IcoGraphViewHolder(view);
            case VIEW_TYPE_ABOUT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ico_new_about, parent, false);
                return new IcoAboutViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScreenShotsViewHolder) {
            ((ScreenShotsViewHolder) holder).bind((App) items.get(position));
        }
        if (holder instanceof ReviewsViewHolder) {
            ((ReviewsViewHolder) holder).bind((AppReviewsData) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ScreenShotsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.screenshots_recycler_view) RecyclerView recyclerView;

        private ImageViewer.Builder imageViewerBuilder;
        private ImageListAdapter imageAdapter;
        private Context context;

        public ScreenShotsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();

        }

        public void bind(App app) {
            if (app.files != null && app.getImages() != null && imageAdapter == null) {
                imageViewerBuilder = new ImageViewer.Builder(context, app.getImages());
                imageAdapter = new ImageListAdapter(app.getImages(), position -> imageViewerBuilder.setStartPosition(position).show());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(recyclerView);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setNestedScrollingEnabled(true);
                recyclerView.setAdapter(imageAdapter);
            }
        }
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviews_recycler_view) RecyclerView reviewsRecyclerView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AppReviewsData data) {
            UserReviewAdapter userReviewAdapter = new UserReviewAdapter(data.userReviews, (UserReviewAdapter.UserReviewCallback) activity);
            reviewsRecyclerView.setHasFixedSize(true);
            reviewsRecyclerView.setNestedScrollingEnabled(true);
            LinearLayoutManager layout = new LinearLayoutManager(activity);
            reviewsRecyclerView.setLayoutManager(layout);
            reviewsRecyclerView.setAdapter(userReviewAdapter);
        }
    }


    public class IcoSplashViewHolder extends RecyclerView.ViewHolder {

        public IcoSplashViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class IcoBudgetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.budget_details) LinearLayout budgetDetails;
        @BindView(R.id.token_details) LinearLayout tokenDetails;
        @BindView(R.id.advertisement_details) LinearLayout advertisementDetails;
        private Context context;

        public IcoBudgetViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.budget_details, R.id.textview1, R.id.company_budget})
        void onBudgetDetailsClicked() {
            showDialog(context, context.getString(R.string.company_cost), context.getString(R.string.company_budget_details));
        }

        @OnClick({R.id.token_details, R.id.textview2, R.id.token_budget})
        void onTokenDetailsClicked() {
            showDialog(context, context.getString(R.string.token_cost), context.getString(R.string.token_budget_details));
        }

        @OnClick({R.id.advertisement_details, R.id.textview3, R.id.advertisement_budget})
        void onAdvertisementDetailsClicked() {
            showDialog(context, context.getString(R.string.advertisement_budget), context.getString(R.string.adver_budget_details));
        }

        private void showDialog(Context context, String title, String body) {
            AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.alert_dialog_dark))
                    .setTitle(title)
                    .setMessage(body)
                    .setCancelable(true)
                    .create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.show();
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
                    activity.getResources().getColor(R.color.graph_color_gradient_start_color),
                    activity.getResources().getColor(R.color.graph_color_gradient_end_color));

            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            LineData lineData = new LineData(dataSet);


            lineChart.setDrawGridBackground(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setTextColor(activity.getResources().getColor(R.color.ico_chart_xaxis_color));
            lineChart.getAxisLeft().setTextColor(activity.getResources().getColor(R.color.ico_chart_yaxis_color));

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
            viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            viewPagerAdapter.addFragment(IcoStepFragment.newInstance(System.currentTimeMillis() + 2505600000L));
            ViewPager viewPager = itemView.findViewById(R.id.ico_viewpager);
            viewPager.setAdapter(viewPagerAdapter);

            setCounter.setText(String.format(itemView.getContext().getString(R.string.step_counter), 1, viewPagerAdapter.getCount()));

            viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    arrowLeft.setAlpha(position == 0 ? 0.2f : 1f);
                    arrowRight.setAlpha(position == viewPagerAdapter.getCount() - 1 ? 0.2f : 1f);
                    setCounter.setText(String.format(itemView.getContext().getString(R.string.step_counter), position + 1, viewPagerAdapter.getCount()));
                    super.onPageSelected(position);
                }
            });


        }

        @OnClick(R.id.arrowLeft)
        void onLeftArrowClicked() {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1, true);
        }

        @OnClick(R.id.arrowRight)
        void onRightArrowClicked() {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
        }

    }

    public class IcoAboutViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ico_chat_duel_tv) TextView chatDuelTv;
        @BindView(R.id.ico_duel_bot_tv) TextView duelBotTv;
        @BindView(R.id.ico_site_tv) TextView siteTv;
        @BindView(R.id.ico_listing_holder) View icoListingHolder;

        public IcoAboutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            icoListingHolder.setOnClickListener(v -> itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://dex.playmarket.io/CDLT/ETH/"))));
            chatDuelTv.setOnClickListener(v -> {
                itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatDuelTv.getText().toString())));
            });
            duelBotTv.setOnClickListener(v -> {
                itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(duelBotTv.getText().toString())));
            });
            siteTv.setOnClickListener(v -> {
                itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(siteTv.getText().toString())));
            });
        }
    }
}
