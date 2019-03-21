package com.blockchain.store.playmarket.utilities.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.DappTransaction;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.mtramin.rxfingerprint.RxFingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class DappTxDialog extends DialogFragment {
    private static final String TAG = "DappTxDialog";

    @BindView(R.id.view2) View view2;
    @BindView(R.id.cancel_button) ImageView cancel_button;
    @BindView(R.id.recipient_address) TextView recipient_address;
    @BindView(R.id.send_amouut_field) TextView send_amouut_field;
    @BindView(R.id.usePassword_button) TextView usePassword_button;
    @BindView(R.id.fingerprintGroup) Group fingerprintGroup;
    @BindView(R.id.passwordGroup) Group passwordGroup;
    @BindView(R.id.confirm_button) Button confirm_button;
    @BindView(R.id.password_editText) TextInputEditText passwordEditText;
    @BindView(R.id.password_inputLayout) TextInputLayout passwordLayout;

    private Disposable fingerprintDisposable = Disposables.empty();
    private TxDialogCallback callback;
    DappTransaction tx;

    private Context context;

    public static DappTxDialog newInstance(DappTransaction tx) {
        Bundle args = new Bundle();
        args.putParcelable("key", tx);
        DappTxDialog fragment = new DappTxDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tx = getArguments().getParcelable("key");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dapp_tx_dialog
                , null);
        ButterKnife.bind(this, view);
        this.context = getActivity();
        initFingerprint();
        setData();
        return view;
    }

    private void setData() {
        EthereumPrice ethereumPrice = new EthereumPrice(tx.getValue());
        recipient_address.setText(tx.getTo());
        send_amouut_field.setText(ethereumPrice.inEther().toPlainString() + " ETH");
    }

    private void initFingerprint() {

        confirm_button.setOnClickListener(v -> {
            boolean isUnlock = AccountManager.unlockKeystore(passwordEditText.getText().toString());
            if (isUnlock) {
                callback.onAccountUnlocked(tx);
                this.dismiss();
            } else {
                passwordLayout.setError("Wrong password");
            }
        });

        if (FingerprintUtils.isFingerprintAvailibility(context)) {
            passwordGroup.setVisibility(View.GONE);
            fingerprintGroup.setVisibility(View.VISIBLE);
            fingerprintDisposable = RxFingerprint.decrypt(context, FingerprintUtils.getEncryptedPassword())
                    .subscribe(fingerprintDecryptionResult -> {
                        switch (fingerprintDecryptionResult.getResult()) {
                            case AUTHENTICATED:
                                callback.onAccountUnlocked(tx);
                                this.dismiss();
                                fingerprintDisposable.dispose();
                        }
                    }, throwable -> Log.e("ERROR", "decrypt", throwable));
        } else {
            passwordGroup.setVisibility(View.VISIBLE);
            fingerprintGroup.setVisibility(View.GONE);
        }

        usePassword_button.setOnClickListener(v -> {
            fingerprintDisposable.dispose();
            passwordGroup.setVisibility(View.VISIBLE);
            fingerprintGroup.setVisibility(View.GONE);
        });

        cancel_button.setOnClickListener(v -> {
            if (FingerprintUtils.isFingerprintAvailibility(context))
                fingerprintDisposable.dispose();
            this.dismiss();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TxDialogCallback) {
            this.callback = (TxDialogCallback) context;
        }
    }

    public interface TxDialogCallback{
        void onAccountUnlocked(DappTransaction tx);
    }
}
