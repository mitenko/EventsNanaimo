package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

/**
 * Created by mitenko on 2017-04-23.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableEventResult.class)
@Value.Immutable
public abstract class EventResult {
    /**
     * The data set of destinations
     */
    @NonNull
    @SerializedName("data")
    public abstract ArrayList<Event> events();

    /**
     * Parceler for immutables
     * @param events
     * @return
     */
    @ParcelFactory
    public static EventResult build(ArrayList<Event> events) {
        return ImmutableEventResult.builder()
                .events(events)
                .build();
    }
}
