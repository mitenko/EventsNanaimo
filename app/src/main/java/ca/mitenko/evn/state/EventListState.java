package ca.mitenko.evn.state;

import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableEventListState.class)
@Value.Immutable
public class EventListState extends RootState {

    /**
     * Destinations loaded state flag
     */
    @Nullable
    @Value.Default
    public ArrayList<Event> events() {
        return null;
    }

    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static EventListState build(ArrayList<Event> events) {
        return ImmutableEventListState.builder()
                .events(events)
                .build();
    }
}
