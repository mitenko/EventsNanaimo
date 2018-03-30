package ca.mitenko.evn.event

import ca.mitenko.evn.model.Activity
import ca.mitenko.evn.model.Destination
import ca.mitenko.evn.model.Event
import ca.mitenko.evn.model.search.DestSearch
import ca.mitenko.evn.model.search.Filter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

/**
 * Created by mitenko on 2017-07-16.
 */

data class DefaultEvent(val isDefault: Boolean = true)
data class UserLocationEvent(val outcome: Outcome, val userLatLng: LatLng?) {
    enum class Outcome {
        SUCCESS,
        DENIED,
        FAILED
    }
}

/**
 * Button Events
 */
data class DestItemClickEvent(val destination: Destination)
data class EventItemClickEvent(val event: Event)
data class ViewEventEvent(val showEvents: Boolean = true)
data class ViewListEvent(val showList: Boolean = true)
data class ViewMapEvent(val showMap: Boolean = true)
data class ViewFilterEvent(val showFilter: Boolean = true)
data class UpdateMapRequestEvent(val showMap: Boolean = true)

/**
 * Filter Events
 */
data class ModifyFilterEvent(val type: Type, val value: String,
                             val action: Action) {
    enum class Type {
        ACTIVITY,
        CATEGORY,
        COST
    }
    enum class Action {
        TOGGLE,
        CLEAR_ALL
    }
}
data class FilterEvent(val filter: Filter)

/**
 * Map Events
 */
data class MapBoundsEvent(val latLngBounds: LatLngBounds)
data class MapItemClickEvent(val destination: Destination?)
data class MapClusterClickEvent(val clusterBounds: LatLngBounds)
data class MapReadyEvent(val ready: Boolean = true)

/**
 * API Events
 */
data class CategoryResultEvent(val categoryResult: HashMap<String, ArrayList<Activity>>)
data class EventResultEvent(val events: ArrayList<Event>)
data class SearchEvent(val search: DestSearch)
data class ErrorEvent(val error: Throwable)