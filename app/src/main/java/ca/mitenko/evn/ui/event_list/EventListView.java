package ca.mitenko.evn.ui.event_list;


import java.util.ArrayList;

import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-04-23.
 */

public interface EventListView extends RootView {
    /**
     * Sets the destination results
     * @param events
     */
    void setEvents(ArrayList<Event> events);
}
