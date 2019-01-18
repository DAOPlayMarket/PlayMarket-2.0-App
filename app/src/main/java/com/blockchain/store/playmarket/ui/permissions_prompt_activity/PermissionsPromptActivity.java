package com.blockchain.store.playmarket.ui.permissions_prompt_activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.login_screen.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

public class PermissionsPromptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_prompt);
    }

    public void requestPermissions(View view) {
        if (PermissionUtils.storagePermissionGranted(this)) {
            if (AccountManager.isHasUsers()) {
                openMainMenuActivity();
            } else {
                openLoginPromptActivity();
            }
        }

        PermissionUtils.verifyStoragePermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (AccountManager.isHasUsers()) {
                openMainMenuActivity();
            } else {
                openLoginPromptActivity();
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    public void openLoginPromptActivity() {
        startActivity(new Intent(getApplicationContext(), LoginPromptActivity.class));

        finish();
    }

    public void openMainMenuActivity() {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }
}
