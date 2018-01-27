package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

/**
 * Created by mitenko on 2017-04-20.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableEvent.class)
@Value.Immutable
public abstract class Event {
    /**
     * The event Id
     */
    @NonNull
    public abstract int id();

    /**
     * Event Detail
     */
    @NonNull
    public abstract Detail detail();

    /**
     * Event Start Time
     */
    @NonNull
    public abstract String readableStartTime();

    /**
     * Event Start Time
     */
    @NonNull
    public abstract String readableEndTime();

    /**
     * Event Start Time
     */
    @NonNull
    public abstract Long unixStartTime();

    /**
     * Event Start Time
     */
    @NonNull
    public abstract Long unixEndTime();

    /**
     * The List of Destinations for this Event
     */
    @NonNull
    public abstract ArrayList<Integer> destinations();

    /**
     * Parceler for immutables
     * @param id
     * @param detail
     * @param readableStartTime
     * @param readableEndTime
     * @param destinations
     * @return
     */
    @ParcelFactory
    public static Event build(int id, Detail detail, String readableStartTime,
                        String readableEndTime, ArrayList<Integer> destinations,
                          Long unixStartTime, Long unixEndTime) {
        return ImmutableEvent.builder()
                .id(id)
                .detail(detail)
                .readableStartTime(readableStartTime)
                .readableEndTime(readableEndTime)
                .destinations(destinations)
                .unixStartTime(unixStartTime)
                .unixEndTime(unixEndTime)
                .build();
    }
}
