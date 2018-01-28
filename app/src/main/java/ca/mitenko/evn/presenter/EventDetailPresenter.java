package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;

import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.EventDetailState;
import ca.mitenko.evn.state.ImmutableEventDetailState;
import ca.mitenko.evn.ui.event_detail.EventDetailView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class EventDetailPresenter extends RootPresenter<EventDetailView, EventDetailState> {
    /**
     * Constructor
     * @param view
     * @param curState
     */
    public EventDetailPresenter(EventDetailView view, EventDetailState curState, EventBus bus) {
        super(view, ImmutableEventDetailState.builder().build(), curState, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(EventDetailView view, EventDetailState curState, EventDetailState prevState) {
        if (view != null) {
            if (!curState.event().equals(prevState.event())) {
                view.displayEvent(curState.event());
            }
        }
    }
}
