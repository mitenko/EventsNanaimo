package ca.mitenko.evn.ui.hub;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-04-22.
 */

public interface HubView extends RootView {
    /**
     * Display the Destination Map
     */
    void showDestMap();

    /**
     * Display the Destination List
     */
    void showDestList();

    /**
     * Display the Event List
     */
    void showEventList();

    /**
     * Display the Event List
     */
    void showDestDetail(Destination selectedDest);

    /**
     * Display the Event List
     */
    void showEventDetail(Event selectedEvent);

    /**
     * Shutdown the app
     */
    void shutdown();

    /**
     * Applies the filter to the UI elements
     */
    void applyFilterToView(Filter filter);

    /**
     * Display the Filter Fragment
     */
    void showFilterView();

    /**
     * Requests the user location
     */
    void getUserLocation();
}
