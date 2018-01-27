package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Category;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.model.search.ImmutableDestSearch;
import ca.mitenko.evn.model.search.ImmutableFilter;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableFilterFragState.class)
@Value.Immutable
public class FilterFragState extends RootState {
    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.filter";

    /**
     * The Filter
     */
    @NonNull
    @Value.Default
    public Filter filter() {
        return ImmutableFilter.builder().build();
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
    public static FilterFragState build(Filter filter, HashMap<String, ArrayList<Activity>> categoryMap) {
        return ImmutableFilterFragState.builder()
                .filter(filter)
                .categoryMap(categoryMap)
                .build();
    }
}
