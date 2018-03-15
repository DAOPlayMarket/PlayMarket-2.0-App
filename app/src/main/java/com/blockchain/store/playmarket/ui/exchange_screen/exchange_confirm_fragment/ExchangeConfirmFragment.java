package com.blockchain.store.playmarket.ui.exchange_screen.exchange_confirm_fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.ui.exchange_screen.ExchangeActivityViewModel;
import com.blockchain.store.playmarket.utilities.QrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeConfirmFragment extends Fragment {
    private static final String TAG = "ExchangeConfirmFragment";
    @BindView(R.id.exchange_details_currency) TextView exchangeDetailsCurrency;
    @BindView(R.id.exchange_details_amount) TextView exchangeDetailsAmount;
    @BindView(R.id.exchange_details_fee) TextView exchangeDetailsFee;
    @BindView(R.id.exchange_details_info) TextView exchangeDetailsInfo;
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
        exchangeDetailsAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX,getActivity().getResources().getDimension(R.dimen.exchange_transaction_amount_large));
        exchangeDetailsAmount.setTypeface(exchangeDetailsAmount.getTypeface(), Typeface.BOLD);
        exchangeDetailsAmount.setVisibility(View.VISIBLE);
        exchangeDetailsFee.setVisibility(View.VISIBLE);
        initObservers();
        return view;
    }

    private void initObservers() {
        exchangeActivityViewModel.chosenCurrency.observe(getActivity(), this::onCurrencyChosen);

        exchangeActivityViewModel.estimatedAmount.observe(getActivity(), s -> {
            exchangeDetailsAmount.setText(String.format(getString(R.string.exchange_details_amount),
                    exchangeActivityViewModel.userEnteredAmount.getValue(),
                    exchangeActivityViewModel.chosenCurrency.getValue().name.toUpperCase(),
                    exchangeActivityViewModel.estimatedAmount.getValue(),
                    getString(R.string.ethereum_name)));

            exchangeDetailsInfo.setText(String.format(getString(R.string.exchange_confirm_transaction),
                    exchangeActivityViewModel.userEnteredAmount.getValue(),
                    exchangeActivityViewModel.chosenCurrency.getValue().name.toUpperCase()));
        });
        exchangeActivityViewModel.payinAddress.observe(getActivity(),
                address -> {
                    addressQr.setImageBitmap(QrUtils.getBitmapQrFromAddress(address, QrUtils.QR_SIZE.LARGE));
                    addressField.setText(address);
                });
    }

    private void onCurrencyChosen(ChangellyCurrency changellyCurrencies) {
        exchangeDetailsCurrency.setText(String.format(getString(R.string.exchange_details_currency), changellyCurrencies.name.toUpperCase()));
    }


}
