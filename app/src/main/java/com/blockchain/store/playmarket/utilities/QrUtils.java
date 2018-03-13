package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.graphics.Bitmap;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

/**
 * Created by Crypton04 on 13.03.2018.
 */

public class QrUtils {
    public static Bitmap getBitmapQrFromAddress(String address) {
        Context baseContext = Application.getInstance().getBaseContext();
        int avatarHeight = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_HEIGHT);
        int avatarWidth = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_WIDTH);
        return QRCode.from(address).to(ImageType.PNG).withSize(avatarWidth, avatarHeight).bitmap();
    }
}
