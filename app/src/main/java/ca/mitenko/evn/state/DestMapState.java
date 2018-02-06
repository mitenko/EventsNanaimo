package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestMapState.class)
@Value.Immutable
public class DestMapState extends RootState {
    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.dest_map";

    /**
     * Thrown Error
     */
    @Nullable
    @Value.Default
    public Throwable error() {
        return null;
    }

    /**
     * Loading results flag
     */
    @Value.Default
    public boolean loadingResults() {
        return false;
    }

    /**
     * Permission to use the user's location
     */
    @NonNull
    @Value.Default
    public boolean hasUserLocationPermission() {
        return true;
    }

    /**
     * The user's location
     */
    @Nullable
    @Value.Default
    public LatLng userLocation() {
        return null;
    }

    /**
     * Map loaded state flag
     */
    @NonNull
    @Value.Default
    public DestSearch search() {
        return new DestSearch();
    }

    /**
     * Map loaded state flag
     */
    @Value.Default
    public boolean mapReady() {
        return false;
    }

    /**
     * Flag indicating map needs to recluster
     */
    @Value.Default
    public boolean recluster() {
        return false;
    }

    /**
     * Destinations loaded state flag
     */
    @Nullable
    @Value.Default
    public Destination selectedItem() {
        return null;
    }

    /**
     * The set of category data
     */
    @Nullable
    @Value.Default
    public HashMap<String, ArrayList<Activity>> categoryMap() {
        return null;
    }

    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestMapState build(boolean mapReady, boolean recluster, boolean loadingResults,
                                     DestSearch search, Destination selectedItem,
                                     HashMap<String, ArrayList<Activity>> categoryMap,
                                     LatLng userLocation, boolean hasUserLocationPermission) {
        return ImmutableDestMapState.builder()
                .loadingResults(loadingResults)
                .recluster(recluster)
                .mapReady(mapReady)
                .search(search)
                .selectedItem(selectedItem)
                .categoryMap(categoryMap)
                .userLocation(userLocation)
                .hasUserLocationPermission(hasUserLocationPermission)
                .build();
    }
}
