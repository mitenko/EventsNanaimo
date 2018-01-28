package ca.mitenko.evn.ui.dest_map.map;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.HashMap;

import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.util.BitmapUtil;

import static ca.mitenko.evn.CategoryConstants.ACCOMMODATION;
import static ca.mitenko.evn.CategoryConstants.ADVENTURE;
import static ca.mitenko.evn.CategoryConstants.BEVERAGES;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.LIFESTYLE;
import static ca.mitenko.evn.CategoryConstants.SERVICE;
import static ca.mitenko.evn.CategoryConstants.SHOPPING;
import static ca.mitenko.evn.CategoryConstants.SIGHT_SEEING;

/**
 * Created by mitenko on 2017-04-27.
 */

public class EvnClusterRenderer extends DefaultClusterRenderer<Destination> {

    /**
     * Marker Anchoring Center CONSTANT
     */
    private final static float CENTER_ANCHOR = 0.5f;

    /**
     * Resize factor for the destination icon inside the background CONSTANT
     */
    private final static float ICON_INSET_FACTOR = 0.8f;

    /**
     * Category Icons
     */
    private Bitmap beverages;
    private Bitmap food;
    private Bitmap shopping;
    private Bitmap sightSeeing;
    private Bitmap service;
    private Bitmap adventure;
    private Bitmap accomodation;
    private Bitmap lifestyle;

    /**
     * Category -> Icon map
     */
    private HashMap<String, Bitmap> catIconMap;

    /**
     * Constructor
     */
    public EvnClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);

        // Load Markers
        beverages = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_beverages);
        food = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_food);
        shopping = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_shopping);
        sightSeeing = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_sight_seeing);
        service = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_service);
        adventure = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_adventure);
        accomodation = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_accommodation);
        lifestyle = BitmapUtil.getBitmapFromVectorDrawable(context, R.drawable.ic_lifestyle);

        // Build the map
        catIconMap = new HashMap<>();
        catIconMap.put(BEVERAGES, beverages);
        catIconMap.put(FOOD, food);
        catIconMap.put(SHOPPING, shopping);
        catIconMap.put(SIGHT_SEEING, sightSeeing);
        catIconMap.put(SERVICE, service);
        catIconMap.put(ADVENTURE, adventure);
        catIconMap.put(ACCOMMODATION, accomodation);
        catIconMap.put(LIFESTYLE, lifestyle);
    }

    /**
     * {@inheritDoc}
     * @param destination
     * @param markerOptions
     */
    @Override
    protected void onBeforeClusterItemRendered(Destination destination,
                                               MarkerOptions markerOptions) {
        // Set-up and inits
        Bitmap destIcon = catIconMap.get(destination.getDisplayIcon());

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(destIcon));
        markerOptions.anchor(CENTER_ANCHOR, CENTER_ANCHOR);
    }
}
