package ca.mitenko.evn.state;

import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableEventDetailState.class)
@Value.Immutable
public class EventDetailState extends RootState {
    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.event_detail";

    /**
     * Destination to be rendered
     */
    @Nullable
    @Value.Default
    public Event event() {
        return null;
    }

    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static EventDetailState build(Event event) {
        return ImmutableEventDetailState.builder()
                .event(event)
                .build();
    }
}
