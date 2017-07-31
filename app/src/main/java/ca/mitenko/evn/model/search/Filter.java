package ca.mitenko.evn.model.search;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

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
