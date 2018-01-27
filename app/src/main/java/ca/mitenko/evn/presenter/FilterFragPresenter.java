package ca.mitenko.evn.presenter;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.CategoryResultEvent;
import ca.mitenko.evn.event.FilterEvent;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.event.SearchEvent;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.FilterFragState;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.ImmutableFilterFragState;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.filter.FilterView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class FilterFragPresenter extends RootPresenter<FilterView, FilterFragState> {

    /**
     * Constructor
     * @param view
     * @param curState
     */
    public FilterFragPresenter(FilterView view, FilterFragState curState,
                               EventBus bus) {
        super(view, ImmutableFilterFragState.builder().build(), curState, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(FilterView view, FilterFragState curState, FilterFragState prevState) {
        if (view != null) {
            // Update the filter buttons
            if (!curState.filter().equals(prevState.filter())) {
                view.applyFilterToView(curState.filter());
            }

            // Set the categories
            if (curState.categoryMap() != null &&
                    !curState.categoryMap().equals(prevState.categoryMap())) {
                view.renderCategories(curState.categoryMap());
            }
        }
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    public void onCategoryResultEvent(CategoryResultEvent event) {
        FilterFragState newState = ImmutableFilterFragState.builder()
                .from(curState)
                .categoryMap(event.getCategoryResult())
                .build();
        render(newState);
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    public void onFilterEvent(FilterEvent event) {
        if (event.getFilter().equals(curState.filter())) {
            return;
        }
        FilterFragState newState = ImmutableFilterFragState.builder()
                .from(curState)
                .filter(event.getFilter())
                .build();
        render(newState);
    }

    /**
     * Called when the Filter has been modified
     */
    @Subscribe
    public void onModifyFilterEvent(ModifyFilterEvent event) {
        Filter newFilter = curState.filter().modify(event, curState.categoryMap());
        FilterFragState newState = ImmutableFilterFragState.builder()
                .from(curState)
                .filter(newFilter)
                .build();
        render(newState);
        bus.postSticky(new FilterEvent(newFilter));
    }
}
