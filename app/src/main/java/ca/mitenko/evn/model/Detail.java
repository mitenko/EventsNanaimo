package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Created by mitenko on 2017-04-19.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDetail.class)
@Value.Immutable
public abstract class Detail {
    /**
     * The detail Id
     */
    @NonNull
    public abstract int id();

    /**
     * The name
     */
    @NonNull
    public abstract String name();

    /**
     * The short description
     */
    @NonNull
    public abstract String shortDesc();

    /**
     * The long description
     */
    @NonNull
    public abstract String longDesc();

    /**
     * The thumb URL
     */
    @Nullable
    public abstract String thumbURL();

    /**
     * The image URL
     */
    @Nullable
    public abstract String imageURL();

    /**
     * Phone Number
     */
    @Nullable
    public abstract String phone();
    /**
     * Website URL
     */
    @Nullable
    public abstract String website();

    /**
     * The thumb URL
     */
    public abstract int cost();

    /**
     * The List of Activities for this Detail
     */
    @NonNull
    public abstract ArrayList<Activity> activities();

    /**
     * Immutable Parcel Builder
     * @param id
     * @param name
     * @param shortDesc
     * @param longDesc
     * @param thumbURL
     * @param imageURL
     * @param phone
     * @param website
     * @param cost
     * @param activities
     * @return
     */
    @ParcelFactory
    public static Detail build(int id, String name, String shortDesc, String longDesc,
                       String thumbURL, String imageURL, String phone, String website,
                               int cost, ArrayList<Activity> activities) {
        return ImmutableDetail.builder()
                .id(id)
                .name(name)
                .shortDesc(shortDesc)
                .longDesc(longDesc)
                .thumbURL(thumbURL)
                .imageURL(imageURL)
                .phone(phone)
                .website(website)
                .cost(cost)
                .activities(activities)
                .build();
    }
}
