package ca.mitenko.evn.util;


import com.google.android.gms.maps.model.LatLng;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2018-04-28.
 */
public class LatLngUtil {
    /**
     * Converts a LatLng object to a location
     * @param dest
     * @return
     */
    public static LatLng fromDestination(Destination dest) {
        return new LatLng(dest.getLatitude(), dest.getLongitude());
    }
}
