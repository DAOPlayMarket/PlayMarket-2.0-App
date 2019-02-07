package com.blockchain.store.playmarket.ui.qr_screen;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.BaseActivity;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrActivity extends BaseActivity {
    @BindView(R.id.qrCode) ImageView qrCode;
    @BindView(R.id.user_address) TextView userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);
        String userAddress = AccountManager.getAddress().getHex();
        this.userAddress.setText(userAddress);
        qrCode.setImageBitmap(getBitmapFromAddress(userAddress));
    }

    @OnClick(R.id.back_button) void onBackClicked() {
        this.finish();
    }

    private Bitmap getBitmapFromAddress(String address) {
        int avatarWidth = (int) getResources().getDimension(R.dimen.QR_AVATAR_SIZE_LARGE);
        return QRCode.from(address).to(ImageType.PNG).withSize(avatarWidth, avatarWidth).bitmap();
    }


}
