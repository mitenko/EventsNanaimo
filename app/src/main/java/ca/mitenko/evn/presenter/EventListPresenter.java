package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.EventResultEvent;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.EventListState;
import ca.mitenko.evn.state.ImmutableEventListState;
import ca.mitenko.evn.ui.event_list.EventListView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class EventListPresenter extends RootPresenter<EventListView, EventListState> {
    /**
     * Constructor
     * @param view
     * @param curState
     */
    public EventListPresenter(EventListView view, EventListState curState, EventBus bus) {
        super(view, ImmutableEventListState.builder().build(), curState, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(EventListView view, EventListState curState, EventListState prevState) {
        if (view != null) {
            if (!curState.events().equals(prevState.events())) {
                view.setEvents(curState.events());
            }
        }
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onEventResultEvent(EventResultEvent event) {
        EventListState newState = ImmutableEventListState.builder()
                .from(curState)
                .events(event.getEvents())
                .build();
        render(newState);
    }
}
