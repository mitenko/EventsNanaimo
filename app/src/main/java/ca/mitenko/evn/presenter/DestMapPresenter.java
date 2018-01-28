package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.CategoryResultEvent;
import ca.mitenko.evn.event.FilterEvent;
import ca.mitenko.evn.event.MapBoundsEvent;
import ca.mitenko.evn.event.MapClusterClickEvent;
import ca.mitenko.evn.event.MapItemClickEvent;
import ca.mitenko.evn.event.MapReadyEvent;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.event.SearchEvent;
import ca.mitenko.evn.event.UpdateMapRequestEvent;
import ca.mitenko.evn.event.UserLocationEvent;
import ca.mitenko.evn.event.ViewFilterEvent;
import ca.mitenko.evn.event.ViewListEvent;
import ca.mitenko.evn.interactor.DestMapInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.ImmutableFilter;
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
        /**
         * if we don't have the search results, go into a loading state
         */
        if (!curState.search().hasResults()) {
            DestMapState newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .loadingResults(true)
                    .build();
            /**
             * Execute a search immediately if we have been
             * denied the user's current location
             */
            if (!curState.hasUserLocationPermission()) {
                interactor.getDestinations(curState.search());
            }
            render(newState);
        }
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
            if (curState.mapReady() && !prevState.mapReady()) {
                if (curState.search().hasResults()) {
                    view.setDestinations(curState.search().filteredResults(false));
                }
                if (curState.search().hasMapBounds()) {
                    view.setMapBounds(curState.search().getMapBounds(), false);
                }
            }

            if (curState.search().hasResults()
                    && !curState.search().filteredResults(false)
                    .equals(prevState.search().filteredResults(false))) {
                view.setDestinations(curState.search().filteredResults(false));
            }

            if (curState.search().getMapBounds() != null &&
                    !curState.search().getMapBounds().equals(prevState.search().getMapBounds())) {
                view.setMapBounds(curState.search().getMapBounds(), true);
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
            if (curState.search().mapOutsideSearch()) {
                view.showUpdateButton();
            } else {
                view.hideUpdateButton();
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

            if (curState.userLocation() != null && prevState.userLocation() == null) {
                view.setUserLocation(curState.userLocation());
                if (curState.search().hasResults()) {
                    view.setDestinations(curState.search().filteredResults(false));
                }
            }
        }
    }

    /**
     * Called when the user location is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onUserLocationEvent(UserLocationEvent event) {
        if (event.getOutcome() == UserLocationEvent.Outcome.SUCCESS && event.getUserLatLng() != null
                && !event.getUserLatLng().equals(curState.userLocation())) {

            ImmutableFilter newFilter = ImmutableFilter.builder()
                    .from(curState.search().getFilter())
                    .userLocation(event.getUserLatLng())
                    .build();

            ImmutableDestSearch newSearch = ImmutableDestSearch.builder()
                    .from(curState.search())
                    .filter(newFilter)
                    .build();

            DestMapState newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .userLocation(event.getUserLatLng())
                    .search(newSearch)
                    .build();

            render(newState);

            if (curState.loadingResults()) {
                // Hit the API for the destinations within these bounds
                interactor.getDestinations(newSearch);
            } else {
                bus.postSticky(new SearchEvent(newSearch));
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
        ImmutableDestSearch newSearch = ImmutableDestSearch.builder()
                    .from(curState.search())
                    .mapBounds(event.getLatLngBounds())
                    .build();

        ImmutableDestMapState.Builder newStateBuilder = ImmutableDestMapState.builder()
                    .from(curState)
                    .search(newSearch)
                    .recluster(true)
                    .selectedItem(null);

        render(newStateBuilder.build());

        // Push out the new search
        bus.postSticky(new SearchEvent(curState.search()));
    }

    /**
     * Called when a map cluster is clicked
     * @param event
     */
    @Subscribe
    public void onMapClusterClickEvent(MapClusterClickEvent event) {
        ImmutableDestSearch newSearch = ImmutableDestSearch.builder()
                .from(curState.search())
                .mapBounds(event.getClusterBounds())
                .build();

        ImmutableDestMapState newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .search(newSearch)
                    .selectedItem(null)
                    .build();

        render(newState);

        // Push out the new search
        bus.postSticky(new SearchEvent(curState.search()));
    }

    /**
     * Called when the Search this Area is clicked
     * @param event
     */
    @Subscribe
    public void onUpdateMapClick(UpdateMapRequestEvent event) {
        DestSearch newSearch = ImmutableDestSearch.builder()
                .from(curState.search())
                .searchBounds(curState.search().mapBoundsOrDefault())
                .build();

        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(newSearch)
                .loadingResults(true)
                .selectedItem(null)
                .build();
        render(newState);

        // Hit the API for the destinations within these bounds
        interactor.getDestinations(newSearch);

        // Don't push out a SearchEvent since the interactor will push one out
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    public void onSearchEvent(SearchEvent event) {
        if (event.getSearch().equals(curState.search())) {
            return;
        }

        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .loadingResults(false)
                .search(event.getSearch())
                .selectedItem(null)
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
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(ImmutableDestSearch.builder()
                    .from(curState.search())
                    .filter(event.getFilter())
                    .build())
                .selectedItem(null)
                .build();
        render(newState);
        // Push out the new search
        bus.postSticky(new SearchEvent(curState.search()));
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

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    public void onCategoryResultEvent(CategoryResultEvent event) {
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .categoryMap(event.getCategoryResult())
                .build();
        render(newState);
    }

    /**
     * Called when a filtering event has occurred
     * @param event
     */
    @Subscribe
    public void onModifyFilterEvent(ModifyFilterEvent event) {
        DestSearch newSearch = ImmutableDestSearch.builder()
                .from(curState.search())
                .filter(curState.search().getFilter().modify(event, curState.categoryMap()))
                .build();

        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(newSearch)
                .build();
        render(newState);
    }

    /**
     * When the 'filter' button is clicked
     *
     * @param event
     */
    @Subscribe
    public void onFilterButtonEvent(ViewFilterEvent event) {
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(null)
                .build();
        render(newState);
    }

    /**
     * When a 'list' button is clicked
     *
     * @param event
     */
    @Subscribe
    public void onListButtonEvent(ViewListEvent event) {
        DestMapState newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(null)
                .build();
        render(newState);
    }

    /**
     * Called when the fragment is saving its state
     */
    public void onSaveInstanceState() {
        curState = ImmutableDestMapState.builder()
                .from(curState)
                .mapReady(false)
                .build();
    }
}
