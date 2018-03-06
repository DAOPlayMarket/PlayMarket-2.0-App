package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.login_prompt_activity.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.permissions_prompt_activity.PermissionsPromptActivity;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements SplashContracts.View {
    private static final String TAG = "SplashActivity";
    private static final int SplashDisplayLength = 500; //todo set to 5s when
    private static final int LOCATION_FOUND_TIMEOUT_MILLIS = 10000; //
    private static final int PERMISSION_REQUEST_CODE = 101;

    public static final int LOCATION_DIALOG_REQUEST = 102;

    @BindView(R.id.LogoTextView) TextView logoTextView;
    @BindView(R.id.LogoVideoView) VideoView logoVideoView;
    @BindView(R.id.continue_without_location) Button continueWithoutLocation;
    @BindView(R.id.network_status) TextView networkStatus;

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_logo);
        ButterKnife.bind(this);
        presenter = new SplashPresenter();
        presenter.init(this);
        setLogoTextFont();
        setupAndPlayVideo();
        checkLocationPermission();
        startLocationTimeout();
    }

    private void startLocationTimeout() {
//        final Handler handler = new Handler();
//        handler.postDelayed(() -> {
//            continueWithoutLocation.setVisibility(View.VISIBLE);
//        }, LOCATION_FOUND_TIMEOUT_MILLIS);
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
            //todo SHOW DIALOG WTF?
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

    protected void loadLoginPromptActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent myIntent;
            if (PermissionUtils.storagePermissionGranted(this)) {
                myIntent = new Intent(getApplicationContext(), LoginPromptActivity.class);
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
        loadLoginPromptActivity();
    }

    @Override
    public void setStatusText(@StringRes int text) {
        networkStatus.setText(text);
    }

    @Override
    public void setStatusText(String text) {
        networkStatus.setText(text);
    }

    @OnClick(R.id.continue_without_location)
    void onLocationTimeoutClicked() {
        loadLoginPromptActivity();
    }
}

