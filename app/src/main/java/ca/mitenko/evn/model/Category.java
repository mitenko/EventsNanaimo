package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

/**
 * Created by mitenko on 2017-04-20.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableCategory.class)
@Value.Immutable
public abstract class Category {
    /**
     * The category Id
     */
    @NonNull
    public abstract int id();

    /**
     * The name
     */
    @NonNull
    public abstract String name();

    /**
     * The List of Activities for this Category
     */
    @NonNull
    public abstract ArrayList<Activity> activities();

    /**
     * Immutable Parcel Builder
     * @param id
     * @param name
     * @param activities
     * @return
     */
    @ParcelFactory
    public static Category build(int id, String name, ArrayList<Activity> activities) {
        return ImmutableCategory.builder()
                .id(id)
                .name(name)
                .activities(activities)
                .build();
    }

}
