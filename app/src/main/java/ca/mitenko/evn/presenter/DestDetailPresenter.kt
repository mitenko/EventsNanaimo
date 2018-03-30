package ca.mitenko.evn.presenter

import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.DestDetailState
import ca.mitenko.evn.state.ImmutableDestDetailState
import ca.mitenko.evn.ui.dest_detail.DestDetailView
import org.greenrobot.eventbus.EventBus

/**
 * Created by mitenko on 2017-04-23.
 */

class DestDetailPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: DestDetailView, curState: DestDetailState, bus: EventBus) : RootPresenter<DestDetailView, DestDetailState>(view, ImmutableDestDetailState.builder().build(), curState, bus) {

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: DestDetailView?, curState: DestDetailState, prevState: DestDetailState) {
        if (view != null) {
            if (curState.destination() != prevState.destination()) {
                view.displayDest(curState.destination())
            }
        }
    }
}
