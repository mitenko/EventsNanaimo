package ca.mitenko.evn.model.search;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import javax.annotation.Nullable;

import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestSearch.class)
@Value.Immutable
public class DestSearch {
    /**
     * The map bounds to search by. Default is roughly nanaimo
     * @return
     */
    @NonNull
    @Value.Default
    public LatLngBounds bounds() {
        return new
                LatLngBounds(
                new LatLng(49.15938572687397,-123.9760036021471),
                new LatLng(49.20606374369103,-123.91420517116785));
    }

    /**
     * The filter to apply to the results
     * @return
     */
    @NonNull
    @Value.Default
    public Filter filter() {
        return ImmutableFilter.builder().build();
    }

    /**
     * The raw result set
     */
    @Nullable
    @Value.Default
    public ArrayList<Destination> results() {
        return null;
    }

    /**
     * The raw result set
     */
    @NonNull
    @Value.Lazy
    public boolean hasResults() {
        return results() != null;
    }

    /**
     * The filtered set of results
     */
    @NonNull
    @Value.Lazy
    @SuppressWarnings("ConstantConditions")
    public ArrayList<Destination> filteredResults() {
        if (!hasResults()) {
            return new ArrayList<>();
        }
        return results();
    }

    /**
     * Parcel Factory
     * @return
     */
    @ParcelFactory
    public static DestSearch build(Filter filter, LatLngBounds bounds,
                                   ArrayList<Destination> results) {
        return ImmutableDestSearch.builder()
                .bounds(bounds)
                .filter(filter)
                .results(results)
                .build();
    }
}
