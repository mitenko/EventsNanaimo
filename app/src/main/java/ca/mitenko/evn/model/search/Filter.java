package ca.mitenko.evn.model.search;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.event.FilterEvent;

import static android.R.attr.action;

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableFilter.class)
@Value.Immutable
public class Filter {
    /**
     * The activities to filter by
     * @return
     */
    @NonNull
    @Value.Default
    public ArrayList<String> activities() {
        return new ArrayList<>();
    }

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
     * Returns true / false if the filter is empty
     * @return
     */
    @Value.Lazy
    public boolean isEmpty() {
        return activities().isEmpty() && categories().isEmpty();
    }

    /**
     * Modifies the current filter based on the filter event
     * @param event
     * @return
     */
    public Filter withFilterEvent(FilterEvent event) {
        /**
         * Deep copy the current activities and categories
         */
        ArrayList<String> categories = new ArrayList<>();
        categories.addAll(categories());
        ArrayList<String> activities = new ArrayList<>();
        activities.addAll(activities());

        FilterEvent.Type type = event.getType();

        if (type == FilterEvent.Type.CATEGORY) {
            if (categories.contains(event.getValue())) {
                categories.remove(event.getValue());
            } else {
                categories.add(event.getValue());
            }
        } else if (type == FilterEvent.Type.ACTIVITY) {
            if (activities.contains(event.getValue())) {
                activities.remove(event.getValue());
            } else {
                activities.add(event.getValue());
            }
        }

        return ImmutableFilter.builder()
                .categories(categories)
                .activities(activities)
                .build();
    }

    /**
     * Parcel Factory
     * @return
     */
    @ParcelFactory
    public static Filter build(ArrayList<String> activities,
        ArrayList<String> categories) {
        return ImmutableFilter.builder()
                .activities(activities)
                .categories(categories)
                .build();
    }
}
