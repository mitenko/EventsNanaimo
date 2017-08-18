package ca.mitenko.evn.model;

/**
 * Created by mitenko on 2017-04-20.
 */

import android.support.annotation.NonNull;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableAddress.class)
@Value.Immutable
public abstract class Address {
    /**
     * The address Id
     */
    @NonNull
    public abstract int id();

    /**
     * First Line
     */
    @NonNull
    public abstract String lineOne();

    /**
     * Second Line
     */
    @NonNull
    public abstract String lineTwo();

    /**
     * Postal Code
     */
    @NonNull
    public abstract String postalCode();

    /**
     * City
     */
    @NonNull
    public abstract String city();

    /**
     * Converts the address into a string
     * @return
     */
    public String toString() {
        return lineOne() + " " + lineTwo() + ", " + city() + ", " + postalCode();
    }

    /**
     * Builds a parcelable immutable
     * @param id
     * @param lineOne
     * @param lineTwo
     * @param postalCode
     * @param city
     * @return
     */
    @ParcelFactory
    public static Address build(int id, String lineOne,
               String lineTwo, String postalCode, String city){
        return ImmutableAddress.builder()
                .id(id)
                .lineOne(lineOne)
                .lineTwo(lineTwo)
                .postalCode(postalCode)
                .city(city)
                .build();
    }
}
