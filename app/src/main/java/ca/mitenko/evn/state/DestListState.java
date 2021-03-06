package ca.mitenko.evn.state;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestListState.class)
@Value.Immutable
public class DestListState extends RootState {
    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.dest_list";

    /**
     * Destinations loaded state flag
     */
    @NonNull
    @Value.Default
    public DestSearch search() {
        return new DestSearch();
    }

    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestListState build(DestSearch search) {
        return ImmutableDestListState.builder()
                .search(search)
                .build();
    }
}
