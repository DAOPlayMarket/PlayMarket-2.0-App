package com.blockchain.store.playmarket.dapps;

import android.net.Uri;

public interface UrlHandler {

    String getScheme();

    String handle(Uri uri);
}