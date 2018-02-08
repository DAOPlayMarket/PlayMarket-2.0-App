package com.blockchain.store.playmarket.utilities;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.ethereum.geth.Account;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import java.math.BigDecimal;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by Crypton04 on 08.02.2018.
 */

public class DialogManager {
    private static final String TAG = "DialogManager";

    public void showPurchaseDialog(AppInfo appinfo, String accountBalanceinWei, Context context, PurchaseDialogCallback callback) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.purchase_confirm_dialog);

        SimpleDraweeView appIcon = dialog.findViewById(R.id.appIcon);
        TextView appTitleText = dialog.findViewById(R.id.appTitleText);
        TextView priceText = dialog.findViewById(R.id.priceText);
        TextView balanceText = dialog.findViewById(R.id.balanceText);
        Button continueButton = dialog.findViewById(R.id.continueButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        EditText passwordText = dialog.findViewById(R.id.passwordText);

        appIcon.setImageURI(appinfo.app.getIconUrl());
        appTitleText.setText(appinfo.app.nameApp);
        balanceText.setText(new EthereumPrice(accountBalanceinWei).inEther().toString());
        priceText.setText(appinfo.getFormattedPrice());

        continueButton.setOnClickListener(v -> {
            if (new BigDecimal(accountBalanceinWei).compareTo(new BigDecimal(appinfo.app.price)) == 1) {
                try {
                    Application.keyManager.getKeystore().unlock(Application.keyManager.getAccounts().get(0), passwordText.getText().toString());
                    dialog.dismiss();
                    callback.onPurchaseClicked();
                } catch (Exception e) {
                    passwordText.setError(context.getString(R.string.wrong_password));
                }

            } else {
                balanceText.setError(context.getString(R.string.not_enought_balance));
                balanceText.requestFocus();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showInvestDialog(AppInfo appinfo, Context context, String accountBalance, InvestDialogCallback callback) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.invest_amount_dialog);

        EditText passwordText = dialog.findViewById(R.id.passwordText);
        EditText investmentAmountText = dialog.findViewById(R.id.investmentAmountText);
        Button continueButton = dialog.findViewById(R.id.continueButton);
        Button closeButton = dialog.findViewById(R.id.cancelButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());
        continueButton.setOnClickListener(v -> {
            if (new BigDecimal(accountBalance).compareTo(new EthereumPrice(investmentAmountText.getText().toString()).inWei()) == 1) {
                try {
                    Application.keyManager.getKeystore().unlock(Application.keyManager.getAccounts().get(0), passwordText.getText().toString());
                    dialog.dismiss();
                    callback.onInvestClicked(new EthereumPrice(investmentAmountText.getText().toString()).inWei());
                } catch (Exception e) {
                    passwordText.setError(context.getString(R.string.wrong_password));
                }

            } else {
//                balanceText.setError(context.getString(R.string.not_enought_balance));
//                balanceText.requestFocus();
            }

        });
        dialog.show();
    }

    public interface PurchaseDialogCallback {
        void onPurchaseClicked();
    }

    public interface InvestDialogCallback {
        public void onInvestClicked(BigDecimal investAmount);
    }

}
