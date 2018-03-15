package com.blockchain.store.playmarket.api;

import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Crypton04 on 22.02.2018.
 */

public class ChangellyInterceptor implements Interceptor {
    private static final String TAG = "ChangellyInterceptor";

    private static final String PRIVATE_KEY = "7846abd752eb0779076f95354ce3a034cd4cef7616f936481c30f1c618b86068";
    private static final String PUBLIC_KEY = "ca49bd94e9284dfa8a9f89b72d1fce33";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody body = request.body();
        String sign = "";
        String bodyString = "";
        if (body != null && body.contentType() != null && body.contentType().subtype() != null) {
            if (body.contentType().subtype().contains("json")) {
                bodyString = bodyToString(body);
                sign = buildHmacSignature(bodyString, PRIVATE_KEY);
            }
        }
        Request build = request.newBuilder()
                .addHeader("api-key", PUBLIC_KEY)
                .addHeader("sign", sign)
                .build();
        Response proceed = chain.proceed(build);
        Log.d(TAG, "intercept: ");
        return proceed;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private String buildHmacSignature(String value, String secret) {
        String result;
        try {
            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(),
                    "HmacSHA512");
            hmacSHA512.init(secretKeySpec);

            byte[] digest = hmacSHA512.doFinal(value.getBytes());
            BigInteger hash = new BigInteger(1, digest);
            result = hash.toString(16);
            if ((result.length() % 2) != 0) {
                result = "0" + result;
            }
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException ex) {
            throw new RuntimeException("Problemas calculando HMAC", ex);
        }
        return result;
    }

}
