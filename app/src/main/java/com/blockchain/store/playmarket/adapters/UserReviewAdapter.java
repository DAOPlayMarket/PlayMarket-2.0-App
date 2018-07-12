package com.blockchain.store.playmarket.adapters;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.SortedUserReview;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.utilities.Blockies;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class UserReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "UserReviewAdapter";
    private static final int TYPE_USER_REVIEW = 0;
    private static final int TYPE_REVIEW_FIELD = 1;
    ArrayList<UserReview> userReviews;

    public UserReviewAdapter(ArrayList<UserReview> userReviews) {
        this.userReviews = userReviews;
    }

    @Override
    public int getItemViewType(int position) {
        if ()
            return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_item, parent, false);
        return new UserReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserReviewViewHolder) {
            ((UserReviewViewHolder) holder).bind(userReviews.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        return userReviews.size() + 1;
    }

    class UserReplyViewHolder extends RecyclerView.ViewHolder {

        public UserReplyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class UserReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_icon) ImageView userIcon;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_commentary) TextView userCommentary;
        @BindView(R.id.read_more) TextView readMore;
        @BindView(R.id.reply) TextView reply;
        @BindView(R.id.left_divider) View leftDivider;

        @BindView(R.id.rating_bar) MaterialRatingBar materialRatingBar;
        private ObjectAnimator textDescriptionAnimator;
        private View itemView;

        public UserReviewViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            materialRatingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                    Log.d(TAG, "onRatingChanged() called with: ratingBar = [" + ratingBar + "], rating = [" + rating + "]");
                }
            });
            userCommentary.post(() -> setupViewAfterOnMeasure(userCommentary));
        }

        public void bind(UserReview userReview, int position) {
//            userCommentary.setText(userReview.description);
            userIcon.setImageBitmap(Blockies.createIcon(userReview.vote));
            userName.setText(userReview.voter);

            reply.setVisibility(userReview.isReviewOnReview ? View.INVISIBLE : View.VISIBLE);
            leftDivider.setVisibility(userReview.isReviewOnReview ? View.VISIBLE : View.GONE);

        }

        private void setupViewAfterOnMeasure(TextView userCommentary) {
            Layout userCommentaryLayout = userCommentary.getLayout();
            if (userCommentaryLayout != null) {
                int lineCount = userCommentaryLayout.getLineCount();
                int ellipsisCount = userCommentaryLayout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    readMore.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "UserReviewViewHolder: ellipsisCount " + ellipsisCount);
            }
            readMore.setOnClickListener(v -> setReadMoreLogic(userCommentary));
        }

        private void setReadMoreLogic(TextView userCommentary) {
            if (userCommentary.getMaxLines() == 2) {
                textDescriptionAnimator = ObjectAnimator.ofInt(userCommentary, "maxLines", 10);
                readMore.setText(itemView.getContext().getString(R.string.read_less));
            } else if (userCommentary.getMaxLines() == 10) {
                textDescriptionAnimator = ObjectAnimator.ofInt(userCommentary, "maxLines", 2);
                readMore.setText(itemView.getContext().getString(R.string.read_more));
            }

            if (textDescriptionAnimator != null && !textDescriptionAnimator.isStarted()) {
                textDescriptionAnimator.setDuration(Constants.USER_REVIEW_EXPAND_ANIMATION_MILLIS).start();
            }
        }


    }
}
