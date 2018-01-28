package ca.mitenko.evn.model.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.model.Activity;

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableFilter.class)
@Value.Immutable
public class Filter {

    /**
     * The categories to filter by
     * @return
     */
    @NonNull
    @Value.Default
    public ArrayList<String> categories() {
        return new ArrayList<>();
    }

    /**
     * The activities to filter by
     * @return
     */
    @NonNull
    @Value.Default
    public HashSet<String> activities() {
        return new HashSet<>();
    }

    /**
     * The cost to filter by
     */
    @NonNull
    @Value.Default
    public ArrayList<Integer> cost() {
        return new ArrayList<>();
    }

    /**
     * User Location to sort the list
     */
    @Nullable
    @Value.Default
    public LatLng userLocation() {
        return null;
    }

    /**
     * Returns true / false if the filter is empty
     * @return
     */
    @Value.Lazy
    public boolean isEmpty() {
        return activities().isEmpty() && categories().isEmpty()
                && cost().isEmpty() && userLocation() == null;
    }

    /**
     * Modifies the current filter based on the filter event
     * @param event
     * @return
     */
    public Filter modify(ModifyFilterEvent event, HashMap<String, ArrayList<Activity>> categoryMap) {
        /**
         * Deep copy the current activities and categories
         */
        ArrayList<String> categories = new ArrayList<>();
        categories.addAll(categories());
        HashSet<String> activities = new HashSet<>();
        activities.addAll(activities());
        ArrayList<Integer> costs = new ArrayList<>();
        costs.addAll(cost());

        ModifyFilterEvent.Type type = event.getType();

        if (event.getAction() == ModifyFilterEvent.Action.TOGGLE) {

            if (type == ModifyFilterEvent.Type.CATEGORY) {
                if (categories.contains(event.getValue())) {
                    categories.remove(event.getValue());
                } else {
                    categories.add(event.getValue());
                }

                /**
                 * To properly add activities after a category toggle,
                 * you need to completely redo them
                 */
                activities.clear();
                for(String categoryName : categories) {
                    ArrayList<Activity> catActivities = categoryMap.get(categoryName);
                    for(Activity activity : catActivities) {
                        activities.add(activity.getName());
                    }
                }

            } else if (type == ModifyFilterEvent.Type.ACTIVITY) {
                if (activities.contains(event.getValue())) {
                    activities.remove(event.getValue());
                } else {
                    activities.add(event.getValue());
                }
            } else if (type == ModifyFilterEvent.Type.COST) {
                Integer eventCost = Integer.parseInt(event.getValue());
                if (costs.contains(eventCost)) {
                    costs.remove(eventCost);
                } else {
                    costs.add(eventCost);
                }
            }
        }

        if (event.getAction() == ModifyFilterEvent.Action.CLEAR_ALL) {
            categories.clear();
            activities.clear();
        }

        return ImmutableFilter.builder()
                .categories(categories)
                .activities(activities)
                .cost(costs)
                .build();
    }

    /**
     * Parcel Factory
     * @return
     */
    @ParcelFactory
    public static Filter build(HashSet<String> activities, ArrayList<Integer> cost,
        ArrayList<String> categories, LatLng userLocation) {
        return ImmutableFilter.builder()
                .activities(activities)
                .categories(categories)
                .userLocation(userLocation)
                .cost(cost)
                .build();
    }
}
