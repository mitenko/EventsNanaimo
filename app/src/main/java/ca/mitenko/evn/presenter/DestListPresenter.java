package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.FilterEvent;
import ca.mitenko.evn.event.SearchEvent;
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
            if (!curState.search().filteredResults(true)
                    .equals(prevState.search().filteredResults(true))) {
                view.setDestinations(curState.search().filteredResults(true));
            }
        }
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onSearchEvent(SearchEvent event) {
        if (curState.search().equals(event.getSearch())) {
            return;
        }
        DestListState newState = ImmutableDestListState.builder()
                .from(curState)
                .search(event.getSearch())
                .build();
        render(newState);
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    public void onFilterEvent(FilterEvent event) {
        if (event.getFilter().equals(curState.search().getFilter())) {
            return;
        }
        DestListState newState = ImmutableDestListState.builder()
                .from(curState)
                .search(ImmutableDestSearch.builder()
                        .from(curState.search())
                        .filter(event.getFilter())
                        .build())
                .build();
        render(newState);
    }
}
