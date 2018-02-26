package com.blockchain.store.playmarket.utilities;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;

/**
 * Created by Crypton04 on 08.02.2018.
 */

public class DialogManager {

    private static final String TAG = "DialogManager";

    public static AlertDialog createFolderDialog;

    public void showPurchaseDialog(AppInfo appinfo, Context context, PurchaseDialogCallback callback) {
        String accountBalanceInWei = AccountManager.getUserBalance();

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
        balanceText.setText(new EthereumPrice(accountBalanceInWei).inEther().toString());
        priceText.setText(appinfo.getFormattedPrice());

        continueButton.setOnClickListener(v -> {
            if (new BigDecimal(accountBalanceInWei).compareTo(new BigDecimal(appinfo.app.price)) == 1) {
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

    public void showInvestDialog(AppInfo appinfo, Context context, InvestDialogCallback callback) {
        String accountBalanceInWei = AccountManager.getUserBalance();

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.invest_amount_dialog);

        EditText passwordText = dialog.findViewById(R.id.passwordText);
        EditText investmentAmountText = dialog.findViewById(R.id.investmentAmountText);
        Button continueButton = dialog.findViewById(R.id.continueButton);
        Button closeButton = dialog.findViewById(R.id.cancelButton);
        TextView balanceText = dialog.findViewById(R.id.balanceText);
        closeButton.setOnClickListener(v -> dialog.dismiss());
        balanceText.setText(new EthereumPrice(accountBalanceInWei).inEther().toString());
        continueButton.setOnClickListener(v -> {
            if (investmentAmountText.getText().toString().trim().isEmpty()) {
                investmentAmountText.setError(context.getString(R.string.wrong_invest_amout));
                investmentAmountText.requestFocus();
                return;
            }
            if (new BigDecimal(accountBalanceInWei).compareTo(new EthereumPrice(investmentAmountText.getText().toString()).inWei()) == 1) {
                try {
                    Application.keyManager.getKeystore().unlock(Application.keyManager.getAccounts().get(0), passwordText.getText().toString());
                    dialog.dismiss();
                    callback.onInvestClicked(investmentAmountText.getText().toString());
                } catch (Exception e) {
                    passwordText.setError(context.getString(R.string.wrong_password));
                }

            } else {
                balanceText.setError(context.getString(R.string.not_enought_balance));
                balanceText.requestFocus();
            }

        });
        dialog.show();
    }

    public void showCreateFolderDialog(Context context, String folderName, CreateFolderDialogCallback callback) {

        createFolderDialog = new AlertDialog.Builder(context)
                .setView(R.layout.create_folder_dialog)
                .setCancelable(false)
                .create();
        createFolderDialog.show();

        EditText folderNamedText = (EditText) createFolderDialog.findViewById(R.id.folder_editText);
        Button confirmButton = (Button) createFolderDialog.findViewById(R.id.confirm_create_button);
        Button cancelButton = (Button) createFolderDialog.findViewById(R.id.cancel_create_button);
        TextInputLayout passwordLayout = (TextInputLayout) createFolderDialog.findViewById(R.id.folder_inputLayout);

        folderNamedText.setText(folderName);

        confirmButton.setOnClickListener(v -> {
            if (folderNamedText.getText().toString().equals("")) {
                passwordLayout.setErrorEnabled(true);
                passwordLayout.setError(context.getResources().getString(R.string.empty_field));
            } else {
                callback.createFolderClicked(folderNamedText.getText().toString());
                createFolderDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> createFolderDialog.dismiss());
        createFolderDialog.show();
    }

    public void showConfirmImportDialog(Context context, String fileData, ConfirmImportDialogCallback callback) {
        AlertDialog confirmImportDialog = new AlertDialog.Builder(context)
                .setView(R.layout.password_prompt_dialog)
                .setCancelable(false)
                .create();
        confirmImportDialog.show();
        final EditText passwordText = (EditText) confirmImportDialog.findViewById(R.id.passwordText);
        Button importButton = (Button) confirmImportDialog.findViewById(R.id.continueButton);
        Button closeButton = (Button) confirmImportDialog.findViewById(R.id.close_button);
        TextInputLayout passwordLayout = (TextInputLayout) confirmImportDialog.findViewById(R.id.password_inputLayout);

        importButton.setOnClickListener(v -> {
            if (new FileUtils().confirmImport(fileData, passwordText.getText().toString())) {
                callback.onImportSuccessful();
                confirmImportDialog.dismiss();
           } else {
               passwordLayout.setErrorEnabled(true);
               passwordLayout.setError(context.getResources().getString(R.string.wrong_password));
           }
        });
        closeButton.setOnClickListener(v -> confirmImportDialog.dismiss());
    }

    public interface PurchaseDialogCallback {
        void onPurchaseClicked();
    }

    public interface InvestDialogCallback {
        public void onInvestClicked(String investAmount);
    }

    public interface CreateFolderDialogCallback {
        void createFolderClicked(String folderName);
    }

    public interface ConfirmImportDialogCallback {
        void onImportSuccessful();
    }
}
