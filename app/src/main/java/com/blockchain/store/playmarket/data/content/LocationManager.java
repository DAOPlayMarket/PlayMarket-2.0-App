package com.blockchain.store.playmarket.data.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Crypton04 on 15.02.2018.
 */
@SuppressLint("MissingPermission")
public class LocationManager {
    private static final String TAG = "LocationManager";

    LocationManagerCallback callback;
    LocationSettingsRequest.Builder builder;
    FusedLocationProviderClient fusedLocationProviderClient;

    public void getLocation(Context context, LocationManagerCallback callback) {
        this.callback = callback;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReady(location);
                    } else {
                        startLocationUpdated(context);
                        //start location service
                    }

                });
    }

    private void startLocationUpdated(Context context) {
        builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Log.d(TAG, "onLocationResult() called with: locationResult = [" + locationResult.getLastLocation().toString() + "]");
                        callback.onLocationReady(locationResult.getLastLocation());
                    }
                }, null);
            }
        }).addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try{
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    resolvableApiException.startResolutionForResult((Activity) context,102);

                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });

    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    public interface LocationManagerCallback {
        void onLocationReady(Location location);
    }
}
