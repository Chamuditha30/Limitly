package com.s22010695.limitly.mode_helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.s22010695.limitly.db_helpers.FocusModeTableHandler;
import com.s22010695.limitly.db_helpers.ZoneInfoModel;
import com.s22010695.limitly.db_helpers.ZonesTableHandler;

import java.util.List;

public class FocusModeHelper {
    //declare objects
    private final Context context;
    private final FocusModeTableHandler focusHelper;
    private final ZonesTableHandler zonesHelper;
    private final FusedLocationProviderClient locationClient;

    public FocusModeHelper(Context context){
        this.context = context;
        this.focusHelper = new FocusModeTableHandler(context);
        this.zonesHelper = new ZonesTableHandler(context);
        this.locationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    //create method for get timer mode is enable or not
    public boolean isEnable(){
        return focusHelper.getIsEnable();
    }

    //create method to start checking if user is in any zone
    @SuppressLint("MissingPermission")
    public void apply(FocusZoneCallback callback) {
        //request one high accuracy location update
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(1000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLastLocation();
                    Log.d("FocusMode", "Got location: " + location);
                    boolean inZone = isWithinZone(location.getLatitude(), location.getLongitude());
                    callback.onResult(inZone);
                } else {
                    Log.d("FocusMode", "No location result");
                    callback.onResult(false);
                }
                locationClient.removeLocationUpdates(this);
            }
        };

        //stop location track if location never arrives
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d("FocusMode", "Timeout waiting for location");
            callback.onResult(false);
            locationClient.removeLocationUpdates(locationCallback);
        }, 5000);

        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    // Compare current location with all zone locations
    private boolean isWithinZone(double userLat, double userLng) {
        List<ZoneInfoModel> zoneList = zonesHelper.getAllZones();
        Location userLocation = new Location("User");
        userLocation.setLatitude(userLat);
        userLocation.setLongitude(userLng);

        for (ZoneInfoModel zone : zoneList) {
            Location zoneLocation = new Location("Zone");
            zoneLocation.setLatitude(zone.getLatitude());
            zoneLocation.setLongitude(zone.getLongitude());

            float distanceInMeters = userLocation.distanceTo(zoneLocation);

            //check 50m radius
            if (distanceInMeters <= 50) {
                return true;
            }
        }
        return false;
    }

    //create interface to return result asynchronously
    public interface FocusZoneCallback {
        void onResult(boolean isInZone);
    }
}
