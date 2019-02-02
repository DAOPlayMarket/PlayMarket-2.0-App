package com.blockchain.store.playmarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.TransictionUtils;
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
    private boolean[] isExpandedArray;
    private RecyclerView recyclerView;


    public TransactionHistoryAdapter(ArrayList<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
        this.isExpandedArray = new boolean[transactionModels.size()];
    }

    public void reloadItems(ArrayList<TransactionModel> transactionModels) {
        if (this.transactionModels.size() != transactionModels.size()) {
            this.isExpandedArray = new boolean[transactionModels.size()];
        }
        this.transactionModels = transactionModels;
        notifyDataSetChanged();
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
            viewholder.bind(transactionModels.get(position), position);

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
        @BindView(R.id.details_additional_info) TextView detailAdditionalInfo;
        @BindView(R.id.group) Group group;

        private Context context;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(TransactionModel model, int position) {
            boolean isExpanded = isExpandedArray[position];

            setViewsText(model);
            setViewsVisibility(model, isExpanded);
            setViewsColor(model, isExpanded);
            arrow.setRotation(isExpanded ? 270 : 180);

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

            group.setOnClickListener(v -> {
                isExpandedArray[position] = !isExpandedArray[position];

                TransitionSet transitionSet = TransictionUtils.getTransactionSetForHistoryAdapter();
                TransitionManager.beginDelayedTransition(recyclerView, transitionSet);

                notifyDataSetChanged();
            });
            etherlink.setText(Html.fromHtml(context.getString(R.string.etherscan_link)));
            etherlink.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.transactionLink));
                context.startActivity(i);
            });

        }

        private void setViewsColor(TransactionModel model, boolean isExpanded) {
            result.setTextColor(model.isPositive() ? ContextCompat.getColor(context, R.color.positive_value)
                    : ContextCompat.getColor(context, R.color.negative_value));
            arrowHolder.setBackground(
                    new ColorDrawable(isExpanded
                            ? ContextCompat.getColor(context, R.color.negative_value)
                            : ContextCompat.getColor(context, R.color.colorPrimary)));
            rightLine.setBackground(new ColorDrawable(isExpanded
                    ? ContextCompat.getColor(context, R.color.negative_value_line)
                    : ContextCompat.getColor(context, R.color.colorPrimary)));
        }

        private void setViewsVisibility(TransactionModel model, boolean isExpanded) {
            detailAdditionalInfo.setVisibility(model.getDetailedInfo().isEmpty() ? View.GONE : View.VISIBLE);
            detailHolder.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        private void setViewsText(TransactionModel model) {

            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
            date.setText(format.format(new Date(model.timeStamp)));

            result.setText(model.getTransactionFormattedResult());
            status.setText(model.transactionStatus.toString());
            title.setText(model.getFormattedTitle());
            hash.setText(model.transactionHash);
            detailAdditionalInfo.setText(model.getDetailedInfo());
        }


    }

}
