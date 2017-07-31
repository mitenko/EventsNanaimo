package ca.mitenko.evn.util;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;

import java.util.Collection;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-07-28.
 */

public class MapUtil {
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
}
