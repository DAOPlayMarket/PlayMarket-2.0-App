package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalCreationFragment extends Fragment implements ProposalCreationContract.View {

    private ProposalCreationPresenter presenter = new ProposalCreationPresenter();
    private static String PROPOSAL_TAG = "Proposal";

    @BindView(R.id.title_textView) TextView titleTextView;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.id_textView) TextView idTextView;
    @BindView(R.id.recipient_editText) EditText recipientEditText;
    @BindView(R.id.qrScanner_button) ImageView qrScannerButton;
    @BindView(R.id.amount_editText) EditText amountEditText;
    @BindView(R.id.description_editText) EditText descriptionEditText;
    @BindView(R.id.transactionBytecode_editText) EditText transactionBytecodeEditText;
    @BindView(R.id.continue_button) Button continueButton;
    @BindView(R.id.cancel_button) Button cancelButton;

    public static Fragment newInstance(Proposal proposal) {
        Fragment fragment = new ProposalCreationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROPOSAL_TAG, proposal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proposal_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.init(this);
        if (getArguments() != null) {
            Proposal proposal = getArguments().getParcelable(PROPOSAL_TAG);
            if (proposal != null) showProposalDetailsView(proposal);
        } else showProposalCreationView();
    }

    private void showProposalDetailsView(Proposal proposal) {
        titleTextView.setText(getResources().getString(R.string.proposal_details));
        textView.setVisibility(View.VISIBLE);
        idTextView.setVisibility(View.VISIBLE);
        idTextView.setText(String.valueOf(proposal.proposalID));
        recipientEditText.setText(proposal.recipient);
        recipientEditText.setEnabled(false);
        qrScannerButton.setVisibility(View.GONE);
        amountEditText.setText(String.valueOf(proposal.amount));
        amountEditText.setEnabled(false);
        descriptionEditText.setText(String.valueOf(proposal.description));
        descriptionEditText.setEnabled(false);
        transactionBytecodeEditText.setText(proposal.fullDescriptionHash);
        transactionBytecodeEditText.setEnabled(false);
        continueButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

    private void showProposalCreationView() {
        recipientEditText.setText(Constants.EMPTY_ADDRESS);
        amountEditText.setText("0");
        titleTextView.setText(getResources().getString(R.string.create_proposal));
        textView.setVisibility(View.GONE);
        idTextView.setVisibility(View.GONE);
        recipientEditText.setEnabled(true);
        qrScannerButton.setVisibility(View.VISIBLE);
        amountEditText.setEnabled(true);
        descriptionEditText.setEnabled(true);
        transactionBytecodeEditText.setEnabled(true);
        continueButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
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
        new DialogManager().showPasswordDialogWithDetails(amountEditText.getText().toString(), recipientEditText.getText().toString(), getContext(), (isUnlock) -> {
            if (isUnlock) {
                presenter.createProposal(
                        recipientEditText.getText().toString(),
                        amountEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        transactionBytecodeEditText.getText().toString(),
                        transactionBytecodeEditText.getText().toString()
                );
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
