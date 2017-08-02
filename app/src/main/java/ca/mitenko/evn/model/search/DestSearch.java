package ca.mitenko.evn.model.search;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import javax.annotation.Nullable;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.ImmutableDestination;

import static android.R.attr.filter;

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestSearch.class)
@Value.Immutable
public class DestSearch {
    /**
     * The search bounds. Default is roughly nanaimo
     * @return
     */
    @NonNull
    @Value.Default
    public LatLngBounds searchBounds() {
        return new
                LatLngBounds(
                new LatLng(49.15938572687397,-123.9760036021471),
                new LatLng(49.20606374369103,-123.91420517116785));
    }

    /**
     * The map bounds. Default is roughly nanaimo
     * @return
     */
    @Nullable
    @Value.Default
    public LatLngBounds mapBounds() {
        return null;
    }

    /**
     * The map bounds. Default is roughly nanaimo
     * @return
     */
    @NonNull
    @Value.Lazy
    public LatLngBounds mapBoundsOrDefault() {
        if (mapBounds() != null) {
            return mapBounds();
        }
        return searchBounds();
    }

    /**
     * Returns true / false if the map bounds are
     * outside of the search bounds
     * @return
     */
    @NonNull
    @Value.Lazy
    public boolean mapOutsideSearch() {
        if (mapBounds() == null) {
            return false;
        }
        return !searchBounds().contains(mapBounds().northeast)
                || !searchBounds().contains(mapBounds().southwest);
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

        /**
         * Filter first by the map bounds
         */
        ArrayList<Destination> mapFiltered = new ArrayList<>();
        if (mapBounds() != null) {
            for(Destination destination : results()) {
                if (mapBounds().contains(destination.getPosition())) {
                    mapFiltered.add(destination);
                }
            }
        } else {
            mapFiltered.addAll(results());
        }

        /**
         * Second filter is by category and / or activity
         */
        if (filter().isEmpty()) {
            return mapFiltered;
        }

        /**
         * Otherwise apply the filter
         */
        ArrayList<Destination> filteredResults = new ArrayList<>();
        for(Destination destination : mapFiltered) {
            for (Activity activity : destination.detail().activities()) {
                /**
                 * Check category first
                 */
                if (filter().categories().contains(activity.category()) ||
                        filter().activities().contains(activity.name())) {
                    filteredResults.add(ImmutableDestination.builder()
                        .from(destination)
                        .displayIcon(activity.category())
                        .build());
                    break;
                }
            }
        }
        return filteredResults;
    }

    /**
     * Parcel Factory
     * @return
     */
    @ParcelFactory
    public static DestSearch build(Filter filter, LatLngBounds searchBounds, LatLngBounds mapBounds,
                                   ArrayList<Destination> results) {
        return ImmutableDestSearch.builder()
                .searchBounds(searchBounds)
                .mapBounds(mapBounds)
                .filter(filter)
                .results(results)
                .build();
    }
}
