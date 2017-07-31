package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.DestinationResultEvent;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.DestListState;
import ca.mitenko.evn.state.ImmutableDestListState;
import ca.mitenko.evn.ui.dest_list.DestListView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class DestListPresenter extends RootPresenter<DestListView, DestListState> {
    /**
     * Constructor
     * @param view
     * @param curState
     */
    public DestListPresenter(DestListView view, DestListState curState, EventBus bus) {
        super(view, ImmutableDestListState.builder().build(), curState, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(DestListView view, DestListState curState, DestListState prevState) {
        if (view != null) {
            if (!curState.destinations().equals(prevState.destinations())) {
                view.setDestinations(curState.destinations());
            }
        }
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onDestinationResultEvent(DestinationResultEvent event) {
        DestListState newState = ImmutableDestListState.builder()
                .from(curState)
                .destinations(event.getSearch().filteredResults())
                .build();
        render(newState);
    }
}
