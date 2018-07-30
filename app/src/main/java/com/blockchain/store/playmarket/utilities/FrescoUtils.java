package com.blockchain.store.playmarket.utilities;

import android.net.Uri;

import com.android.internal.util.Predicate;
import com.bumptech.glide.load.DataSource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;

public class FrescoUtils {

    public void getBitmapFromUri(Uri uri) {
        if (Fresco.getImagePipeline().isInBitmapMemoryCache(uri)) {
            MemoryCache<CacheKey, CloseableImage> bitmapMemoryCache = Fresco.getImagePipeline().getBitmapMemoryCache();
            Predicate<CacheKey> cacheKeyPredicate = predicateForUri(uri);
//            Fresco.getImagePipeline().isInBitmapMemoryCache()
        }
    }

    private Predicate<CacheKey> predicateForUri(final Uri uri) {
        return new Predicate<CacheKey>() {
            @Override
            public boolean apply(CacheKey key) {
                return key.containsUri(uri);
            }
        };
    }
}
