package ca.mitenko.evn.ui.dest_map


import ca.mitenko.evn.model.Destination
import ca.mitenko.evn.ui.common.RootView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

/**
 * Created by mitenko on 2017-04-23.
 */

interface DestMapView : RootView {
    /**
     * Displays an error
     */
    fun showError(message: String)

    /**
     * Sets the destination results
     * @param destinations
     */
    fun setDestinations(destinations: ArrayList<Destination>)

    /**
     * Sets the destination results
     * @param destination
     */
    fun setSelectedItem(destination: Destination?)

    /**
     * Sets the map boundaries
     * @param mapBounds
     */
    fun setMapBounds(mapBounds: LatLngBounds, animate: Boolean)

    /**
     * Shows the Progress Bar
     */
    fun showProgressBar()

    /**
     * Hides the Progress Bar
     */
    fun hideProgressBar()

    /**
     * Shows the Update button
     */
    fun showUpdateButton()

    /**
     * Hides the Update button
     */
    fun hideUpdateButton()

    /**
     * Tells the map to recluster
     */
    fun recluster()

    /**
     * Sets the user location
     */
    fun setUserLocation(userLocation: LatLng)
}
