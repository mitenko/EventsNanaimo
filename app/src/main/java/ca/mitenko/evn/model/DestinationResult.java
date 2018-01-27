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

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestinationResult.class)
@Value.Immutable
public abstract class DestinationResult {
    /**
     * The data set of destinations
     */
    @NonNull
    @SerializedName("data")
    public abstract ArrayList<Destination> destinations();

    /**
     * Parceler for immutables
     * @param destinations
     * @return
     */
    @ParcelFactory
    public static DestinationResult build(ArrayList<Destination> destinations) {
        return ImmutableDestinationResult.builder()
                .destinations(destinations)
                .build();
    }
}
