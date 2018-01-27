package ca.mitenko.evn.model.search;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.ImmutableDestination;
import ca.mitenko.evn.util.LocationUtil;

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestSearch.class)
@Value.Immutable
public class DestSearch {
    private static final LatLngBounds DEFAULT_BOUNDS = new
            LatLngBounds(
            new LatLng(49.15938572687397,-123.9760036021471),
            new LatLng(49.20606374369103,-123.91420517116785));

    /**
     * The search bounds. Default is roughly nanaimo
     * @return
     */
    @NonNull
    @Value.Default
    public LatLngBounds searchBounds() {
        return DEFAULT_BOUNDS;
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
     * Returns true / false if the search
     * has a non-default set of map bounds
     * @return
     */
    @NonNull
    @Value.Lazy
    public boolean hasMapBounds() {
        return mapBounds() != null;
    }

    /**
     * Returns true / false if the map bounds are
     * outside of the search bounds
     * @return
     */
    @NonNull
    @Value.Lazy
    public boolean mapOutsideSearch() {
        if (!hasResults() || mapBounds() == null) {
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
    public ArrayList<Destination> filteredResults(boolean filterByMapBounds) {
        if (!hasResults()) {
            return new ArrayList<>();
        }

        /**
         * Sort the list by nearest to farther if user location is known
         */
        ArrayList<Destination> orderedResults = results();
        if (filter().userLocation() != null) {
            Location userLocation = LocationUtil.fromLatLng(filter().userLocation());

            Collections.sort(orderedResults, new Comparator<Destination>() {
                public int compare(Destination d1, Destination d2) {
                    Location location1 = LocationUtil.fromDestination(d1);
                    Location location2 = LocationUtil.fromDestination(d2);
                    return Integer.signum(
                            (int)location1.distanceTo(userLocation) - (int)location2.distanceTo(userLocation));
                }
            });
        }

        /**
         * Filter by category and / or activity
         */
        if (filter().isEmpty()) {
            return orderedResults;
        }

        /**
         * Otherwise apply the filter
         */
        ArrayList<Destination> filteredResults = new ArrayList<>();
 /*       for(Destination destination : orderedResults) {
            Integer destinationCost = destination.getDetail().getCost();
            for (Activity activity : destination.getDetail().getActivities()) {
                *//**
                 * Filter by Activity / Cost / Category
                 *//*
                if ((filter().categories().isEmpty() || filter().categories().contains(activity.getCategory())) &&
                        (filter().activities().isEmpty() || filter().activities().contains(activity.getName())) &&
                        (filter().cost().isEmpty() || filter().cost().contains(destinationCost))) {
                    filteredResults.add(ImmutableDestination.builder()
                        .from(destination)
                        .displayIcon(activity.category())
                        .build());
                    break;
                }
            }
        }*/
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
