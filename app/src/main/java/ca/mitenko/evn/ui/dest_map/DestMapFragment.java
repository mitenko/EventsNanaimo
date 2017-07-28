package ca.mitenko.evn.ui.dest_map;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.DestItemClickEvent;
import ca.mitenko.evn.event.UpdateMapRequestEvent;
import ca.mitenko.evn.event.ViewListEvent;
import ca.mitenko.evn.interactor.DestMapInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.DestMapPresenter;
import ca.mitenko.evn.state.ImmutableDestMapState;
import ca.mitenko.evn.ui.common.RootFragment;
import ca.mitenko.evn.ui.dest_map.map.DestMap;
import retrofit2.Retrofit;

/**
 * Created by mitenko on 2017-04-22.
 */
public class DestMapFragment extends RootFragment
    implements DestMapView, View.OnClickListener {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.dest_map";

    /**
     * Hub Fragment container
     */
    @BindView(R.id.map_container)
    ConstraintLayout mapContainer;

    /**
     * The Map View
     */
    @BindView(R.id.mapview)
    DestMap destMap;

    /**
     * The Selected Destination Card View
     */
    @BindView(R.id.dest_card_view)
    DestCardView selectedDestView;

    /**
     * The Map View
     */
    @BindView(R.id.list_button)
    FloatingActionButton listButton;

    /**
     * The Progress Bar Container
     */
    @BindView(R.id.map_progress_container)
    FrameLayout progressBarContainer;

    /**
     * The Progress Bar Container
     */
    @BindView(R.id.map_progress_view)
    ProgressView progressBar;

    /**
     * The Update Search Button
     */
    @BindView(R.id.update_search_button)
    TextView updateSearchButton;

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
     * The selected destination
     */
    private Destination selectedDestination = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_dest_map, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EvNApplication) getActivity().getApplication())
                .getApplicationComponent().inject(this);

        // Init the Dest Map
        destMap.onMapCreate(savedInstanceState);
        destMap.setBus(bus);

        // Init buttons
        listButton.setOnClickListener(this);
        selectedDestView.setOnClickListener(this);
        updateSearchButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DestMapInteractor interactor =
                new DestMapInteractor(retrofit, bus, evnService);

        // Init presenter
        presenter = new DestMapPresenter(
                this, ImmutableDestMapState.builder().build(), bus, interactor);
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
        hub.showCategoryPicker(true);
    }

    /**
     * Called to define the toolbar
     */

    /**
     * Presenter / View Callbacks
     */
    /**
     * {@inheritDoc}
     */
    public void setDestinations(ArrayList<Destination> destinations) {
        destMap.setDestinationResult(destinations);
    }

    /**
     * {@inheritDoc}
     * @param destination
     */
    public void setSelectedItem(Destination destination) {
        this.selectedDestination = destination;
        TransitionManager.beginDelayedTransition(mapContainer);
        if (destination != null) {
            selectedDestView.bind(destination);
            selectedDestView.setVisibility(View.VISIBLE);
        } else {
            selectedDestView.setVisibility(View.GONE);
        }
    }

    /**
     * Recluster
     */
    public void recluster() {
        destMap.recluster();
    }

    /**
     * Shows the Update button
     */
    public void showUpdateButton() {
        updateSearchButton.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the Update button
     */
    public void hideUpdateButton() {
        updateSearchButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the Progress Bar
     */
    public void showProgressBar() {
        if (progressBarContainer.getVisibility() == View.VISIBLE) {
            return;
        }
        progressBarContainer.setVisibility(View.VISIBLE);
        progressBar.start();
    }

    /**
     * Hides the Progress Bar
     */
    public void hideProgressBar() {
        if (progressBarContainer.getVisibility() == View.INVISIBLE) {
            return;
        }
        progressBar.stop();

        /**
         * Hide the view once the animation has completed
         */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progressBarContainer.setVisibility(View.INVISIBLE);
            }
        };
        Integer outAnimationDuration =
                getContext().getResources().getInteger(R.integer.map_progress_animation_duration);
        progressBar.postDelayed(runnable, outAnimationDuration);
    }

    /**
     * Button Events
     */
    public void onClick(View view) {
        if (view.equals(listButton)) {
            bus.post(new ViewListEvent());
        }
        if (view.equals(selectedDestView)) {
            bus.post(new DestItemClickEvent(selectedDestination));
        }
        if (view.equals(updateSearchButton)) {
            bus.post(new UpdateMapRequestEvent());
        }
    }

    /**
     * On Start
     */
    @Override
    public void onStart() {
        if (destMap != null) {
            destMap.onMapStart();
        }
        super.onStart();
    }

    /**
     * On Resume
     */
    @Override
    public void onResume() {
        if(destMap != null) {
            destMap.onMapResume();
        }
        super.onResume();
    }

    /**
     * On Pause
     */
    @Override
    public void onPause() {
        if(destMap != null) {
            destMap.onMapPause();
        }
        super.onPause();
    }

    /**
     * On Destroy
     */
    @Override
    public void onDestroy() {
        if(destMap != null) {
            destMap.onMapDestroy();
        }
        super.onDestroy();
    }

    /**
     * On Low Memory
     */
    @Override
    public void onLowMemory() {
        if(destMap != null) {
            destMap.onMapLowMemory();
        }
        super.onLowMemory();
    }

    /**
     * On Save Instance State
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(destMap != null) {
            Bundle mapState = new Bundle();
            destMap.onMapSaveInstanceState(mapState);
        }
        super.onSaveInstanceState(outState);
    }
}
