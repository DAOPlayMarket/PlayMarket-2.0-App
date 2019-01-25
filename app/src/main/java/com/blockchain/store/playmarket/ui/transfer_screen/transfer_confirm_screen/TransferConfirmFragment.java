package com.blockchain.store.playmarket.ui.transfer_screen.transfer_confirm_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class TransferConfirmFragment extends Fragment {
    private TransferViewModel transferViewModel;

    @BindView(R.id.sender_address_info_textView) TextView senderAddressTextView;
    @BindView(R.id.recipient_address_info_textView) TextView recipientAddressTextView;
    @BindView(R.id.amount_info_textView) TextView transferAmountTextView;
    @BindView(R.id.dimension_info_textView) TextView dimensionTextView;
    @BindView(R.id.password_editText) EditText passwordEditText;
    @BindView(R.id.password_textInputLayout) TextInputLayout passwordTextInputLayout;
    @BindView(R.id.fingerprint_layout) LinearLayout fingerprintLayout;
    @BindView(R.id.password_layout)  LinearLayout passwordLayout;
    @BindView(R.id.transaction_state_textView) TextView transactionStateTextView;
    @BindView(R.id.use_password_button) Button usePasswordButton;

    private Disposable fingerprintDisposable = Disposables.empty();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_confirm, container, false);
        ButterKnife.bind(this, view);
        getDataFromViewModel();

        return view;
    }

    @OnTextChanged(value = R.id.password_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onPasswordChanged(Editable editable) {
        transferViewModel.senderPassword.setValue(editable.toString());
    }

    @OnClick(R.id.use_password_button) void usePasswordButtonClicked() {
        initPassword();
    }

    private void getDataFromViewModel() {
        transferViewModel = ViewModelProviders.of(getActivity()).get(TransferViewModel.class);
        transferViewModel.senderAddress.observe(getActivity(), s -> senderAddressTextView.setText(s));
        transferViewModel.recipientAddress.observe(getActivity(), s -> recipientAddressTextView.setText(s));
        transferViewModel.transferAmount.observe(getActivity(), s -> transferAmountTextView.setText(s));
        transferViewModel.dimension.observe(getActivity(), s -> dimensionTextView.setText(s));
    }

    public void showError() {
        passwordTextInputLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(getContext(), R.color.red_error_color));
        passwordTextInputLayout.setError(getResources().getString(R.string.wrong_password));
    }

    public void initFingerprint() {
        fingerprintLayout.setVisibility(View.VISIBLE);
        usePasswordButton.setVisibility(View.VISIBLE);
        passwordLayout.setVisibility(View.GONE);
        enableFingerprint();
    }

    public void initPassword() {
        fingerprintLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.VISIBLE);
        usePasswordButton.setVisibility(View.GONE);
        disposedFingerprint();
        ((TransferActivity) getActivity()).setContinueButtonVisibility(View.VISIBLE);
    }

    private void enableFingerprint() {
        fingerprintDisposable = RxFingerprint.decrypt(getActivity(), FingerprintUtils.getEncryptedPassword())
                .subscribe(fingerprintDecryptionResult -> {
                    switch (fingerprintDecryptionResult.getResult()) {
                        case FAILED:
                            break;
                        case HELP:
                            break;
                        case AUTHENTICATED:
                            transactionStateTextView.setText(getResources().getString(R.string.confirm_button));
                            transferViewModel.senderPassword.setValue(fingerprintDecryptionResult.getDecrypted());
                            ((TransferActivity) getActivity()).continueButtonClicked();
                            break;
                    }
                }, throwable -> {
                    //noinspection StatementWithEmptyBody
                    if (RxFingerprint.keyInvalidated(throwable)) {
                        // The keys you wanted to use are invalidated because the user has turned off his
                        // secure lock screen or changed the fingerprints stored on the device
                        // You have to re-encrypt the data to access it
                    }
                    Log.e("ERROR", "decrypt", throwable);
                });
    }

    public void disposedFingerprint(){
        fingerprintDisposable.dispose();
    }
}
