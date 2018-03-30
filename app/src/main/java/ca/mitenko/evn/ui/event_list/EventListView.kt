package ca.mitenko.evn.ui.event_list


import java.util.ArrayList

import ca.mitenko.evn.model.Event
import ca.mitenko.evn.ui.common.RootView

/**
 * Created by mitenko on 2017-04-23.
 */

interface EventListView : RootView {
    /**
     * Displays an error
     */
    fun showError(message: String)

    /**
     * Sets the destination results
     * @param events
     */
    fun setEvents(events: ArrayList<Event>?)
}
