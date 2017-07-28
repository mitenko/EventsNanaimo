package ca.mitenko.evn.ui.dest_map.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.HashMap;

import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;

import static ca.mitenko.evn.CategoryConstants.ACCOMODATION;
import static ca.mitenko.evn.CategoryConstants.ADVENTURE;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.LIFESTYLE;
import static ca.mitenko.evn.CategoryConstants.ON_THE_TOWN;
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
     * Context
     */
    private Context context;

    /**
     * Icon for markers / items
     */
    private Bitmap markerBackground;

    /**
     * Category Icons
     */
    private Bitmap onTheTown;
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
        this.context = context;

        // Load Markers
        markerBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_bg);
        onTheTown = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_on_the_town);
        food = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_food);
        shopping = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_shopping);
        sightSeeing = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_sight_seeing);
        service = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_service);
        adventure = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_adventure);
        accomodation = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_accomodation);
        lifestyle = BitmapFactory.decodeResource(context.getResources(), R.drawable.bm_lifestyle);

        // Build the map
        catIconMap = new HashMap<>();
        catIconMap.put(ON_THE_TOWN, onTheTown);
        catIconMap.put(FOOD, food);
        catIconMap.put(SHOPPING, shopping);
        catIconMap.put(SIGHT_SEEING, sightSeeing);
        catIconMap.put(SERVICE, service);
        catIconMap.put(ADVENTURE, adventure);
        catIconMap.put(ACCOMODATION, accomodation);
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
        Bitmap marker = markerBackground.copy(markerBackground.getConfig(), true);
        Bitmap destIcon = catIconMap.get(destination.detail().activities().get(0).category());
        int bgWidth = marker.getWidth();
        int bgHeight = marker.getHeight();

        int icLeft = (int) (bgWidth - (bgWidth * ICON_INSET_FACTOR)) / 2;
        int icTop = (int)(bgHeight - (bgHeight * ICON_INSET_FACTOR)) / 2;
        int icRight = icLeft + (int)(bgWidth * ICON_INSET_FACTOR);
        int icBottom = icTop + (int)(bgHeight * ICON_INSET_FACTOR);

        // Build a canvas and paint for drawing on the marker
        Bitmap markerIcon = destIcon.copy(destIcon.getConfig(), true);
        Canvas canvas = new Canvas(marker);

        Rect dstRect = new Rect(icLeft, icTop, icRight, icBottom);
        canvas.drawBitmap(markerIcon, null, dstRect, null);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker));
        markerOptions.anchor(CENTER_ANCHOR, CENTER_ANCHOR);
    }

    /**
     * {@inheritDoc}
     * @param cluster
     * @param markerOptions
     */
 /*   @Override
    protected void onBeforeClusterRendered(Cluster<Destination> cluster,
                                           MarkerOptions markerOptions) {

    }*/
}
