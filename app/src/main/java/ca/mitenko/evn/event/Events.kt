package ca.mitenko.evn.event

import ca.mitenko.evn.model.*
import ca.mitenko.evn.model.search.*;
import com.google.android.gms.maps.model.LatLngBounds
import java.util.ArrayList

/**
 * Created by mitenko on 2017-07-16.
 */

data class DefaultEvent(val isDefault: Boolean = true)

/**
 * Button Events
 */
data class FilterSearchEvent (val searchType: SearchType) {
    enum class SearchType {
        NIGHTLIFE
    }
}
data class DestItemClickEvent(val destination: Destination)
data class ViewEventEvent(val showEvents: Boolean = true)
data class ViewListEvent(val showList: Boolean = true)
data class ViewMapEvent(val showMap: Boolean = true)
data class UpdateMapRequestEvent(val showMap: Boolean = true)

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
data class CategoryResultEvent(val categoryResult: ArrayList<Category>)
data class DestinationResultEvent(val search: DestSearch)
data class EventResultEvent(val events: ArrayList<Event>)