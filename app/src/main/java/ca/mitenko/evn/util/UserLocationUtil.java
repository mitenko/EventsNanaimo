package ca.mitenko.evn.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import ca.mitenko.evn.Manifest;
import ca.mitenko.evn.event.UserLocationEvent;
import ca.mitenko.evn.event.UserLocationEvent.Outcome;


/**
 * Created by mitenko on 2017-04-22.
 */

public class UserLocationUtil implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    /**
     * Context object
     */
    private final Context context;

    /**
     * Google API Client
     */
    private GoogleApiClient googleApiClient;

    /**
     * Event bus
     */
    private EventBus bus;

    /**
     * Request timeout CONSTANT
     */
    private static final int LOCATION_REQUEST_TIMEOUT = 10000;

    /**
     * Location request interval
     */
    private static final int LOCATION_REQUEST_INTERVAL = 1000;

    /**
     * Constructor
     * Takes a CurrentLocationRequestCallback to return the location
     * Initiates the location request
     * @param _context
     */
    public UserLocationUtil(@NonNull Context _context, EventBus bus) {
        this.context = _context;
        this.bus = bus;
        googleApiClient = null;
    }

    /**
     * Returns true / false based on if the app has permissions granted
     */
    public boolean hasAccessLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED;
    }

    /**
     * Initiates the process to determine current location
     */
    public void getLocation() {
        if (!hasAccessLocationPermission()) {
            bus.postSticky(new UserLocationEvent(Outcome.DENIED, null));
            return;
        }

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    /**
     * GoogleApiClient connection callback
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            // Check if we can access the location data
            LocationManager locationManager = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // Now that we're connected make a call to getLastLocation
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                // Call the listener
                if (location != null) {
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    bus.postSticky(new UserLocationEvent(Outcome.SUCCESS, userLatLng));
                } else {
                    // In the rare case that we receive a null location,
                    // ping the service until something comes up
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                            createLocationRequest(), this);
                }
            } else {
                bus.postSticky(new UserLocationEvent(Outcome.FAILED, null));
            }
        } catch (SecurityException e) {
            bus.postSticky(new UserLocationEvent(Outcome.FAILED, null));
        }
    }

    /**
     * LocationRequest callback
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        // Stop location updates
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        bus.postSticky(new UserLocationEvent(Outcome.SUCCESS, userLatLng));
    }

    /**
     * onConnectionSuspended callback
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause){
        // Don't panic: the GoogleApiClient will attempt to reconnect
    }

    /**
     * onConnectionFailed callback
     * @param result
     */
    @Override
    public void onConnectionFailed (ConnectionResult result){
        bus.postSticky(new UserLocationEvent(Outcome.FAILED, null));
    }

    /**
     * Creates and returns a location request with the configuration initialized
     * @return
     */
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setExpirationDuration(LOCATION_REQUEST_TIMEOUT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }
}
