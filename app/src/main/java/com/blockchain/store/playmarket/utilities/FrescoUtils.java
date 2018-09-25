package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.graphics.Palette;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class FrescoUtils {

    public static Observable<Bitmap> getBitmapDataSource(Context context, String imageUrl) {
        return new Observable<Bitmap>() {

            @Override
            protected void subscribeActual(Observer<? super Bitmap> observer) {
                ImagePipeline imagePipeline = Fresco.getImagePipeline();

                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(imageUrl))
                        .setRequestPriority(Priority.HIGH)
                        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                        .build();
                imagePipeline.fetchDecodedImage(imageRequest, context).subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        observer.onNext(bitmap);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        observer.onError(dataSource.getFailureCause());

                    }
                }, CallerThreadExecutor.getInstance());
            }
        };
    }

    public static Observable<Palette> getPalleteFromBitemap(Bitmap bitmap) {
        return new Observable<Palette>() {
            @Override
            protected void subscribeActual(Observer<? super Palette> observer) {
                Palette generate = Palette.from(bitmap).generate();
                observer.onNext(generate);
            }
        };
    }

}
