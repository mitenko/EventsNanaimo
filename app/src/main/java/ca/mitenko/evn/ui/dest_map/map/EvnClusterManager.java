package ca.mitenko.evn.ui.dest_map.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-04-27.
 */

public class EvnClusterManager extends ClusterManager<Destination> {
    /**
     * Custom REW Cluster Renderer
     */
    private EvnClusterRenderer clusterRenderer;

    /**
     * Constructor
     * @param context
     * @param map
     */
    public EvnClusterManager(Context context, GoogleMap map) {
        super(context, map);
        clusterRenderer = new EvnClusterRenderer(context, map, this);
        setRenderer(clusterRenderer);
    }
}
