package ca.mitenko.evn.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

/**
 * Created by mitenko on 2017-04-20.
 */

@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableCategoryResult.class)
@Value.Immutable
public abstract class CategoryResult {
    /**
     * The data set of destinations
     */
    @NonNull
    @SerializedName("data")
    public abstract ArrayList<Category> categories();

    /**
     * Immutable Parcel Builder
     * @param categories
     * @return
     */
    @ParcelFactory
    public static CategoryResult build(ArrayList<Category> categories) {
        return ImmutableCategoryResult.builder()
                .categories(categories)
                .build();
    }

}
