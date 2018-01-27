package ca.mitenko.evn.ui.event_detail;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-05-06.
 */

public interface EventDetailView extends RootView {
    /**
     * Displays the Destination
     */
    void displayEvent(Event event);
}
