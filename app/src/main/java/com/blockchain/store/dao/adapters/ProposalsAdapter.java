package com.blockchain.store.dao.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.playmarket.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProposalsAdapter extends RecyclerView.Adapter<ProposalsAdapter.ViewHolder> {

    private ArrayList<Proposal> proposals;
    private Callbacks.ProposalCallback proposalCallback;

    public ProposalsAdapter(ArrayList<Proposal> proposals, Callbacks.ProposalCallback proposalCallback){
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

    public void setItems(ArrayList<Proposal> proposals) {
        this.proposals = proposals;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_textView) TextView idTextView;
        @BindView(R.id.description_textView) TextView descriptionTextView;

        protected ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
                    }

        protected void bind(Proposal proposal) {
            idTextView.setText(String.valueOf(proposal.proposalID));
            descriptionTextView.setText(proposal.description);
            itemView.setOnClickListener(v -> proposalCallback.onItemClicked(proposal));
        }
    }

}
