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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DaoTokenTransfer extends AppCompatActivity {


    @BindView(R.id.unlock_account)
    EditText unlock_account;
    @BindView(R.id.unlock_account_btn)
    Button unlock_account_btn;
    @BindView(R.id.withdraw_amount) EditText withdraw_amount;
    @BindView(R.id.button) Button button;
    private DaoToken daoToken;

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
        daoToken = getIntent().getParcelableExtra("abc");
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
                    String rawTx = CryptoUtils.generateWithdrawTx(result, daoToken.address, Long.parseLong(withdraw_amount.getText().toString()));
                    return RestApi.getServerApi().deployTransaction(rawTx);

                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::transferSuccess, this::transferFailed);
    }

    private void transferFailed(Throwable t) {

    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {

    }


}
