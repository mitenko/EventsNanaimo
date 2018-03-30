package ca.mitenko.evn.presenter

import ca.mitenko.evn.event.*
import ca.mitenko.evn.interactor.DestMapInteractor
import ca.mitenko.evn.model.search.DestSearch.Companion.DEFAULT_BOUNDS
import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.DestMapState
import ca.mitenko.evn.state.ImmutableDestMapState
import ca.mitenko.evn.ui.dest_map.DestMapView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

/**
 * Created by mitenko on 2017-04-23.
 */

class DestMapPresenter
/**
 * Constructor
 * @param view
 * @param curState
 */
(view: DestMapView, curState: DestMapState,
 bus: EventBus,
 /**
  * The interactor
  */
 internal var interactor: DestMapInteractor) : RootPresenter<DestMapView, DestMapState>(view, ImmutableDestMapState.builder().build(), curState, bus) {

    init {
        /**
         * if we don't have the search results, go into a loading state
         */
        if (!curState.search().hasResults()) {
            val newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .loadingResults(true)
                    .build()
            /**
             * Execute a search immediately if we have been
             * denied the user's current location
             */
            if (!curState.hasUserLocationPermission()) {
                interactor.getDestinations(curState.search())
            }
            render(newState)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: DestMapView?, curState: DestMapState, prevState: DestMapState) {
        var curState = curState
        if (view != null) {

            /**
             * Cut off point for rendering things if the map isn't ready
             */
            if (!curState.mapReady()) {
                return
            }

            /**
             * Display an error during the map load
             */
            if (curState.error() != null && curState.error() != prevState.error()) {
                view.showError(curState.error()?.message ?: "")
            }

            /**
             * Set destinations if we have them and the map just became ready
             */
            if (curState.mapReady() && !prevState.mapReady()) {
                if (curState.search().hasResults()) {
                    view.setDestinations(curState.search().filteredResults(false))
                }
                if (curState.search().hasMapBounds()) {
                    view.setMapBounds(curState.search().mapBounds ?: DEFAULT_BOUNDS, false)
                }
            }

            Timber.e(curState.search().filteredResults(false).toString())
            Timber.e(prevState.search().filteredResults(false).toString())
            if (curState.search().hasResults() &&
                    curState.search().filteredResults(false) != prevState.search().filteredResults(false)) {
                view.setDestinations(curState.search().filteredResults(false))
            }

            if (curState.search().mapBounds != null && curState.search().mapBounds != prevState.search().mapBounds) {
                view.setMapBounds(curState.search().mapBounds ?: DEFAULT_BOUNDS, true)
            }

            if (curState.recluster()) {
                view.recluster()
                curState = ImmutableDestMapState.builder()
                        .from(curState)
                        .recluster(false)
                        .build()
            }

            /**
             * Show the update button if the search results bounds
             * are out of sync with the current map bounds
             */
            if (curState.search().mapOutsideSearch()) {
                view.showUpdateButton()
            } else {
                view.hideUpdateButton()
            }

            if (curState.loadingResults()) {
                view.showProgressBar()
            } else {
                view.hideProgressBar()
            }

            if (curState.selectedItem() != null && curState.selectedItem() != prevState.selectedItem() || prevState.selectedItem() != null && prevState.selectedItem() != curState.selectedItem()) {
                view.setSelectedItem(curState.selectedItem())
            }

            if (curState.userLocation() != null && prevState.userLocation() == null) {
                view.setUserLocation(curState.userLocation() ?: DEFAULT_BOUNDS.center)
                if (curState.search().hasResults()) {
                    view.setDestinations(curState.search().filteredResults(false))
                }
            }
        }
    }

    /**
     * Called when the user location is known
     * @param event
     */
    @Subscribe(sticky = true)
    fun onUserLocationEvent(event: UserLocationEvent) {
        if (event.outcome === UserLocationEvent.Outcome.SUCCESS && event.userLatLng != null
                && event.userLatLng != curState.userLocation()) {

            val newFilter = curState.search().filter.copy(userLocation = event.userLatLng)
            val newSearch = curState.search().copy(filter = newFilter)

            val newState = ImmutableDestMapState.builder()
                    .from(curState)
                    .userLocation(event.userLatLng)
                    .search(newSearch)
                    .build()

            render(newState)

            if (curState.loadingResults()) {
                // Hit the API for the destinations within these bounds
                interactor.getDestinations(newSearch)
            } else {
                bus.postSticky(SearchEvent(newSearch))
            }
        }
    }

    /**
     * Called when the map becomes ready
     * @param event
     */
    @Subscribe
    fun onMapReady(event: MapReadyEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .mapReady(true)
                .build()
        render(newState)
    }

    /**
     * Called when the Map Bounds have changed
     * @param event
     */
    @Subscribe
    fun onMapBoundsEvent(event: MapBoundsEvent) {
        val newStateBuilder = ImmutableDestMapState.builder()
                .from(curState)
                .search(curState.search().copy(mapBounds = event.latLngBounds))
                .recluster(true)
                .selectedItem(null)

        render(newStateBuilder.build())

        // Push out the new search
        bus.postSticky(SearchEvent(curState.search()))
    }

    /**
     * Called when a map cluster is clicked
     * @param event
     */
    @Subscribe
    fun onMapClusterClickEvent(event: MapClusterClickEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(curState.search().copy(mapBounds = event.clusterBounds))
                .selectedItem(null)
                .build()

        render(newState)

        // Push out the new search
        bus.postSticky(SearchEvent(curState.search()))
    }

    /**
     * Called when the Search this Area is clicked
     * @param event
     */
    @Subscribe
    fun onUpdateMapClick(event: UpdateMapRequestEvent) {
        val newSearch = curState.search().copy(
                searchBounds = curState.search().mapBoundsOrDefault())

        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(newSearch)
                .loadingResults(true)
                .selectedItem(null)
                .build()
        render(newState)

        // Hit the API for the destinations within these bounds
        interactor.getDestinations(newSearch)

        // Don't push out a SearchEvent since the interactor will push one out
    }

    /**
     * Called when a new set of destinations is known
     * @param event
     */
    @Subscribe(sticky = true)
    fun onSearchEvent(event: SearchEvent) {
        if (event.search == curState.search()) {
            return
        }

        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .loadingResults(false)
                .search(event.search)
                .selectedItem(null)
                .build()
        render(newState)
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    fun onFilterEvent(event: FilterEvent) {
        if (event.filter == curState.search().filter) {
            return
        }
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(curState.search().copy(filter = event.filter))
                .selectedItem(null)
                .build()
        render(newState)
        // Push out the new search
        bus.postSticky(SearchEvent(curState.search()))
    }

    /**
     * Called when a map item has been clicked
     * @param event
     */
    @Subscribe
    fun onMapItemClickEvent(event: MapItemClickEvent) {
        // Make the selectedDestination null if we've clicked the same destination ie deselect
        val selectedDest = if (curState.selectedItem() != null && curState.selectedItem() == event.destination)
            null
        else
            event.destination

        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(selectedDest)
                .build()
        render(newState)
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    fun onCategoryResultEvent(event: CategoryResultEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .categoryMap(event.categoryResult)
                .build()
        render(newState)
    }

    /**
     * Called when a filtering event has occurred
     * @param event
     */
    @Subscribe
    fun onModifyFilterEvent(event: ModifyFilterEvent) {
        val newFilter = curState.search().filter.modify(event, curState.categoryMap())
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .search(curState.search().copy(filter = newFilter))
                .build()
        render(newState)
    }

    /**
     * Called when a filtering event has occurred
     * @param event
     */
    @Subscribe
    fun onErrorEvent(event: ErrorEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .loadingResults(false)
                .error(event.error)
                .build()
        render(newState)
    }

    /**
     * When the 'filter' button is clicked
     *
     * @param event
     */
    @Subscribe
    fun onFilterButtonEvent(event: ViewFilterEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(null)
                .build()
        render(newState)
    }

    /**
     * When a 'list' button is clicked
     *
     * @param event
     */
    @Subscribe
    fun onListButtonEvent(event: ViewListEvent) {
        val newState = ImmutableDestMapState.builder()
                .from(curState)
                .selectedItem(null)
                .build()
        render(newState)
    }

    /**
     * Called when the fragment is saving its state
     */
    fun onSaveInstanceState() {
        curState = ImmutableDestMapState.builder()
                .from(curState)
                .mapReady(false)
                .build()
    }
}
