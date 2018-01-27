package ca.mitenko.evn.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;

import java.util.Collection;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-07-28.
 */

public class MapUtil {

    /**
     * Northeast Heading in degrees CONSTANT
     */
    private final static double NORTHEAST_HEADING = 45d;
    /**
     * Southwest Heading in degrees CONSTANT
     */
    private final static double SOUTHWEST_HEADING = -135d;

    /**
     * Minimum distance to extend the cluster bounds in meters CONSTANT
     */
    private final static double MINIMUM_BOUNDS_EXTENSION = 20;

    /**
     * Fraction of the current bounds by which to extend the bounds during a cluster zoom
     */
    private final static double BOUNDS_EXTENSION_FRACTION = 0.5d;

    /**
     * Extends the map boundaries by an amount determined
     * as a fraction of the total boundary size
     * @param bounds
     * @return
     */
    public static LatLngBounds extendBounds(LatLngBounds bounds) {
        // Extend the bounds so the search won't drop edge listings
        // Calculate the padding to put in
        // as a fraction of the distance between the boundary points
        LatLng northEastBound = bounds.northeast;
        LatLng southWestBound = bounds.southwest;
        float distance[] = new float[2];
        Location.distanceBetween(
                northEastBound.latitude, northEastBound.longitude,
                southWestBound.latitude, southWestBound.longitude, distance);
        float boundsDiagonalDistance = distance[0];

        // Calculate the padding as a fraction of the bound's diagonal distance
        double paddingDistance = boundsDiagonalDistance * BOUNDS_EXTENSION_FRACTION < MINIMUM_BOUNDS_EXTENSION ?
                MINIMUM_BOUNDS_EXTENSION :
                boundsDiagonalDistance * BOUNDS_EXTENSION_FRACTION;

        // Calculate the extended LatLng points
        LatLng extendedNorthEast = SphericalUtil.computeOffset(northEastBound, paddingDistance, NORTHEAST_HEADING);
        LatLng extendedSouthWest = SphericalUtil.computeOffset(southWestBound, paddingDistance, SOUTHWEST_HEADING);

        // Make the extended bounds
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(extendedNorthEast);
        builder.include(extendedSouthWest);
        return builder.build();
    }

    /**
     * Will build a LatLngBounds based on the items within the cluster
     * @param cluster
     * @return
     */
    public static LatLngBounds getClusterBounds(Cluster cluster) {
        Collection<Destination> destinations = cluster.getItems();

        if (destinations == null || destinations.size() == 0) {
            return null;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Destination destination: destinations) {
            builder.include(destination.getPosition());
        }
        return builder.build();
    }

    /**
     * Will use the destination's data to build a static map query
     * @param destination
     * @return
     */
    public static String toStaticMapURL(Destination destination) {
        return "https://maps.googleapis.com/maps/api/staticmap?"
            + "center=" + destination.latitude() + "," + destination.longitude()
            + "&zoom=16&size=640x400"
            + "&key=AIzaSyASFlFrUY2m055zls177YGZ41N2douQx2Q";
    }
}
