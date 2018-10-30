package com.blockchain.store.playmarket.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.UserReview;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DialogManager {
    private static final String TAG = "DialogManager";

    private EditText folderNamedText;
    private EditText passwordText;

    public void showReviewDialog(UserReview userReview, Context context, CreateReviewCallback callback) {
        String accountBalanceInWei = AccountManager.getUserBalance();
        String accountBalanceAsString = new EthereumPrice(accountBalanceInWei).inEther().toString();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.invest_amount_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);

        TextView userName = dialog.findViewById(R.id.user_name);
        TextView userCommentary = dialog.findViewById(R.id.user_commentary);
        TextView readMore = dialog.findViewById(R.id.read_more);
        LinearLayout replayReviewHolder = dialog.findViewById(R.id.replay_review_holder);
        EditText commentary = dialog.findViewById(R.id.commentary);
        MaterialRatingBar ratingBar = dialog.findViewById(R.id.user_rating_bar);
        TextView yourBalanceText = dialog.findViewById(R.id.your_balance);
        EditText passwordField = dialog.findViewById(R.id.password_field);
        TextView fingerPrintTitle = dialog.findViewById(R.id.fingerprint_info_textView);
        Button continueButton = dialog.findViewById(R.id.continue_button);
        Button closeButton = dialog.findViewById(R.id.cancelButton);


        Disposable fingerprintDisposable = Disposables.empty();

        if (userReview == null) {
            replayReviewHolder.setVisibility(View.GONE);
        } else {
            userName.setText(userReview.author);
            userCommentary.setText(userReview.text);
            ratingBar.setVisibility(View.GONE);

        }

        yourBalanceText.setText(String.format(context.getString(R.string.your_balance_is), accountBalanceAsString));

        if (FingerprintUtils.isFingerprintAvailibility(context)) {
            fingerPrintTitle.setVisibility(View.VISIBLE);
            fingerprintDisposable = RxFingerprint.decrypt(context, Hawk.get(Constants.ENCRYPTED_PASSWORD))
                    .subscribe(fingerprintDecryptionResult -> {
                        switch (fingerprintDecryptionResult.getResult()) {
                            case FAILED:
                                passwordField.setError(context.getResources().getString(R.string.fingerprint_not_recognized));
                                passwordField.requestFocus();
                                break;
                            case HELP:
                                break;
                            case AUTHENTICATED:
                                //todo add check if comment isn't empty
                                if (new BigDecimal(accountBalanceInWei).compareTo(new BigDecimal("0")) == 1) {
                                    try {
                                        passwordField.setText(fingerprintDecryptionResult.getDecrypted());
                                        AccountManager.getKeyStore().unlock(AccountManager.getAccount(), passwordField.getText().toString());
                                        dialog.dismiss();
                                        callback.onReviewInfoReady(commentary.getText().toString(), String.valueOf((int) ratingBar.getRating()));
                                    } catch (Exception e) {
                                        passwordField.setError(context.getString(R.string.wrong_password));
                                    }

                                } else {
                                    passwordField.setError(context.getString(R.string.not_enought_balance));
                                    passwordField.requestFocus();
                                }
                                break;
                        }
                    }, throwable -> Log.e("ERROR", "decrypt", throwable));
        }


        closeButton.setOnClickListener(v -> dialog.dismiss());
        continueButton.setOnClickListener(v -> {
            if (new BigDecimal(accountBalanceInWei).compareTo(new BigDecimal("0")) == 1) {
                try {
                    AccountManager.getKeyStore().unlock(AccountManager.getAccount(), passwordField.getText().toString());
                    dialog.dismiss();
                    callback.onReviewInfoReady(commentary.getText().toString(), String.valueOf((int) ratingBar.getRating()));
                } catch (Exception e) {
                    passwordField.setError(context.getString(R.string.wrong_password));
                }

            } else {
                passwordField.setError(context.getString(R.string.not_enought_balance));
                passwordField.requestFocus();
            }

        });
        dialog.show();
    }


    public AlertDialog showCreateFolderDialog(Context context, String folderName, CreateFolderDialogCallback callback) {

        AlertDialog createFolderDialog = new AlertDialog.Builder(context)
                .setView(R.layout.create_folder_dialog)
                .setCancelable(false)
                .create();
        createFolderDialog.show();

        folderNamedText = (EditText) createFolderDialog.findViewById(R.id.folder_editText);
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

        cancelButton.setOnClickListener(v -> {
            createFolderDialog.dismiss();
        });
        return createFolderDialog;
    }

    public void showAutoUpdateDialog(Context context, App app) {
        AlertDialog autoUpdateDialog = new AlertDialog.Builder(context)
                .setView(R.layout.auto_update_dialog)
                .setCancelable(false)
                .create();
        autoUpdateDialog.show();
        autoUpdateDialog.findViewById(R.id.btn_later).setOnClickListener(
                v -> autoUpdateDialog.dismiss());
        Button btnUpdate = autoUpdateDialog.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> {
            new MyPackageManager().startDownloadApkService(app, true);
            autoUpdateDialog.dismiss();
        });

    }

    public String getFolderNameText() {
        return folderNamedText.getText().toString();
    }


    public String getPasswordText() {
        return passwordText.getText().toString();
    }

    public interface CreateFolderDialogCallback {
        void createFolderClicked(String folderName);
    }

    public interface ConfirmImportDialogCallback {
        void onImportSuccessful();
    }

    public interface CreateReviewCallback {
        void onReviewInfoReady(String review, String rating);
    }

    public enum DialogNames {
        CREATE_FOLDER_DIALOG,
        CONFIRM_IMPORT_DIALOG
    }
}
