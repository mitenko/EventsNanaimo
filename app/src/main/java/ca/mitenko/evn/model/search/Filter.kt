package ca.mitenko.evn.model.search

import android.os.Parcelable
import ca.mitenko.evn.event.ModifyFilterEvent
import ca.mitenko.evn.model.Activity
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import org.immutables.value.Value
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcelize
data class Filter(
        val categories: ArrayList<String> = ArrayList(),
        val activities: HashSet<String> = HashSet(),
        val cost: ArrayList<Int> = ArrayList(),
        val userLocation: LatLng? = null
): Parcelable {

    /**
     * Returns true / false if the filter is empty
     * @return
     */
    open val isEmpty: Boolean
        @Value.Lazy
        get() = (activities.isEmpty() && categories.isEmpty()
                && cost.isEmpty() && userLocation == null)
    /**
     * Modifies the current filter based on the filter event
     * @param event
     * @return
     */
    fun modify(event: ModifyFilterEvent, categoryMap: HashMap<String, ArrayList<Activity>>): Filter {
        /**
         * Deep copy the current activities and categories
         */
        val categories = ArrayList<String>()
        categories.addAll(categories)
        val activities = HashSet<String>()
        activities.addAll(activities)
        val costs = ArrayList<Int>()
        costs.addAll(cost)

        val type = event.type

        if (event.action === ModifyFilterEvent.Action.TOGGLE) {

            if (type === ModifyFilterEvent.Type.CATEGORY) {
                if (categories.contains(event.value)) {
                    categories.remove(event.value)
                } else {
                    categories.add(event.value)
                }

                /**
                 * To properly add activities after a category toggle,
                 * you need to completely redo them
                 */
                activities.clear()
                for (categoryName in categories) {
                    val catActivities = categoryMap[categoryName]
                    if (catActivities != null && !catActivities.isEmpty()) {
                        for ((_, name) in catActivities) {
                            activities.add(name)
                        }
                    }
                }

            } else if (type === ModifyFilterEvent.Type.ACTIVITY) {
                if (activities.contains(event.value)) {
                    activities.remove(event.value)
                } else {
                    activities.add(event.value)
                }
            } else if (type === ModifyFilterEvent.Type.COST) {
                val eventCost = Integer.parseInt(event.value)
                if (costs.contains(eventCost)) {
                    costs.remove(eventCost)
                } else {
                    costs.add(eventCost)
                }
            }
        }

        if (event.action === ModifyFilterEvent.Action.CLEAR_ALL) {
            categories.clear()
            activities.clear()
        }

        return this.copy(categories = categories, activities = activities, cost = costs)
    }
}
