package ca.mitenko.evn.ui.dest_map.map;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ca.mitenko.evn.R;
import ca.mitenko.evn.event.MapBoundsEvent;
import ca.mitenko.evn.event.MapItemClickEvent;
import ca.mitenko.evn.event.MapReadyEvent;
import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-04-22.
 */

public class DestMap extends MapView
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener,
        ClusterManager.OnClusterItemClickListener<Destination>{

    /**
     * The Cluster Manager
     */
    protected EvnClusterManager clusterManager;

    /**
     * Google Map
     */
    protected GoogleMap map;

    /**
     * The Event Bus
     */
    private EventBus bus;

    /**
     * Constructor
     * @param context
     */
    public DestMap(Context context) {
        super(context);
        getMapAsync(this);
    }

    /**
     * Constructor
     * @param context
     * @param attributeSet
     */
    public DestMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getMapAsync(this);
    }

    /**
     * Constructor
     * @param context
     * @param attributeSet
     * @param i
     */
    public DestMap(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getMapAsync(this);
    }

    /**
     * Setters and Getters
     */
    /**
     * Sets the Event Bus
     * @param bus
     */
    public void setBus(EventBus bus) {
        this.bus = bus;
    }

    /**
     * Map Callbacks
     */
    /**
     * Default On Map Ready Callback
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        // Register some lifecycle and camera listeners
        map.setOnMapLoadedCallback(this);
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveStartedListener(this);

        // Initialize the Cluster Manager
        clusterManager = new EvnClusterManager(getContext(), map);
        map.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterItemClickListener(this);

        // Move to a default position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(49.1827304, -123.9451047), 13));

        // Let the presenter know
        bus.post(new MapReadyEvent());
    }

    /**
     * Default On Map Loaded Callback
     */
    @Override
    public void onMapLoaded() {}

    /**
     * Fragment Callbacks
     */
    public void setDestinationResult(ArrayList<Destination> destinations) {
        clusterManager.clearItems();
        clusterManager.addItems(destinations);
        clusterManager.cluster();
    }

    /**
     * Called so the map can recluster
     */
    public void recluster() {
        clusterManager.cluster();
    }

    /**
     * Camera / Movement Methods
     */
    /**
     * {@inheritDoc}
     * Called when the camera starts moving after it has been idle
     * or when the reason for camera motion has changed.
     */
    public void onCameraMoveStarted(int reason) {
        bus.post(new MapItemClickEvent(null));
    }

    /**
     * {@inheritDoc}
     * Called when camera movement has ended, there are no pending
     * animations and the user has stopped interacting with the map.
     */
    public void onCameraIdle() {
        LatLngBounds newMapBounds = map.getProjection().getVisibleRegion().latLngBounds;
        bus.post(new MapBoundsEvent(newMapBounds));
    }

    /**
     * Cluster Manager Methods
     */

    /**
     * Called when a cluster item is clicked
     * @param destination
     * @return
     */
    public boolean onClusterItemClick(Destination destination) {
        bus.post(new MapItemClickEvent(destination));
        return true;
    }

    /**
     * Required MapView Lifecycle Implementations
     */
    /**
     * On Map Create
     * @param bundle
     */
    public void onMapCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /**
     * On Map Start
     */
    public void onMapStart() {
        super.onStart();
    }

    /**
     * On Map Resume
     */
    public void onMapResume() {
        super.onResume();
    }

    /**
     * On Map Pause
     */
    public void onMapPause() {
        super.onPause();
    }

    /**
     * On Map Stop
     */
    public void onMapStop() {
        super.onStop();
    }

    /**
     * On Map Destroy
     */
    public void onMapDestroy() {
        super.onDestroy();
    }

    /**
     * On Map Low Memory
     */
    public void onMapLowMemory() {
        super.onLowMemory();
    }

    /**
     * On Map Save Instance State
     * @param bundle
     */
    public void onMapSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
