package ca.mitenko.evn.presenter

import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.EventDetailState
import ca.mitenko.evn.state.ImmutableEventDetailState
import ca.mitenko.evn.ui.event_detail.EventDetailView
import org.greenrobot.eventbus.EventBus

/**
 * Created by mitenko on 2017-04-23.
 */

class EventDetailPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: EventDetailView, curState: EventDetailState, bus: EventBus) : RootPresenter<EventDetailView, EventDetailState>(view, ImmutableEventDetailState.builder().build(), curState, bus) {

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: EventDetailView?, curState: EventDetailState, prevState: EventDetailState) {
        if (view != null) {
            if (curState.event() != prevState.event()) {
                view.displayEvent(curState.event())
            }
        }
    }
}
