package ca.mitenko.evn.state;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLngBounds;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Search;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestMapState.class)
@Value.Immutable
public class DestMapState extends RootState {
    /**
     * Loading results flag
     */
    @Value.Default
    public boolean loadingResults() {
        return false;
    }

    /**
     * Map loaded state flag
     */
    @Nullable
    @Value.Default
    public Search search() {
        return new Search();
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
     * The Map Bounds
     */
    @Nullable
    @Value.Default
    public LatLngBounds mapBounds() { return null; }

    /**
     * Destinations loaded state flag
     */
    @Nullable
    @Value.Default
    public ArrayList<Destination> destinations() {
        return null;
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
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestMapState build(boolean mapReady, ArrayList<Destination> destinations,
                                     LatLngBounds mapBounds, Destination selectedItem) {
        return ImmutableDestMapState.builder()
                .mapReady(mapReady)
                .destinations(destinations)
                .mapBounds(mapBounds)
                .selectedItem(selectedItem)
                .build();
    }
}
