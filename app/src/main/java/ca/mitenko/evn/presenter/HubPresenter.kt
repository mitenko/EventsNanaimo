package ca.mitenko.evn.presenter

import ca.mitenko.evn.event.*
import ca.mitenko.evn.interactor.CategoryInteractor
import ca.mitenko.evn.interactor.EventListInteractor
import ca.mitenko.evn.presenter.common.RootPresenter
import ca.mitenko.evn.state.HubState
import ca.mitenko.evn.state.HubState.FragmentType
import ca.mitenko.evn.state.HubState.FragmentType.*
import ca.mitenko.evn.state.ImmutableHubState
import ca.mitenko.evn.ui.hub.HubView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * Created by mitenko on 2017-04-23.
 */

class HubPresenter
/**
 * Constructor
 *
 * @param view
 * @param curState
 */
(view: HubView, curState: HubState, bus: EventBus,
 /**
  * Query for events
  */
 internal var eventInteractor: EventListInteractor,
 /**
  * Query for categories
  */
 internal var categoryInteractor: CategoryInteractor) : RootPresenter<HubView, HubState>(view, ImmutableHubState.builder().build(), curState, bus) {

    init {
        if (curState.events() == null) {
            eventInteractor.getEvents()
        }
        if (curState.categoryMap() == null) {
            categoryInteractor.getCategories()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun renderState(view: HubView?, curState: HubState, prevState: HubState) {
        if (view != null) {
            if (curState.currentFragment() != null && curState.currentFragment() != prevState.currentFragment()) {
                val currentFragment = curState.currentFragment()
                when (currentFragment) {
                    DEST_MAP -> view.showDestMap()
                    DEST_LIST -> view.showDestList()
                    EVENT_LIST -> view.showEventList()
                    DEST_DETAIL -> view.showDestDetail(curState.selectedDest())
                    EVENT_DETAIL -> view.showEventDetail(curState.selectedEvent())
                    FILTER -> view.showFilterView()
                }
            }

            // Update the filter buttons
            if (curState.filter() != prevState.filter()) {
                view.applyFilterToView(curState.filter())
            }

            // Shutdown
            if (curState.shutdown()) {
                view.shutdown()
            }

            // Request the user location
            if (curState.haveLocationPermission() && !curState.locationRequested()) {
                view.getUserLocation()
            }
        }
    }

    /**
     * Called once we have location permission
     */
    fun onLocationPermissionGranted(havePermission: Boolean) {
        val newState = ImmutableHubState.builder()
                .from(curState)
                .haveLocationPermission(havePermission)
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
        if (curState.currentFragment() == DEST_LIST) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_LIST)
                .fragmentStack(setFragmentStack(DEST_LIST))
                .build()
        render(newState)
    }

    /**
     * When a 'map' button is clicked
     *
     * @param event
     */
    @Subscribe
    fun onMapButtonEvent(event: ViewMapEvent) {
        if (curState.currentFragment() == DEST_MAP) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_MAP)
                .fragmentStack(setFragmentStack(DEST_MAP))
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
        if (curState.currentFragment() == FILTER) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(FILTER)
                .fragmentStack(setFragmentStack(FILTER))
                .build()
        render(newState)
    }

    /**
     * When the events button is clicked
     *
     * @param event
     */
    @Subscribe
    fun onEventButtonEvent(event: ViewEventEvent) {
        if (curState.currentFragment() == EVENT_LIST) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(EVENT_LIST)
                .fragmentStack(setFragmentStack(EVENT_LIST))
                .build()
        render(newState)
    }

    /**
     * When a destination item has been clicked
     * @param event
     */
    @Subscribe
    fun onDestItemClickEvent(event: DestItemClickEvent) {
        if (curState.currentFragment() == DEST_DETAIL) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_DETAIL)
                .selectedDest(event.destination)
                .fragmentStack(setFragmentStack(DEST_DETAIL))
                .build()
        render(newState)
    }

    /**
     * When a destination item has been clicked
     * @param event
     */
    @Subscribe
    fun onEventItemClickEvent(event: EventItemClickEvent) {
        if (curState.currentFragment() == EVENT_DETAIL) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(EVENT_DETAIL)
                .selectedEvent(event.event)
                .fragmentStack(setFragmentStack(EVENT_DETAIL))
                .build()
        render(newState)
    }

    /**
     * Happens when the events have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    fun onEventResultEvent(event: EventResultEvent) {
        val newState = ImmutableHubState.builder()
                .from(curState)
                .events(event.events)
                .build()
        curState = newState
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    fun onCategoryResultEvent(event: CategoryResultEvent) {
        val newState = ImmutableHubState.builder()
                .from(curState)
                .categoryMap(event.categoryResult)
                .build()
        render(newState)
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    fun onFilterEvent(event: FilterEvent) {
        if (event.filter == curState.filter()) {
            return
        }
        val newState = ImmutableHubState.builder()
                .from(curState)
                .filter(event.filter)
                .build()
        render(newState)
    }

    /**
     * Called when one of the filter buttons is pressed
     * @param event
     */
    fun onModifyFilterEvent(event: ModifyFilterEvent) {
        val newFilter = curState.filter().modify(event, curState.categoryMap()!!)
        val newState = ImmutableHubState.builder()
                .from(curState)
                .filter(newFilter)
                .build()
        render(newState)
        bus.postSticky(FilterEvent(newFilter))
    }

    /**
     * Called when the back button is pressed
     */
    fun onBackPressed() {
        val fragmentStack = curState.fragmentStack()
        val newState: HubState
        if (fragmentStack.isEmpty()) {
            newState = ImmutableHubState.builder()
                    .from(curState)
                    .shutdown(true)
                    .build()
        } else {
            val poppedFragment = fragmentStack.removeAt(fragmentStack.size - 1)
            newState = ImmutableHubState.builder()
                    .from(curState)
                    .currentFragment(poppedFragment)
                    .fragmentStack(fragmentStack)
                    .build()
        }
        render(newState)
    }

    /**
     * Sets the fragment stack
     * @return
     */
    fun setFragmentStack(newCurrentFragment: FragmentType): ArrayList<FragmentType> {
        var fragmentStack = ArrayList<FragmentType>()
        val curFragment = curState.currentFragment()
        if (newCurrentFragment != DEST_MAP && curFragment != null) {
            fragmentStack = curState.fragmentStack()
            fragmentStack.add(curFragment)
        }
        return fragmentStack
    }
}
