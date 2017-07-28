package ca.mitenko.evn.state;

import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestDetailState.class)
@Value.Immutable
public class DestDetailState extends RootState {

    /**
     * Destination to be rendered
     */
    @Nullable
    @Value.Default
    public Destination destination() {
        return null;
    }

    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestDetailState build(Destination destination) {
        return ImmutableDestDetailState.builder()
                .destination(destination)
                .build();
    }
}
