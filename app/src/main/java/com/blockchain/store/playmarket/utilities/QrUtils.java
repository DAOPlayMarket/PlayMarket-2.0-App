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

    public enum QR_SIZE {
        SMALL, LARGE
    }

    public static Bitmap getBitmapQrFromAddress(String address, QR_SIZE qrSize) {
        Context baseContext = Application.getInstance().getBaseContext();
        int avatarHeight = 0;
        int avatarWidth = 0;
        if (qrSize == QR_SIZE.SMALL) {
            avatarHeight = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_SIZE_SMALL);
            avatarWidth = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_SIZE_SMALL);
        }
        if (qrSize == QR_SIZE.LARGE) {
            avatarHeight = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_SIZE_LARGE);
            avatarWidth = (int) baseContext.getResources().getDimension(R.dimen.QR_AVATAR_SIZE_LARGE);
        }
        if (avatarHeight == 0 && avatarWidth == 0) {
            return QRCode.from(address).to(ImageType.PNG).bitmap();
        }
        return QRCode.from(address).to(ImageType.PNG).withSize(avatarWidth, avatarHeight).bitmap();
    }
}
