package ca.mitenko.evn.ui.hub;

import ca.mitenko.evn.model.Destination;
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
     * Shutdown the app
     */
    void shutdown();

    /**
     * Applies the filter to the UI elements
     */
    void showFilter(Filter filter);
}
