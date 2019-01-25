package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.playmarket.R;

import org.web3j.abi.datatypes.Int;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalDetailsFragment extends Fragment implements ProposalDetailsContract.View {

    private ProposalDetailsPresenter presenter = new ProposalDetailsPresenter();

    private static String PROPOSAL_TAG = "Proposal";

    @BindView(R.id.id_textView) TextView idTextView;
    @BindView(R.id.description_textView) TextView descriptionTextView;

    @BindView(R.id.totalVoted_progressBar) ProgressBar totalVotedProgressBar;
    @BindView(R.id.totalVoted_textView) TextView totalVotedTextView;

    @BindView(R.id.quorum_progressBar) ProgressBar quorumProgressBar;
    @BindView(R.id.quorumPercent_textView) TextView quorumPercentTextView;
    @BindView(R.id.quorum_textView) TextView quorumTextView;

    @BindView(R.id.majority_progressBar) ProgressBar majorityProgressBar;
    @BindView(R.id.majorityPercent_textView) TextView majorityPercentTextView;
    @BindView(R.id.majority_textView) TextView majorityTextView;

    @BindView(R.id.executed_group) Group executedGroup;
    @BindView(R.id.not_executed_group) Group notExecutedGroup;

    @BindView(R.id.execute_button)
    Button executeButton;

    public static Fragment newInstance(Proposal proposal) {
        Fragment fragment = new ProposalDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_TAG, proposal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proposal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init(this);
        if (getArguments() != null) {
            Proposal proposal = getArguments().getParcelable(PROPOSAL_TAG);
            if (proposal != null) {
                bindData(proposal);
                if (proposal.isExecuted) {
                    executedGroup.setVisibility(View.VISIBLE);
                    notExecutedGroup.setVisibility(View.GONE);
                } else {
                    executedGroup.setVisibility(View.GONE);
                    notExecutedGroup.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @OnClick(R.id.close_image_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }


    private void bindData(Proposal proposal){

        Rules rules = presenter.getRules();

        idTextView.setText(String.valueOf(proposal.proposalID));
        descriptionTextView.setText(proposal.description);

        String quorumPercentage = presenter.obtainPercentage(proposal.numberOfVotes, rules.minimumQuorum);

        totalVotedProgressBar.setProgress(Integer.valueOf(quorumPercentage));
        totalVotedTextView.setText(quorumPercentage + " %");

        quorumProgressBar.setProgress(Integer.valueOf(quorumPercentage));
        quorumPercentTextView.setText(quorumPercentage + " %");
        quorumTextView.setText(String.valueOf(proposal.numberOfVotes));

        String majorityPercentage = presenter.obtainPercentage(proposal.votesSupport, rules.requisiteMajority);
        majorityProgressBar.setProgress(Integer.valueOf(majorityPercentage));
        majorityPercentTextView.setText(majorityPercentage + " %");
        majorityTextView.setText(String.valueOf(proposal.votesSupport));
    }

}
