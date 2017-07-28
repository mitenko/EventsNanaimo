package ca.mitenko.evn.ui.dest_map;


import android.view.View;

import java.util.ArrayList;

import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-04-23.
 */

public interface DestMapView extends RootView {
    /**
     * Sets the destination results
     * @param destinations
     */
    void setDestinations(ArrayList<Destination> destinations);

    /**
     * Sets the destination results
     * @param destination
     */
    void setSelectedItem(Destination destination);

    /**
     * Shows the Progress Bar
     */
    void showProgressBar();

    /**
     * Hides the Progress Bar
     */
    void hideProgressBar();
    /**
     * Shows the Update button
     */
    void showUpdateButton();

    /**
     * Hides the Update button
     */
    void hideUpdateButton();

    /**
     * Tells the map to recluster
     */
    void recluster();
}
