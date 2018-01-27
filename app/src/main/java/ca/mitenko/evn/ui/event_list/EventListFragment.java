package ca.mitenko.evn.ui.event_list;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.EventListPresenter;
import ca.mitenko.evn.state.EventDetailState;
import ca.mitenko.evn.state.EventListState;
import ca.mitenko.evn.state.ImmutableEventDetailState;
import ca.mitenko.evn.state.ImmutableEventListState;
import ca.mitenko.evn.ui.common.RootFragment;
import ca.mitenko.evn.ui.event_list.adapter.EventListAdapter;
import retrofit2.Retrofit;

/**
 * Created by mitenko on 2017-04-22.
 */

public class EventListFragment extends RootFragment
    implements EventListView {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.event_list";

    /**
     * Hub Fragment container
     */
    @BindView(R.id.list_container)
    CoordinatorLayout listContainer;

    /**
     * The Map View
     */
    @BindView(R.id.list_view)
    RecyclerView recyclerView;

    /**
     * Message Showing the empty list
     */
    @BindView(R.id.empty_notice)
    ConstraintLayout emptyListMessage;

    /**
     * Event bus
     */
    @Inject
    EventBus bus;

    /**
     * Retrofit
     */
    @Inject
    Retrofit retrofit;

    /**
     * The Service
     */
    @Inject
    EventsNanaimoService evnService;

    /**
     * The List Adapter
     */
    private EventListAdapter adapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EvNApplication) getActivity().getApplication())
                .getApplicationComponent().inject(this);

        // The Destination List
        adapter = new EventListAdapter(bus);
        recyclerView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init presenter
        presenter = new EventListPresenter(
                this, (EventListState)state, bus);
        setToolbar();
        hub.showFilterButton();
    }

    /**
     * Returns the state key for storing and restoring the state
     * @return
     */
    public String getStateKey() {
        return EventListState.TAG;
    }

    /**
     * Returns the default state
     * @return
     */
    public EventListState getDefaultState() {
        return ImmutableEventListState.builder().build();
    }

    /**
     * {@inheritDoc}
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolbar();
            hub.showFilterButton();
        }
    }

    /**
     * Sets the tool bar to show the proper title
     */
    public void setToolbar() {
        hub.setToolbarTitle(getString(R.string.event_toolbar_title));
        hub.showCategoryPicker(false);
    }

    /**
     * Presenter / View Callbacks
     */
    /**
     * {@inheritDoc}
     */
    public void setEvents(ArrayList<Event> events) {
        adapter.setEvents(events);
        if (events.size() > 0) {
            emptyListMessage.setVisibility(View.GONE);
            recyclerView.scrollToPosition(0);
        } else {
            emptyListMessage.setVisibility(View.VISIBLE);
        }
    }
}
