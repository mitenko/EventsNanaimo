package ca.mitenko.evn.util;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-08-16.
 */

public class LocationUtil {
    /**
     * Converts a LatLng object to a location
     * @param latLng
     * @return
     */
    public static Location fromLatLng(LatLng latLng) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }
    /**
     * Converts a LatLng object to a location
     * @param destination
     * @return
     */
    public static Location fromDestination(Destination destination) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(destination.latitude());
        location.setLongitude(destination.longitude());
        return location;
    }
}
