package ca.mitenko.evn.ui.dest_detail;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-05-06.
 */

public interface DestDetailView extends RootView {
    /**
     * Displays the Destination
     */
    void displayDest(Destination dest);
}
