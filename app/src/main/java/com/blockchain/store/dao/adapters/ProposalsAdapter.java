package com.blockchain.store.dao.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProposalsAdapter extends RecyclerView.Adapter<ProposalsAdapter.ViewHolder> {

    private ArrayList<Proposal> proposals;
    private Callbacks.ProposalCallback proposalCallback;
    private DaoDatabase daoDatabase = Application.getDaoDatabase();

    public ProposalsAdapter(ArrayList<Proposal> proposals, Callbacks.ProposalCallback proposalCallback) {
        this.proposals = proposals;
        this.proposalCallback = proposalCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proposal, parent, false);
        return new ProposalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proposal proposal = proposals.get(position);
        holder.bind(proposal);
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public void updateItems(ArrayList<Proposal> proposals) {
        this.proposals = proposals;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        @BindView(R.id.id_textView) TextView idTextView;
        @BindView(R.id.description_textView) TextView descriptionTextView;
        @BindView(R.id.isAccepted_textView) TextView isAcceptedTextView;
        @BindView(R.id.isExecuted_textView) TextView isExecutedTextView;
        @BindView(R.id.votingEndTime_textView) TextView votingEndTimeTextView;
        @BindView(R.id.statusGroup) Group statusGroup;
        @BindView(R.id.timeGroup) Group timeGroup;

        protected ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        protected void bind(Proposal proposal) {
            idTextView.setText(String.valueOf(proposal.proposalID));
            descriptionTextView.setText(proposal.description);
            itemView.setOnClickListener(v -> proposalCallback.onItemClicked(proposal));

            Proposal.ProposalType proposalType = proposal.getProposalType();
            switch (proposalType) {
                case Ongoing:
                    statusGroup.setVisibility(View.GONE);
                    timeGroup.setVisibility(View.VISIBLE);
                    votingEndTimeTextView.setText(timeConverter((proposal.endTimeOfVoting * 1000) - System.currentTimeMillis()));
                    break;
                case NotExecutedAccepted:
                    statusGroup.setVisibility(View.VISIBLE);
                    timeGroup.setVisibility(View.GONE);
                    isExecutedTextView.setText(itemView.getResources().getString(R.string.not_executed));
                    isAcceptedTextView.setText(itemView.getResources().getString(R.string.accepted));
                    isAcceptedTextView.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
                    isExecutedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    break;
                case NotExecutedNotAccepted:
                    statusGroup.setVisibility(View.VISIBLE);
                    timeGroup.setVisibility(View.GONE);
                    isExecutedTextView.setText(itemView.getResources().getString(R.string.not_executed));
                    isAcceptedTextView.setText(itemView.getResources().getString(R.string.not_accepted));
                    isAcceptedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    isExecutedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    break;
                case Executed:
                    statusGroup.setVisibility(View.VISIBLE);
                    timeGroup.setVisibility(View.GONE);
                    if (proposal.proposalPassed) {
                        isAcceptedTextView.setText(itemView.getResources().getString(R.string.accepted));
                        isAcceptedTextView.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
                    } else {
                        isAcceptedTextView.setText(itemView.getResources().getString(R.string.not_accepted));
                        isAcceptedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    }
                    isExecutedTextView.setText(itemView.getResources().getString(R.string.executed));
                    isExecutedTextView.setTextColor(itemView.getResources().getColor(R.color.colorGreen));
                    break;
                case Unexecutable:
                    statusGroup.setVisibility(View.VISIBLE);
                    timeGroup.setVisibility(View.GONE);
                    isExecutedTextView.setText(itemView.getResources().getString(R.string.not_executed));
                    isAcceptedTextView.setText(itemView.getResources().getString(R.string.not_accepted));
                    isAcceptedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    isExecutedTextView.setTextColor(itemView.getResources().getColor(R.color.colorRed));
                    break;
            }
        }
    }

    private String timeConverter(long unixTimestamp){
        long days = TimeUnit.MILLISECONDS.toDays(unixTimestamp);
        unixTimestamp -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(unixTimestamp);
        unixTimestamp -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(unixTimestamp);
        return String.valueOf(days) + "d " + String.valueOf(hours) + "h " + String.valueOf(minutes) + "min";
    }

}
