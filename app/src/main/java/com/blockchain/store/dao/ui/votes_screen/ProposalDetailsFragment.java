package com.blockchain.store.dao.ui.votes_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalDetailsFragment extends Fragment {

    public static String INSTANCE_TAG = "Proposal";

    @BindView(R.id.endTime_textView) TextView endTimeTextView;
    @BindView(R.id.id_textView) TextView idTextView;
    @BindView(R.id.recipient_textView) TextView recipientTextView;
    @BindView(R.id.amount_textView) TextView amountTextView;
    @BindView(R.id.description_textView) TextView descriptionTextView;
    @BindView(R.id.transactionBytecode_textView) TextView transactionBytecodeTextView;

    public static ProposalDetailsFragment newInstance(Proposal proposal) {
        Bundle args = new Bundle();
        args.putParcelable(INSTANCE_TAG, proposal);
        ProposalDetailsFragment fragment = new ProposalDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_details, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            Proposal proposal = getArguments().getParcelable(INSTANCE_TAG);
            if (proposal != null) bindData(proposal);
        }
        return view;
    }

    @OnClick(R.id.copyBytecode_button)
    void onCopyBytecodeClicked() {
        ClipboardUtils.copyToClipboard(getActivity(), transactionBytecodeTextView.getText().toString().replaceAll(" ", ""));
        ToastUtil.showToast(R.string.bytecode_copied);
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    private void bindData(Proposal proposal) {
        Date date = new Date(proposal.endTimeOfVoting * 1000);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
//        String dt = new java.util.Date(proposal.endTimeOfVoting * 1000).toString();
        endTimeTextView.setText(dateFormat.format(date));
        idTextView.setText(String.valueOf(proposal.proposalID));
        recipientTextView.setText(proposal.recipient);
        String amountEth = new EthereumPrice(String.valueOf(proposal.amount), EthereumPrice.Currency.WEI).inEther().toString();
        amountTextView.setText(amountEth);
        descriptionTextView.setText(String.valueOf(proposal.description));
        transactionBytecodeTextView.setText(proposal.transactionBytecode);
    }

}
