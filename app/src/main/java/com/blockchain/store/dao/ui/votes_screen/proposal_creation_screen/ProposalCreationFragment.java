package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalCreationFragment extends Fragment implements ProposalCreationContract.View {

    private ProposalCreationPresenter presenter = new ProposalCreationPresenter();

    @BindView(R.id.recipient_inputLayout) TextInputLayout recipientInputLayout;
    @BindView(R.id.recipient_editText) EditText recipientEditText;
    @BindView(R.id.amount_inputLayout) TextInputLayout amountInputLayout;
    @BindView(R.id.amount_editText) EditText amountEditText;
    @BindView(R.id.description_editText) EditText descriptionEditText;
    @BindView(R.id.transactionBytecode_editText) EditText transactionBytecodeEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposal_creation, container, false);
        ButterKnife.bind(this, view);
        presenter.init(this);
        recipientEditText.setText(Constants.EMPTY_ADDRESS);
        amountEditText.setText("0");
        return view;
    }

    @OnClick(R.id.cancel_button)
    void onCancelClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.qrScanner_button)
    void onScannerButtonPressed() {
        Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.continue_button)
    void onContinueButtonClicked() {
        new DialogManager().showConfirmDialog(getContext(), (isUnlock) -> {
            if (isUnlock) {
                Proposal proposal = new Proposal();
                proposal.recipient = recipientEditText.getText().toString();
                proposal.amount = Long.valueOf(amountEditText.getText().toString());
                proposal.description = descriptionEditText.getText().toString();
                proposal.transactionBytecode = transactionBytecodeEditText.getText().toString();
                presenter.createProposal(proposal);
                if (getActivity() != null) getActivity().onBackPressed();
            } else {
                Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        recipientEditText.setText(data.getStringExtra("qrResult"));
    }
}
