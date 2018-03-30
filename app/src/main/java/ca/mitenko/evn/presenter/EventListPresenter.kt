package ca.mitenko.evn.presenter

import ca.mitenko.evn.event.ErrorEvent
import ca.mitenko.evn.event.EventResultEvent
import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.EventListState
import ca.mitenko.evn.state.ImmutableDestMapState
import ca.mitenko.evn.state.ImmutableEventListState
import ca.mitenko.evn.ui.event_list.EventListView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by mitenko on 2017-04-23.
 */

class EventListPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: EventListView, curState: EventListState, bus: EventBus) : RootPresenter<EventListView, EventListState>(view, ImmutableEventListState.builder().build(), curState, bus) {

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: EventListView?, curState: EventListState, prevState: EventListState) {
        if (view != null) {
            /**
             * Display an error during the map load
             */
            if (curState.error() != null && curState.error() != prevState.error()) {
                view.showError(curState.error()?.message ?: "")
            }

            if (curState.events() != null && curState.events() != prevState.events()) {
                view.setEvents(curState.events())
            }
        }
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    fun onEventResultEvent(event: EventResultEvent) {
        val newState = ImmutableEventListState.builder()
                .from(curState)
                .events(event.events)
                .build()
        render(newState)
    }
    /**
     * Called when a filtering event has occurred
     * @param event
     */
    @Subscribe
    fun onErrorEvent(event: ErrorEvent) {
        val newState = ImmutableEventListState.builder()
                .from(curState)
                .error(event.error)
                .build()
        render(newState)
    }
}
