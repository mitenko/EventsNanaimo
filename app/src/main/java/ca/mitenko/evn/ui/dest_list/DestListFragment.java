package ca.mitenko.evn.ui.dest_list;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.DestListPresenter;
import ca.mitenko.evn.state.ImmutableDestListState;
import ca.mitenko.evn.ui.common.RootFragment;
import ca.mitenko.evn.ui.dest_list.adapter.DestListAdapter;
import retrofit2.Retrofit;

/**
 * Created by mitenko on 2017-04-22.
 */

public class DestListFragment extends RootFragment
    implements DestListView, View.OnClickListener {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.dest_list";

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
     * The Map View
     */
    @BindView(R.id.map_button)
    FloatingActionButton mapButton;

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
    private DestListAdapter adapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_dest_list, container, false);
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
        adapter = new DestListAdapter(bus);
        recyclerView.setAdapter(adapter);

        // Init buttons
        mapButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init presenter
        presenter = new DestListPresenter(
                this, ImmutableDestListState.builder().build(), bus);

        setToolbar();
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
        }
    }

    /**
     * Sets the tool bar to show the proper title
     */
    public void setToolbar() {
        hub.setToolbarTitle(getString(R.string.dest_map_toolbar_title));
        hub.showCategoryPicker(false);
    }

    /**
     * Presenter / View Callbacks
     */
    /**
     * {@inheritDoc}
     */
    public void setDestinations(ArrayList<Destination> destinations) {
        adapter.setDestinationResult(destinations);
    }

    /**
     * Button Events
     */
    public void onClick(View view) {
        if (view.equals(mapButton)) {
            bus.post(new ViewMapEvent());
        }
    }
}
