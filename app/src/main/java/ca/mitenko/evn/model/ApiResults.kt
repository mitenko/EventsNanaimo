package ca.mitenko.evn.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.HashMap

/**
 * Created by mitenko on 2018-01-27.
 */

@Parcelize
data class CategoryResult(
        val categories: @RawValue ArrayList<Category>
): Parcelable {
    /**
     * Maps the Catgory array to something more useful
     * @return
     */
    fun categoryMap(): HashMap<String, java.util.ArrayList<Activity>> {
        val categoryMap = HashMap<String, java.util.ArrayList<Activity>>()
        for (category in categories) {
            categoryMap.put(category.name, category.activities)
        }
        return categoryMap
    }
}

@Parcelize
data class DestinationResult(
        val destinations: @RawValue ArrayList<Destination>
): Parcelable

@Parcelize
data class EventResult(
        val events: @RawValue ArrayList<Event>
): Parcelable