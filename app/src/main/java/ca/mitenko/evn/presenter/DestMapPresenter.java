package ca.mitenko.evn.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.DestinationResultEvent;
import ca.mitenko.evn.event.MapBoundsEvent;
import ca.mitenko.evn.event.MapClusterClickEvent;
import ca.mitenko.evn.event.MapItemClickEvent;
import ca.mitenko.evn.event.MapReadyEvent;
import ca.mitenko.evn.event.UpdateMapRequestEvent;
import ca.mitenko.evn.interactor.DestMapInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Search;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.ImmutableDestSearch;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.DestMapState;
import ca.mitenko.evn.state.ImmutableDestMapState;
import ca.mitenko.evn.ui.dest_map.DestMapView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class DestMapPresenter extends RootPresenter<DestMapView, DestMapState> {
    /**
     * The interactor
     */
    DestMapInteractor interactor;

    /**
     * Constructor
     * @param view
     * @param curState
     */
    public DestMapPresenter(DestMapView view, DestMapState curState,
                            EventBus bus, DestMapInteractor interactor) {
        super(view, ImmutableDestMapState.builder().build(), curState, bus);
        this.interactor = interactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(DestMapView view, DestMapState curState, DestMapState prevState) {
        if (view != null) {

            /**
             * Cut off point for rendering things if the map isn't ready
             */
            if (!curState.mapReady()) {
                return;
            }

            /**
             * Set destinations if we have them and the map just became ready
             */
            if (curState.mapReady() && !prevState.mapReady() && curState.search().hasResults()) {
                view.setDestinations(curState.search().filteredResults());
            }

            if (curState.search().hasResults()
                    && !curState.search().filteredResults().equals(prevState.search().filteredResults())) {
                view.setDestinations(curState.search().filteredResults());
            }

            if (curState.mapBounds() != null &&
                    !curState.mapBounds().equals(prevState.mapBounds())) {
                view.setMapBounds(curState.mapBounds());
            }

            if (curState.recluster()) {
                view.recluster();
                curState = ImmutableDestMapState.builder()
                        .from(curState)
                        .recluster(false)
                        .build();
            }

            /**
             * Show the update button if the search results bounds
             * are out of sync with the current map bounds
             */
            if (curState.mapBounds() != null) {
                LatLngBounds searchBounds = curState.search().bounds();
                LatLngBounds mapBounds = curState.mapBounds();
                if (!searchBounds.contains(mapBounds.northeast) ||
                        !searchBounds.contains(mapBounds.southwest)) {
                    view.showUpdateButton();
                } else {
                    view.hideUpdateButton();
                }
            }

            if (curState.loadingResults()) {
                view.showProgressBar();
            } else {
                view.hideProgressBar();
            }

            if ((curState.selectedItem() != null &&
                    !curState.selectedItem().equals(prevState.selectedItem()))
                    ||
                (prevState.selectedItem() != null &&
                        !prevState.selectedItem().equals(curState.selectedItem()))){
                view.setSelectedItem(curState.selectedItem());
            }
        }
    }

    /**
     * Called when the map becomes ready
     * @param event
     */
    @Subscribe
    public void onMapReady(MapReadyEvent event) {
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .mapReady(true)
                .build();
        render(newState);
    }

    /**
     * Called when the Map Bounds have changed
     * @param event
     */
    @Subscribe
    public void onMapBoundsEvent(MapBoundsEvent event) {
        ImmutableDestMapState.Builder newStateBuilder =
                ImmutableDestMapState.builder()
                        .from(curState)
                        .recluster(true);

        /**
         * Set the state bounds if the event bounds are outside
         */
        if (curState.mapBounds() == null ||
                !(curState.mapBounds().contains(event.getLatLngBounds().northeast)
                        && curState.mapBounds().contains(event.getLatLngBounds().southwest))) {
            newStateBuilder
                .mapBounds(event.getLatLngBounds());
        }

        /**
         * Force a new search if destinations == null
         */
        if (!curState.search().hasResults()) {
            // Hit the API for the destinations within these bounds
            interactor.getDestinations(curState.search());
        }
        render(newStateBuilder.build());
    }

    /**
     * Called when a map cluster is clicked
     * @param event
     */
    @Subscribe
    public void onMapClusterClickEvent(MapClusterClickEvent event) {
        ImmutableDestMapState newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .mapBounds(event.getClusterBounds())
                    .build();
        render(newState);
    }

    /**
     * Called when the Search this Area is clicked
     * @param event
     */
    @Subscribe
    public void onUpdateMapClick(UpdateMapRequestEvent event) {
        DestSearch newSearch = ImmutableDestSearch.builder()
                .from(curState.search())
                .bounds(curState.mapBounds())
                .build();

        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(newSearch)
                .loadingResults(true)
                .build();
        render(newState);

        // Hit the API for the destinations within these bounds
        interactor.getDestinations(newSearch);
    }


    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onDestinationResultEvent(DestinationResultEvent event) {
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .loadingResults(false)
                .search(event.getSearch())
                .build();
        render(newState);
    }

    /**
     * Called when a map item has been clicked
     * @param event
     */
    @Subscribe
    public void onMapItemClickEvent(MapItemClickEvent event) {
        // Make the selectedDestination null if we've clicked the same destination ie deselect
        Destination selectedDest =
            (curState.selectedItem() != null
                    && curState.selectedItem().equals(event.getDestination())) ?
            null : event.getDestination();

        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(selectedDest)
                .build();
        render(newState);
    }
}
