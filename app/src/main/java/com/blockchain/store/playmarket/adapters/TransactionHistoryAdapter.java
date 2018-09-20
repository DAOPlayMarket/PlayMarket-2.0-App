package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.transitions.Recolor;
import com.blockchain.store.playmarket.views.FonAwesomeTextViewSolid;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "TransactionHistoryAdapt";

    private ArrayList<TransactionModel> transactionModels;
    private boolean isExpanded = false;
    private RecyclerView recyclerView;


    public TransactionHistoryAdapter(ArrayList<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
    }

    @Override
    public int getItemViewType(int position) {
        return transactionModels.get(position).getTransactionType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_item_default, parent, false);
        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DefaultViewHolder) {
            DefaultViewHolder viewholder = (DefaultViewHolder) holder;
            viewholder.bind(transactionModels.get(position));

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date) TextView date;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.arrow) FonAwesomeTextViewSolid arrow;
        @BindView(R.id.arrow_holder) FrameLayout arrowHolder;
        @BindView(R.id.result) TextView result;
        @BindView(R.id.status) TextView status;
        @BindView(R.id.hash) TextView hash;
        @BindView(R.id.details_etherlink) TextView etherlink;
        @BindView(R.id.right_line) View rightLine;
        @BindView(R.id.detail_holder) LinearLayout detailHolder;

        private Context context;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(TransactionModel model) {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
            date.setText(format.format(new Date(model.timeStamp)));

            result.setText(model.getTransactionFormattedResult());
            status.setText(model.getTransactionType().toString());
            title.setText(model.getFormattedTitle());
            hash.setText(model.transactionHash);

            result.setTextColor(model.isPositive() ? ContextCompat.getColor(context, R.color.positive_value)
                    : ContextCompat.getColor(context, R.color.negative_value));

            switch (model.transactionStatus) {
                case SUCCEES:
                    status.setTextColor(ContextCompat.getColor(context, R.color.positive_value));
                    break;
                case FAILED:
                    status.setTextColor(ContextCompat.getColor(context, R.color.negative_value));
                    break;
                case PENDING:
                    status.setTextColor(ContextCompat.getColor(context, R.color.pending_value));
                    break;
            }

            detailHolder.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            arrow.setRotation(isExpanded ? 270 : 180);
            arrowHolder.setBackground(new ColorDrawable(
                    isExpanded ?
                            ContextCompat.getColor(context, R.color.positive_value)
                            : ContextCompat.getColor(context, R.color.white)));
            arrowHolder.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                TransitionSet transitionSet = new TransitionSet();

                ChangeTransform changeTransform = new ChangeTransform();
                changeTransform.setDuration(200);
                changeTransform.setInterpolator(new AccelerateInterpolator());

                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
                autoTransition.setDuration(200);

                Recolor recolor = new Recolor();
                recolor.setDuration(200);

                transitionSet.addTransition(autoTransition);
                transitionSet.addTransition(changeTransform);
                transitionSet.addTransition(recolor);
                TransitionManager.beginDelayedTransition(recyclerView, transitionSet);

                notifyDataSetChanged();
            });
            etherlink.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.transactionLink));
                context.startActivity(i);
            });

        }


    }

}
