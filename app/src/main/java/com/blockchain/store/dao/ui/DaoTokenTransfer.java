package com.blockchain.store.dao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.mtramin.rxfingerprint.RxFingerprint;

import org.ethereum.geth.Account;
import org.ethereum.geth.Transaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA_RINKEBY;
import static org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction;

public class DaoTokenTransfer extends AppCompatActivity {
    private static final String TAG = "DaoTokenTransfer";

    @BindView(R.id.unlock_account)
    EditText unlock_account;
    @BindView(R.id.unlock_account_btn)
    Button unlock_account_btn;
    @BindView(R.id.withdraw_amount) EditText withdraw_amount;
    @BindView(R.id.button) Button button;

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

    @OnClick(R.id.unlock_account_btn)
    public void unlock_account_btn() {
        Account account = AccountManager.getAccount();
        try {
            AccountManager.getKeyStore().unlock(account, unlock_account.getText().toString());
            Toast.makeText(this, "Account unlocked!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.fingerprint_not_recognized, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.button)
    public void button() {
        RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    Pair<Transaction, Transaction> transaction = CryptoUtils.test111(result, 50);
                    try {
                        String s = transaction.first.encodeJSON();
                        Log.d(TAG, "button: " +s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    long gas = transaction.first.getGas();
                    Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA_RINKEBY));
                    String numericData = Numeric.toHexString(transaction.first.getData());
                    org.web3j.protocol.core.methods.request.Transaction tx = createFunctionCallTransaction(
                            AccountManager.getAddress().getHex(),
                            new BigInteger(String.valueOf(result.count)),
                            new BigInteger(result.getGasPrice()),
                            new BigInteger(String.valueOf(Constants.GAS_LIMIT)),
                            DaoConstants.Repository,
                            numericData);
                    return build.ethEstimateGas(tx).observable();

                })
                .map(this::mapEstimateResult)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    public EthEstimateGas mapEstimateResult(EthEstimateGas result){
        if (result.getError() == null) {
            return result;
        } else {
            throw new RuntimeException(result.getError().getMessage());
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
