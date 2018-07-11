package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.login_screen.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.permissions_prompt_activity.PermissionsPromptActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.TransactionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthCompileSolidity;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.exception.SmartContractException;
import io.ethmobile.ethdroid.solidity.ContractType;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.utilities.Constants.NODE_ADDRESS;

public class SplashActivity extends AppCompatActivity implements SplashContracts.View {
    private static final String TAG = "SplashActivity";
    private static final int SplashDisplayLength = 500;
    private static final int PERMISSION_REQUEST_CODE = 101;
    public static final int LOCATION_DIALOG_REQUEST = 102;

    @BindView(R.id.LogoTextView) TextView logoTextView;
    @BindView(R.id.LogoVideoView) VideoView logoVideoView;
    @BindView(R.id.network_status) TextView networkStatus;
    @BindView(R.id.error_holder) LinearLayout errorHolder;

    private SplashPresenter presenter;
    private String errorString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_logo);
        ButterKnife.bind(this);
        test();
//        presenter = new SplashPresenter();
//        presenter.init(this);
//        setLogoTextFont();
//        setupAndPlayVideo();
//        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (PermissionUtils.isLocationPermissionGranted(this)) {
            presenter.requestUserLocation(this);
        } else {
            PermissionUtils.verifyLocationPermissions(this, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.requestUserLocation(this);
        } else {
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_DIALOG_REQUEST) {
            if (resultCode == RESULT_OK) {
                presenter.requestUserLocation(this);
            } else if (resultCode == RESULT_CANCELED) {
                presenter.requestUserLocation(this);
            }
        }
    }

    protected void setLogoTextFont() {
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/roboto.ttf");
        logoTextView.setTypeface(tf);
    }

    protected void setupAndPlayVideo() {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.image;
        logoVideoView.setVideoURI(Uri.parse(path));
        logoVideoView.setOnPreparedListener(mp -> mp.setLooping(true));
        logoVideoView.start();
    }

    protected void openLoginPromptActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent myIntent;
            if (PermissionUtils.storagePermissionGranted(this)) {
                if (AccountManager.isHasUsers()) {
                    myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                } else {
                    myIntent = new Intent(getApplicationContext(), LoginPromptActivity.class);
                }
            } else {
                myIntent = new Intent(getApplicationContext(), PermissionsPromptActivity.class);
            }
            logoVideoView.stopPlayback();

            startActivity(myIntent);
            this.finish();
        }, SplashDisplayLength);
    }


    @Override
    public void onLocationReady() {
        openLoginPromptActivity();
    }

    @Override
    public void setStatusText(@StringRes int text) {
        networkStatus.setText(text);
    }

    @Override
    public void setStatusText(int stringRes, String errorString) {
        this.errorString = errorString;
        networkStatus.setText(getString(stringRes));

    }

    @OnClick(R.id.network_status)
    void onNetworkStatusClicked() {
        if (errorString != null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage(errorString).create();
            alertDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            alertDialog.show();
        }
    }

    @Override
    public void setStatusText(String text) {
        networkStatus.setText(text);
    }

    @Override
    public void onNearestNodeFailed(Throwable throwable) {
        errorHolder.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        errorHolder.setVisibility(View.GONE);
        presenter.requestUserLocation(this);

    }

    private void test() {
        ArrayList<Type> arrayList = new ArrayList<>();
        arrayList.add(new Uint(new BigInteger("1")));
        arrayList.add(new Address("0xa10E1b2255d3EC6d0fc379518C579a5f3caa9c42"));


        List<TypeReference<?>> typeReferences = Arrays.<TypeReference<?>>asList(new TypeReference<org.web3j.abi.datatypes.Uint>() {
                                                                                },
                new TypeReference<org.web3j.abi.datatypes.Address>() {
                });

        Function function = new Function("buyApp",
                arrayList, typeReferences);

        String encode = FunctionEncoder.encode(function);
        Log.d("test", "generateAppBuyTransaction: " + encode);

        byte[] oldMehod = CryptoUtils.getDataForBuyApp("1", "a10E1b2255d3EC6d0fc379518C579a5f3caa9c42");
        Log.d(TAG, "old method: " + oldMehod);


//        web.ethGetTransactionByHash("0x8c8f13aa8d227b1cd0b07896106841bba618c60e17180c433f9c640016da5c2b").observable().observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::onTestOk, this::onTestError);
    }

    private void onTestOk(EthTransaction ethTransaction) {
        Log.d(TAG, "onTestOk() called with: ethTransaction = [" + ethTransaction + "]");
    }

    private void onTestOk(Web3ClientVersion web3ClientVersion) {
        Log.d(TAG, "onTestOk() called with: web3ClientVersion = [" + web3ClientVersion + "]");
    }

    private void onTestError(Throwable throwable) {
        Log.d(TAG, "onTestError() called with: throwable = [" + throwable + "]");
    }
}

