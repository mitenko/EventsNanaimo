package ca.mitenko.evn.ui.dest_list;


import java.util.ArrayList;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-04-23.
 */

public interface DestListView extends RootView {
    /**
     * Sets the destination results
     * @param destinations
     */
    void setDestinations(ArrayList<Destination> destinations);
}
