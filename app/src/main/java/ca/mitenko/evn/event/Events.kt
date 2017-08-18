package ca.mitenko.evn.event

import ca.mitenko.evn.model.*
import ca.mitenko.evn.model.search.*;
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.ArrayList

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
data class MapBoundsEvent(val latLngBounds: LatLngBounds, val executeSearch: Boolean = false)
data class MapItemClickEvent(val destination: Destination?)
data class MapClusterClickEvent(val clusterBounds: LatLngBounds)
data class MapReadyEvent(val ready: Boolean = true)

/**
 * API Events
 */
data class CategoryResultEvent(val categoryResult: HashMap<String, ArrayList<Activity>>)
data class EventResultEvent(val events: ArrayList<Event>)
data class SearchEvent(val search: DestSearch)