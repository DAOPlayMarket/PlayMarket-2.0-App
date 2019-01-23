package com.blockchain.store.dao.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.orhanobut.hawk.Hawk;

import org.ethereum.geth.Account;
import org.ethereum.geth.Transaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.http.HttpService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

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
            RxFingerprint.decrypt(this, Hawk.get(Constants.ENCRYPTED_PASSWORD))
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
                    Transaction transaction = CryptoUtils.test111(result, 10000L);
                    Web3j build = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
                    return build.ethEstimateGas(createEthCallTransaction(AccountManager.getAddress().getHex(),DaoConstants.Repository,new String(transaction.getData()))).observable();

                })
                .map(result -> result)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferSuccess(EthEstimateGas ethEstimateGas) {
        Log.d(TAG, "transferSuccess: ");
    }

    private void transferFailed(Throwable t) {
        Log.d(TAG, "transferFailed: ");
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d(TAG, "transferSuccess: ");
    }


}
