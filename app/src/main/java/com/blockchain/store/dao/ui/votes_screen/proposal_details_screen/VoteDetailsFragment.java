package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen.ProposalCreationFragment;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.google.android.gms.common.util.ArrayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteDetailsFragment extends Fragment implements VoteDetailsContract.View {

    private VoteDetailsPresenter presenter = new VoteDetailsPresenter();
    private NavigationCallback callback;

    private static String PROPOSAL_TAG = "Proposal";
    private String localBalance;
    private String repositoryBalance;
    private  Proposal proposal;

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
    @BindView(R.id.ongoing_group) Group ongoingGroup;
    @BindView(R.id.finish_group) Group finishGroup;
    @BindView(R.id.execute_button) Button executeButton;
    @BindView(R.id.isAccepted_textView) TextView isAcceptedTextView;
    @BindView(R.id.isExecuted_textView) TextView isExecutedTextView;
    @BindView(R.id.localBalance_textView) TextView localBalanceTextView;
    @BindView(R.id.repositoryBalance_textView) TextView repositoryBalanceTextView;
    @BindView(R.id.progressBar) ProgressBar progressbar;

    public static Fragment newInstance(Proposal proposal) {
        Fragment fragment = new VoteDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_TAG, proposal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vote_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init(this);
        if (getArguments() != null) {
            proposal = getArguments().getParcelable(PROPOSAL_TAG);
            if (proposal != null) {
                bindData(proposal);
                Proposal.ProposalType proposalType = proposal.getProposalType();
                switch (proposalType) {
                    case Ongoing:
                        showOngoingProposal();
                        break;
                    case NotExecutedAccepted:
                        showNotExecutedAcceptedProposal();
                        break;
                    case NotExecutedNotAccepted:
                        showNotExecutedNotAcceptedProposal();
                        break;
                    case Executed:
                        showExecutedProposal(proposal);
                        break;
                    case Unexecutable:
                        showFailedProposal();
                        break;
                }
            }
        }
    }

    @OnClick(R.id.close_image_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.voteForSupport_button)
    void onSupportClicked() {
        new DialogManager().showVotingDialog(true, repositoryBalance, getContext(), (isUnlock) -> {
            if (isUnlock) {
                presenter.votingForProposal(proposal.proposalID, true, "");
                if (getActivity() != null) getActivity().onBackPressed();
            } else Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.voteAgainst_button)
    void onAgainstClicked() {
        new DialogManager().showVotingDialog(false, repositoryBalance, getContext(), (isUnlock) -> {
            if (isUnlock) {
                presenter.votingForProposal(proposal.proposalID, false, "");
                if (getActivity() != null) getActivity().onBackPressed();
            } else Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();

        });
    }

    @OnClick(R.id.execute_button)
    void onExecuteClicked(){
        new DialogManager().showVotingDialog(false, repositoryBalance, getContext(), (isUnlock) -> {
            if (isUnlock) {
                presenter.executeProposal(proposal.proposalID, proposal.fullDescriptionHash);
                if (getActivity() != null) getActivity().onBackPressed();
            } else Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.details_button)
    void onDetailsClicked(){
        callback.onProposalDetailsClicked(proposal);
    }


    private void bindData(Proposal proposal){

        Rules rules = presenter.getRules();

        idTextView.setText(String.valueOf(proposal.proposalID));
        descriptionTextView.setText(proposal.description);

        String totalPercentage = presenter.obtainPercentage(String.valueOf(proposal.numberOfVotes), DaoConstants.TOTAL_DAO_TOKEN);

        totalVotedProgressBar.setProgress(Integer.valueOf(totalPercentage));
        totalVotedTextView.setText(totalPercentage + " %");

        String quorumPercentage = presenter.obtainPercentage(String.valueOf(proposal.numberOfVotes), String.valueOf(rules.minimumQuorum));
        quorumProgressBar.setProgress(Integer.valueOf(quorumPercentage));
        quorumPercentTextView.setText(quorumPercentage + " %");
        quorumTextView.setText(String.valueOf(presenter.getTokenDecimals(proposal.numberOfVotes)));

        String majorityPercentage = presenter.obtainPercentage(String.valueOf(proposal.votesSupport), String.valueOf(rules.requisiteMajority));
        majorityProgressBar.setProgress(Integer.valueOf(majorityPercentage));
        majorityPercentTextView.setText(majorityPercentage + " %");
        majorityTextView.setText(String.valueOf(presenter.getTokenDecimals(proposal.votesSupport)));
    }

    private void showOngoingProposal(){
        showComponents(progressbar);
        presenter.getDaoTokenBalance();
    }

    private void showFailedProposal(){
        isAcceptedTextView.setText(getResources().getString(R.string.not_accepted));
        isExecutedTextView.setText(getResources().getString(R.string.not_executed));
        isAcceptedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        isExecutedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        showComponents(finishGroup);
    }

    private void showNotExecutedNotAcceptedProposal(){
        isAcceptedTextView.setText(getResources().getString(R.string.not_accepted));
        isExecutedTextView.setText(getResources().getString(R.string.not_executed));
        isAcceptedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        isExecutedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        showComponents(finishGroup, executeButton);
    }

    private void showNotExecutedAcceptedProposal(){
        isAcceptedTextView.setText(getResources().getString(R.string.accepted));
        isExecutedTextView.setText(getResources().getString(R.string.not_executed));
        isAcceptedTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        isExecutedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        showComponents(finishGroup, executeButton);
    }

    private void showExecutedProposal(Proposal proposal){
        isExecutedTextView.setText(getResources().getString(R.string.executed));
        isExecutedTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        if (proposal.proposalPassed) {
            isAcceptedTextView.setText(getResources().getString(R.string.accepted));
            isAcceptedTextView.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        else {
            isAcceptedTextView.setText(getResources().getString(R.string.not_accepted));
            isAcceptedTextView.setTextColor(getResources().getColor(R.color.colorRed));
        }
        showComponents(finishGroup);
    }

    @Override
    public void setDaoTokenBalance(Pair<String, String> tokenBalancePair) {
        localBalance = String.valueOf(getTokenWithDecimals(tokenBalancePair.first));
        repositoryBalance = String.valueOf(getTokenWithDecimals(tokenBalancePair.second));
        localBalanceTextView.setText(localBalance);
        repositoryBalanceTextView.setText(repositoryBalance);
        showComponents(ongoingGroup);
    }

    private double getTokenWithDecimals(String balance) {
        if (Long.valueOf(balance) == 0) {
            return 0;
        } else {
            return (Double.valueOf(balance) / Math.pow(10, 4));
        }
    }

    private void showComponents(View... views) {
        if (ArrayUtils.contains(views, ongoingGroup)) ongoingGroup.setVisibility(View.VISIBLE); else { ongoingGroup.setVisibility(View.GONE); }
        if (ArrayUtils.contains(views, finishGroup)) finishGroup.setVisibility(View.VISIBLE); else { finishGroup.setVisibility(View.GONE); }
        if (ArrayUtils.contains(views, executeButton)) executeButton.setVisibility(View.VISIBLE); else { executeButton.setVisibility(View.GONE); }
        if (ArrayUtils.contains(views, progressbar)) progressbar.setVisibility(View.VISIBLE); else { progressbar.setVisibility(View.GONE); }
    }

}
