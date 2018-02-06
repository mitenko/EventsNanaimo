package ca.mitenko.evn.model.search

import android.os.Parcelable
import ca.mitenko.evn.model.Destination
import ca.mitenko.evn.util.LocationUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.parcel.Parcelize
import org.immutables.value.Value
import java.util.*

/**
 * Created by mitenko on 2017-07-30.
 */

@Parcelize
data class DestSearch(
        val searchBounds: LatLngBounds = DEFAULT_BOUNDS,
        val mapBounds: LatLngBounds? = null,
        val filter: Filter = Filter(),
        val results: ArrayList<Destination>? = null

): Parcelable {
    /**
     * The map bounds. Default is roughly nanaimo
     * @return
     */
    fun mapBoundsOrDefault() = mapBounds ?: searchBounds

    /**
     * Returns true / false if the search
     * has a non-default set of map bounds
     * @return
     */
    fun hasMapBounds() = mapBounds != null

    /**
     * Returns true / false if the map bounds are
     * outside of the search bounds
     * @return
     */
    @Value.Lazy
    fun mapOutsideSearch(): Boolean {
        return if (!hasResults() || mapBounds == null) {
            false
        } else !searchBounds.contains(mapBounds.northeast) || !searchBounds.contains(mapBounds.southwest)
    }

    /**
     * The raw result set
     */
    fun hasResults() = results != null

    /**
     * The filtered set of results
     */
    fun filteredResults(filterByMapBounds: Boolean): ArrayList<Destination> {
        if (!hasResults()) {
            return ArrayList()
        }

        /**
         * Sort the list by nearest to farther if user location is known
         */
        val orderedResults = results ?: ArrayList()
        if (filter.userLocation != null) {
            val userLocation = LocationUtil.fromLatLng(filter.userLocation)

            Collections.sort(orderedResults!!) { d1, d2 ->
                val location1 = LocationUtil.fromDestination(d1)
                val location2 = LocationUtil.fromDestination(d2)
                Integer.signum(
                        location1.distanceTo(userLocation).toInt() - location2.distanceTo(userLocation).toInt())
            }
        }

        /**
         * Filter by category and / or activity
         */
        if (filter.isEmpty) {
            return orderedResults
        }

        /**
         * Otherwise apply the filter
         */
        val filteredResults = ArrayList<Destination>()
        for (destination in orderedResults) {
            val destinationCost = destination.detail.cost
            for ((_, name, category) in destination.detail.activities) {
                /**
                 * Filter by Activity / Cost / Category
                 */
                if ((filter.categories.isEmpty() || filter.categories.contains(category)) &&
                        (filter.activities.isEmpty() || filter.activities.contains(name)) &&
                        (filter.cost.isEmpty() || filter.cost.contains(destinationCost))) {
                    filteredResults.add(
                            destination.copy(displayIcon = category ?: "unknown"))
                    break
                }
            }
        }
        return filteredResults
    }

    companion object {
        val DEFAULT_BOUNDS = LatLngBounds(
                LatLng(49.15938572687397, -123.9760036021471),
                LatLng(49.20606374369103, -123.91420517116785))
    }
}
