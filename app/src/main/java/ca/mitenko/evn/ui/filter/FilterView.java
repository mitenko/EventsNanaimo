package ca.mitenko.evn.ui.filter;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Category;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-05-06.
 */

public interface FilterView extends RootView {
    /**
     * Applies the filter to the UI elements
     */
    void applyFilterToView(Filter filter);

    /**
     * Renders the categories in the view
     */
    void renderCategories(HashMap<String, ArrayList<Activity>> categoryMap);
}
