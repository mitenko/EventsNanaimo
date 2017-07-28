package ca.mitenko.evn.state;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-22.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestListState.class)
@Value.Immutable
public class DestListState extends RootState {

    /**
     * Destinations loaded state flag
     */
    @Value.Default
    public ArrayList<Destination> destinations() {
        return new ArrayList<>();
    }
    /**
     * Parcel factory to allow parcelling immutables
     */
    @ParcelFactory
    public static DestListState build(ArrayList<Destination> destinations) {
        return ImmutableDestListState.builder()
                .destinations(destinations)
                .build();
    }
}
