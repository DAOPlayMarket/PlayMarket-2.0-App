package com.blockchain.store.playmarket.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.AppDetailsImpl;
import com.blockchain.store.playmarket.views.FadingTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AppDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum ViewTypes {
        MAIN, SCREENSHOTS, REVIEWS, ICO
    }

    ArrayList<AppDetailsImpl> items = new ArrayList<>();
    private AppCompatActivity activity;

    public AppDetailAdapter(ArrayList<AppDetailsImpl> items, AppCompatActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_details_main_item, parent, false);

        View view2 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_details_screenshots_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MainItemViewHolder extends RecyclerView.ViewHolder {
        private static final int DEFAULT_MAX_LINES = 3;
        private static final int LIMIT_MAX_LINES = 150;
        private static final int ANIMATOR_DURATION = 400;

        @BindView(R.id.action_btn) Button actionBtn;
        @BindView(R.id.app_name) TextView appName;
        @BindView(R.id.invest_btn) Button investBtn;
        @BindView(R.id.image_icon) SimpleDraweeView imageIcon;
        @BindView(R.id.delete_view) TextView deleteBtn;
        @BindView(R.id.rating_textView) TextView appRating;
        @BindView(R.id.main_layout_holder) View mainLayoutHolder;
        @BindView(R.id.no_marks_textView) TextView noMarksTextView;
        @BindView(R.id.app_description) FadingTextView appDescription;
        @BindView(R.id.price_progress_bar) ProgressBar priceProgressBar;
        @BindView(R.id.marks_count_textView) TextView marksCountTextView;
        @BindView(R.id.app_description_short) TextView appDescriptionShort;
        @BindView(R.id.rating_materialRatingBar) MaterialRatingBar ratingBar;
        @BindView(R.id.age_restrictions_textView) TextView ageRestrictionsTextView;

        private App app;
        private ObjectAnimator textDescriptionAnimator;

        public MainItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(App app) {
            this.app = app;
            imageIcon.setImageURI(Uri.parse(app.getIconUrl()));
            setViews();
        }

        private void setViews() {
            appName.setText(app.nameApp);

            String ageRestrictions = app.ageRestrictions + " +";
            ageRestrictionsTextView.setText(ageRestrictions);

            if (app.rating == null || app.rating.ratingCount == 0) {
                marksCountTextView.setVisibility(View.GONE);
            } else {
                marksCountTextView.setText(app.rating.ratingCount + " marks");
            }
            if (app.description != null)
                appDescription.setText(Html.fromHtml(app.description));
            if (app.shortDescription != null)
                appDescriptionShort.setText(Html.fromHtml(app.shortDescription));
            if (app.rating != null) {
                noMarksTextView.setVisibility(View.GONE);
                appRating.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                appRating.setText(String.valueOf(app.getRating()));
                ratingBar.setRating(Float.valueOf(String.valueOf(app.getRating())));
            } else {
                noMarksTextView.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.GONE);
            }
            if (app.isIco) {
                investBtn.setVisibility(View.VISIBLE);
            } else {
                investBtn.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.app_description)
        void onDescriptionClicked() {
            if (appDescription.getMaxLines() == DEFAULT_MAX_LINES) {
                textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", LIMIT_MAX_LINES);
                appDescription.setNeedToDrawFading(true);
            } else if (appDescription.getMaxLines() == LIMIT_MAX_LINES) {
                textDescriptionAnimator = ObjectAnimator.ofInt(appDescription, "maxLines", DEFAULT_MAX_LINES);
                appDescription.setNeedToDrawFading(false);
            }

            if (textDescriptionAnimator != null && !textDescriptionAnimator.isStarted()) {
                textDescriptionAnimator.setDuration(ANIMATOR_DURATION).start();
            }

        }

    }

    class ScreenShotsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.screenshots_recycler_view) RecyclerView recyclerView;

        private ImageViewer.Builder imageViewerBuilder;
        private ImageListAdapter imageAdapter;
        private Context context;

        public ScreenShotsViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();

        }

        public void bind(App app) {
            if (app.files != null && app.getImages() != null) {

            }
            imageViewerBuilder = new ImageViewer.Builder(context, app.getImages());
            imageAdapter = new ImageListAdapter(app.getImages(), position -> imageViewerBuilder.setStartPosition(position).show());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(imageAdapter);
        }
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public ReviewsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
