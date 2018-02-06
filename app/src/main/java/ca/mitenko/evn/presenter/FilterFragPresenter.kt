package ca.mitenko.evn.presenter

import ca.mitenko.evn.event.CategoryResultEvent
import ca.mitenko.evn.event.FilterEvent
import ca.mitenko.evn.event.ModifyFilterEvent
import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.FilterFragState
import ca.mitenko.evn.state.ImmutableFilterFragState
import ca.mitenko.evn.ui.filter.FilterView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by mitenko on 2017-04-23.
 */

class FilterFragPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: FilterView, curState: FilterFragState,
 bus: EventBus) : RootPresenter<FilterView, FilterFragState>(view, ImmutableFilterFragState.builder().build(), curState, bus) {

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: FilterView?, curState: FilterFragState, prevState: FilterFragState) {
        if (view != null) {
            // Update the filter buttons
            if (curState.filter() != prevState.filter()) {
                view.applyFilterToView(curState.filter())
            }

            // Set the categories
            if (curState.categoryMap() != null && curState.categoryMap() != prevState.categoryMap()) {
                view.renderCategories(curState.categoryMap())
            }
        }
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    fun onCategoryResultEvent(event: CategoryResultEvent) {
        val newState = ImmutableFilterFragState.builder()
                .from(curState)
                .categoryMap(event.categoryResult)
                .build()
        render(newState)
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    fun onFilterEvent(event: FilterEvent) {
        if (event.filter == curState.filter()) {
            return
        }
        val newState = ImmutableFilterFragState.builder()
                .from(curState)
                .filter(event.filter)
                .build()
        render(newState)
    }

    /**
     * Called when the Filter has been modified
     */
    @Subscribe
    fun onModifyFilterEvent(event: ModifyFilterEvent) {
        val newFilter = curState.filter().modify(event, curState.categoryMap()!!)
        val newState = ImmutableFilterFragState.builder()
                .from(curState)
                .filter(newFilter)
                .build()
        render(newState)
        bus.postSticky(FilterEvent(newFilter))
    }
}
