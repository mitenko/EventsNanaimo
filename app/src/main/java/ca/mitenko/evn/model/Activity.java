package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

/**
 * Created by David on 2017-04-20.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableActivity.class)
@Value.Immutable
public abstract class Activity {
    /**
     * The activity Id
     */
    @NonNull
    public abstract int id();
    /**
     * The name
     */
    @NonNull
    public abstract String name();

    /**
     * The activity's category
     */
    @NonNull
    public abstract String category();

    /**
     * Parcel Factory for parcelling immutables
     * @param id
     * @param name
     * @param category
     * @return
     */
    @ParcelFactory
    public static Activity build(int id, String name, String category) {
        return ImmutableActivity.builder()
                .id(id)
                .name(name)
                .category(category)
                .build();
    }
}
