package com.blockchain.store.dao.ui.votes_screen.proposal_details_screen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalDetailsFragment extends Fragment implements ProposalDetailsContract.View {

    private ProposalDetailsPresenter presenter = new ProposalDetailsPresenter();

    private static String ACCEPT_TAG = "IsAccept";
    private static String EXECUTE_TAG = "IsExecute";

    @BindView(R.id.id_textView) TextView idTextView;
    @BindView(R.id.description_textView) TextView descriptionTextView;

    public static Fragment newInstance(Boolean isAccept, Boolean isExecute, Proposal proposal){
        Fragment fragment = new ProposalDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ACCEPT_TAG, isAccept);
        bundle.putBoolean(EXECUTE_TAG, isExecute);
        bundle.putParcelable("Proposal", proposal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proposal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init(this);
        Rules rules = presenter.getRules();
        if (getArguments() != null) {
            Proposal proposal = getArguments().getParcelable("Proposal");
            if (proposal != null) {
                idTextView.setText(String.valueOf(proposal.proposalID));
                descriptionTextView.setText(proposal.description);
            }
        }
    }

    @OnClick(R.id.close_image_button)
    void onCloseClicked(){
        if (getActivity() != null) getActivity().onBackPressed();
    }

}
