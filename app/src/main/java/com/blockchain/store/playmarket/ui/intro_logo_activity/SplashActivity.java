package com.blockchain.store.playmarket.ui.intro_logo_activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.VideoView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.login_prompt_activity.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.permissions_prompt_activity.PermissionsPromptActivity;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements IntroLogoContracts.View {
    private static final String TAG = "SplashActivity";
    private static final int SplashDisplayLength = 5000;

    @BindView(R.id.LogoTextView) TextView logoTextView;
    @BindView(R.id.LogoVideoView) VideoView logoVideoView;

    private IntroLogoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_logo);
        ButterKnife.bind(this);
        presenter = new IntroLogoPresenter();
        presenter.init(this);
        presenter.getNearestNodes();
        setLogoTextFont();
        setupAndPlayVideo();
        loadLoginPromptActivity();
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
            startActivity(myIntent);
            this.finish();
        }, SplashDisplayLength);
    }


}

