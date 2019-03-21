package com.blockchain.store.playmarket.ui.experimental_settings_screen;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.utilities.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IpfsConfigActivity extends BaseActivity {
    private static final String TAG = "IpfsConfigActivity";

    @BindView(R.id.config) TextView config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipfs_config);
        ButterKnife.bind(this);
        RestApi.getServerApi().getPlainConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataReady, this::onDataError);
    }

    private void onDataReady(ResponseBody responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody.string());
            config.setText(json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onDataReady: ");
    }

    private void onDataError(Throwable throwable) {

    }

}
