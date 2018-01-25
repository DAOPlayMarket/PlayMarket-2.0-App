package com.blockchain.store.playmarket.utilities.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by samsheff on 12/09/2017.
 */

public class ImageUtils {

    public static Bitmap getBitmapFromBase64(String base64Image) {
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), 0);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}
