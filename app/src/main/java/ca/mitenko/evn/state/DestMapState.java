package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLngBounds;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.ImmutableDestSearch;
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
    @NonNull
    @Value.Default
    public DestSearch search() {
        return ImmutableDestSearch.builder().build();
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
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestMapState build(boolean mapReady, boolean recluster, boolean loadingResults,
                                     DestSearch search, Destination selectedItem) {
        return ImmutableDestMapState.builder()
                .loadingResults(loadingResults)
                .recluster(recluster)
                .mapReady(mapReady)
                .search(search)
                .selectedItem(selectedItem)
                .build();
    }
}
