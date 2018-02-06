package ca.mitenko.evn.presenter

import ca.mitenko.evn.event.FilterEvent
import ca.mitenko.evn.event.SearchEvent
import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.DestListState
import ca.mitenko.evn.state.ImmutableDestListState
import ca.mitenko.evn.ui.dest_list.DestListView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by mitenko on 2017-04-23.
 */

class DestListPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: DestListView, curState: DestListState, bus: EventBus) : RootPresenter<DestListView, DestListState>(view, ImmutableDestListState.builder().build(), curState, bus) {

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: DestListView?, curState: DestListState, prevState: DestListState) {
        if (view != null) {
            if (curState.search().filteredResults(true) != prevState.search().filteredResults(true)) {
                view.setDestinations(curState.search().filteredResults(true))
            }
        }
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    fun onSearchEvent(event: SearchEvent) {
        if (curState.search() == event.search) {
            return
        }
        val newState = ImmutableDestListState.builder()
                .from(curState)
                .search(event.search)
                .build()
        render(newState)
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    fun onFilterEvent(event: FilterEvent) {
        if (event.filter == curState.search().filter) {
            return
        }
        val newState = ImmutableDestListState.builder()
                .from(curState)
                .search(curState.search().copy(filter = event.filter))
                .build()
        render(newState)
    }
}
