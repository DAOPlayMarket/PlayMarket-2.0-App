package com.blockchain.store.playmarket.data.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blockchain.store.playmarket.ui.intro_logo_activity.SplashActivity;
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
public class LocationManager extends LocationCallback implements OnFailureListener {
    private static final String TAG = "LocationManager";

    LocationManagerCallback callback;
    LocationSettingsRequest.Builder builder;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    public void getLocation(Context context, LocationManagerCallback callback) {
        this.callback = callback;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    Log.d(TAG, "last known location:[" + location + "]");
                    if (location != null) {
                        callback.onLocationReady(location);
                    } else {
                        startLocationUpdated(context);
                    }
                });
    }

    private void startLocationUpdated(Context context) {
        builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(locationSettingsResponse ->
                fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(),
                        LocationManager.this, null)).addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    resolvableApiException.startResolutionForResult((Activity) context, SplashActivity.LOCATION_DIALOG_REQUEST);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        Log.d(TAG, "onLocationResult() called with: locationResult = [" + locationResult + "]");
        callback.onLocationReady(locationResult.getLastLocation());
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
        Log.d(TAG, "onLocationAvailability() called with: locationAvailability = [" + locationAvailability + "]");
    }

    private LocationRequest createLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }

        return locationRequest;
    }

    public void stopLocationServices() {
        fusedLocationProviderClient.removeLocationUpdates(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure() called with: e = [" + e + "]");
    }

    public interface LocationManagerCallback {
        void onLocationReady(Location location);
    }
}
