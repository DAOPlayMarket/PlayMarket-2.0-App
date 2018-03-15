package com.blockchain.store.playmarket.ui.exchange_screen.exchange_confirm_fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.ui.exchange_screen.ExchangeActivityViewModel;
import com.blockchain.store.playmarket.utilities.QrUtils;

import net.glxn.qrgen.android.QRCode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeConfirmFragment extends Fragment {
    private static final String TAG = "ExchangeConfirmFragment";
    @BindView(R.id.exchange_details_currency) TextView exchangeDetailsCurrency;
    @BindView(R.id.exchange_details_amount) TextView exchangeDetailsAmount;
    @BindView(R.id.address_field) TextView addressField;
    @BindView(R.id.address_qr) ImageView addressQr;

    private ExchangeActivityViewModel exchangeActivityViewModel;

    public ExchangeConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_confirm, container, false);
        ButterKnife.bind(this, view);
        exchangeActivityViewModel = ViewModelProviders.of(getActivity()).get(ExchangeActivityViewModel.class);
        initObservers();
        return view;
    }

    private void initObservers() {
        exchangeActivityViewModel.chosenCurrency.observe(getActivity(), new Observer<ChangellyCurrency>() {
            @Override
            public void onChanged(@Nullable ChangellyCurrency changellyCurrency) {
                onCurrencyChosen(changellyCurrency);
            }
        });
        exchangeActivityViewModel.userEnteredAmount.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        exchangeActivityViewModel.payinAddress.observe(getActivity(),
                s -> addressQr.setImageBitmap(QrUtils.getBitmapQrFromAddress(s, QrUtils.QR_SIZE.LARGE)));
    }

    private void onCurrencyChosen(ChangellyCurrency changellyCurrencies) {
        exchangeDetailsAmount.setVisibility(View.VISIBLE);
        exchangeDetailsCurrency.setText(String.format(getString(R.string.exchange_details_currency), changellyCurrencies.name.toUpperCase()));
        exchangeDetailsAmount.setText(String.format(getString(R.string.exchange_details_amount),
                exchangeActivityViewModel.userEnteredAmount,
                exchangeActivityViewModel.chosenCurrency.getValue().name,
                exchangeActivityViewModel.estimatedAmount.getValue(),
                getString(R.string.ethereum_name)));
    }


}
