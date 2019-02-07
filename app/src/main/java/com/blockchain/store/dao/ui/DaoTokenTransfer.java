package com.blockchain.store.dao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.blockchain.store.playmarket.utilities.LocaleUtils;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.mtramin.rxfingerprint.RxFingerprint;

import org.ethereum.geth.Account;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;
import org.json.JSONObject;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA_RINKEBY;
import static com.blockchain.store.playmarket.utilities.Constants.USER_ETHERSCAN_ID;

public class DaoTokenTransfer extends BaseActivity {
    private static final String TAG = "DaoTokenTransfer";

    @BindView(R.id.unlock_account) EditText unlock_account;
    @BindView(R.id.unlock_account_btn) Button unlock_account_btn;
    @BindView(R.id.withdraw_amount) EditText withdraw_amount;
    @BindView(R.id.button) Button button;
    @BindView(R.id.button2) Button button2;

    public static void start(Context context, DaoToken daoToken) {
        Intent starter = new Intent(context, DaoTokenTransfer.class);
        starter.putExtra("abc", daoToken);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao_token_transfer);
        ButterKnife.bind(this);
        initFingerPrint();

    }

    private void initFingerPrint() {
        if (FingerprintUtils.isFingerprintAvailibility(this)) {
            RxFingerprint.decrypt(this, FingerprintUtils.getEncryptedPassword())
                    .subscribe(fingerprintDecryptionResult -> {
                        switch (fingerprintDecryptionResult.getResult()) {
                            case FAILED:
                                Toast.makeText(this, R.string.fingerprint_not_recognized, Toast.LENGTH_SHORT).show();
                                break;
                            case HELP:
                                break;
                            case AUTHENTICATED:
                                Account account = AccountManager.getAccount();
                                try {
                                    AccountManager.getKeyStore().unlock(account, fingerprintDecryptionResult.getDecrypted());
                                    Toast.makeText(this, "Account unlocked!", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, R.string.fingerprint_not_recognized, Toast.LENGTH_SHORT).show();
                                }

                                break;
                        }
                    }, throwable -> Log.e("ERROR", "decrypt", throwable));
        }
    }

    private void performChangeLocale() {
        LocaleUtils.setLocale(new Locale("en"));
        LocaleUtils.updateConfig((Application) getApplication(), getBaseContext().getResources().getConfiguration());
        recreate();
    }

    @OnClick(R.id.unlock_account_btn)
    public void unlock_account_btn() {
        performChangeLocale();
//        Account account = AccountManager.getAccount();
//        try {
//            AccountManager.getKeyStore().unlock(account, unlock_account.getText().toString());
//            Toast.makeText(this, "Account unlocked!", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, R.string.fingerprint_not_recognized, Toast.LENGTH_SHORT).show();
//        }
    }

    public static Transaction transactionTest = null;

    @OnClick(R.id.button)
    public void button() {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    Pair<Transaction, Transaction> transaction = CryptoUtils.test111(result, 50);
                    String rawTransaction = CryptoUtils.getRawTransaction(transaction.first);
                    String s = Numeric.toHexStringNoPrefix(rawTransaction.getBytes());
//                    return new TransactionSender().send(transaction1)
                    return Observable.just(1);
                })

                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }


    @OnClick(R.id.button2)
    void onDownloadClicked() {
        Ion.with(this)
                .load("https://m000003.playmarket.io/api/download-app?idApp=0")
                .setHeader("macHash", "macHashTest")
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        Log.d(TAG, "onProgress() called with: downloaded = [" + downloaded + "], total = [" + total + "]");
                    }
                }).write(new File("")).setCallback(new FutureCallback<File>() {
            @Override
            public void onCompleted(Exception e, File result) {
                Log.d(TAG, "onCompleted() called with: e = [" + e + "], result = [" + result + "]");
            }
        });

//        RestApi.getServerApi().downloadFile()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(Object o) {

    }

    public BigInteger mapEstimateResult(EthEstimateGas result) {
        if (result.getError() == null) {
            BigInteger amountUsed = result.getAmountUsed();
            amountUsed = amountUsed.add(new BigInteger(String.valueOf(Constants.GAS_LIMIT_ADDITION)));
            return amountUsed;
        } else {
            throw new RuntimeException(result.getError().getMessage());
        }
    }

    public Observable<EthSendTransaction> mapWithAddGasLimitToTx(BigInteger gasLimit, Transaction signedTransaction) {
        try {
            String encodedJson = signedTransaction.encodeJSON();
            JSONObject json = new JSONObject(encodedJson);
            json.put("gas", Numeric.toHexStringWithPrefix(gasLimit));
            Transaction transaction = new Transaction(json.toString());
            Transaction signedTx = CryptoUtils.keyManager.getKeystore().signTx(AccountManager.getAccount(), transaction, new BigInt(USER_ETHERSCAN_ID));
            Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA_RINKEBY));
            return build.ethSendRawTransaction("0x" + CryptoUtils.getRawTransaction(signedTx)).observable();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void transferSuccess(EthEstimateGas ethEstimateGas) {
        BigInteger bigInteger = Numeric.toBigInt(String.valueOf(ethEstimateGas.getAmountUsed()));
        Log.d(TAG, "transferSuccess: " + bigInteger);
    }

    private void transferFailed(Throwable t) {
        Log.d(TAG, "transferFailed: ");
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d(TAG, "transferSuccess: ");
    }


}
