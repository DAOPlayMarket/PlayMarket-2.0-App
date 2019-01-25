package com.blockchain.store.dao.ui.votes_screen.proposal_creation_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;
import com.blockchain.store.playmarket.utilities.crypto.GenerateTransactionData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProposalCreationFragment extends Fragment implements ProposalCreationContract.View{

    private ProposalCreationPresenter presenter = new ProposalCreationPresenter();

    @BindView(R.id.amount_editText) EditText amountEditText;
    @BindView(R.id.recipient_editText) EditText recipientEditText;
    @BindView(R.id.description_editText) EditText descriptionEditText;
    @BindView(R.id.descriptionHash_editText) EditText fullDescriptionHashEditText;
    @BindView(R.id.transactionBytecode_textView) EditText transactionBytecodeTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_proposal_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.init(this);
        ButterKnife.bind(this, view);
        recipientEditText.setText(AccountManager.getAddress().getHex());
        amountEditText.setText("1");
    }

    @OnClick(R.id.cancel_button)
    void onCancelClicked(){
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.close_button)
    void onCloseClicked(){
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.qrScanner_button)
    void onScannerButtonPressed() {
        Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.continue_button)
    void onContinueButtonClicked(){
        new DialogManager().showPasswordDialogWithDetails(amountEditText.getText().toString(), recipientEditText.getText().toString(), getContext(), (isUnlock) -> {
            if (isUnlock) {
                presenter.createProposal(
                        recipientEditText.getText().toString(),
                        amountEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        fullDescriptionHashEditText.getText().toString(),
                        transactionBytecodeTextView.getText().toString()
                );
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
