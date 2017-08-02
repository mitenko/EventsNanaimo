package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

/**
 * Created by mitenko on 2017-04-20.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableDestination.class)
@Value.Immutable
public abstract class Destination implements ClusterItem {
    /**
     * The destination Id
     */
    @NonNull
    public abstract int id();

    /**
     * The destination latitude
     */
    @NonNull
    public abstract float latitude();

    /**
     * The destination longitude
     */
    @NonNull
    public abstract float longitude();

    /**
     * Destination Address
     */
    @NonNull
    public abstract Address address();

    /**
     * Category of the icon to display
     */
    @NonNull
    public abstract String displayIcon();

    /**
     * Destination Detail
     */
    @NonNull
    public abstract Detail detail();

    /**
     * Parceler for immutables
     * @param id
     * @param latitude
     * @param longitude
     * @param address
     * @return
     */
    @ParcelFactory
    public static Destination build(int id, float latitude, float longitude,
                                    Address address, Detail detail) {
        return ImmutableDestination.builder()
                .id(id)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .detail(detail)
                .build();
    }

    /**
     * Cluster Ite
    /**
     * The position of this marker. This must always return the same value.
     */
    public LatLng getPosition() {
        return new LatLng(latitude(), longitude());
    }

    /**
     * The title of this marker.
     */
    public String getTitle() {
        return detail().name();
    }

    /**
     * The description of this marker.
     */
    public String getSnippet() {
        return "";
    }

}
