package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import ca.mitenko.evn.event.CategoryResultEvent;
import ca.mitenko.evn.event.DestItemClickEvent;
import ca.mitenko.evn.event.EventResultEvent;
import ca.mitenko.evn.event.FilterEvent;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.event.ViewEventEvent;
import ca.mitenko.evn.event.ViewFilterEvent;
import ca.mitenko.evn.event.ViewListEvent;
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.interactor.CategoryInteractor;
import ca.mitenko.evn.interactor.EventListInteractor;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.HubState.*;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.hub.HubView;

import static ca.mitenko.evn.state.HubState.FragmentType.DEST_DETAIL;
import static ca.mitenko.evn.state.HubState.FragmentType.DEST_LIST;
import static ca.mitenko.evn.state.HubState.FragmentType.DEST_MAP;
import static ca.mitenko.evn.state.HubState.FragmentType.EVENT_LIST;
import static ca.mitenko.evn.state.HubState.FragmentType.FILTER;

/**
 * Created by mitenko on 2017-04-23.
 */

public class HubPresenter extends RootPresenter<HubView, HubState> {
    /**
     * Query for events
     */
    EventListInteractor eventInteractor;

    /**
     * Query for categories
     */
    CategoryInteractor categoryInteractor;

    /**
     * Constructor
     *
     * @param view
     * @param curState
     */
    public HubPresenter(HubView view, HubState curState, EventBus bus,
                        EventListInteractor eventInteractor,
                        CategoryInteractor categoryInteractor) {
        super(view, ImmutableHubState.builder().build(), curState, bus);
        this.eventInteractor = eventInteractor;
        this.categoryInteractor = categoryInteractor;
        if (curState.events() == null) {
            eventInteractor.getEvents();
        }
        if (curState.categoryMap() == null) {
            categoryInteractor.getCategories();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(HubView view, HubState curState, HubState prevState) {
        if (view != null) {
            if (curState.currentFragment() != null &&
                    !curState.currentFragment().equals(prevState.currentFragment())) {
                FragmentType currentFragment = curState.currentFragment();
                switch(currentFragment) {
                    case DEST_MAP:
                        view.showDestMap();
                        break;
                    case DEST_LIST:
                        view.showDestList();
                        break;
                    case EVENT_LIST:
                        view.showEventList();
                        break;
                    case DEST_DETAIL:
                        view.showDestDetail(curState.selectedDest());
                        break;
                    case FILTER:
                        view.showFilterView();
                        break;
                }
            }

            // Update the filter buttons
            if (!curState.filter().equals(prevState.filter())) {
                view.applyFilterToView(curState.filter());
            }

            // Shutdown
            if (curState.shutdown()) {
                view.shutdown();
            }

            // Request the user location
            if (!curState.locationRequested()) {
                view.getUserLocation();
            }
        }
    }

    /**
     * When a 'list' button is clicked
     *
     * @param event
     */
    @Subscribe
    public void onListButtonEvent(ViewListEvent event) {
        if (curState.currentFragment() == DEST_LIST) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_LIST)
                .fragmentStack(setFragmentStack(DEST_LIST))
                .build();
        render(newState);
    }

    /**
     * When a 'map' button is clicked
     *
     * @param event
     */
    @Subscribe
    public void onMapButtonEvent(ViewMapEvent event) {
        if (curState.currentFragment() == DEST_MAP) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_MAP)
                .fragmentStack(setFragmentStack(DEST_MAP))
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
        if (curState.currentFragment() == FILTER) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(FILTER)
                .fragmentStack(setFragmentStack(FILTER))
                .build();
        render(newState);
    }

    /**
     * When the events button is clicked
     *
     * @param event
     */
    @Subscribe
    public void onEventButtonEvent(ViewEventEvent event) {
        if (curState.currentFragment() == EVENT_LIST) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(EVENT_LIST)
                .fragmentStack(setFragmentStack(EVENT_LIST))
                .build();
        render(newState);
    }

    /**
     * When a destination item has been clicked
     * @param event
     */
    @Subscribe
    public void onDestItemClickEvent(DestItemClickEvent event) {
        if (curState.currentFragment() == DEST_DETAIL) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .currentFragment(DEST_DETAIL)
                .selectedDest(event.getDestination())
                .fragmentStack(setFragmentStack(DEST_DETAIL))
                .build();
        render(newState);
    }

    /**
     * Happens when the events have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    public void onEventResultEvent(EventResultEvent event) {
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .events(event.getEvents())
                .build();
        curState = newState;
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    public void onCategoryResultEvent(CategoryResultEvent event) {
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .categoryMap(event.getCategoryResult())
                .build();
        render(newState);
    }

    /**
     * Called when the Filter as a whole has been updated
     * @param event
     */
    @Subscribe(sticky = true)
    public void onFilterEvent(FilterEvent event) {
        if (event.getFilter().equals(curState.filter())) {
            return;
        }
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .filter(event.getFilter())
                .build();
        render(newState);
    }

    /**
     * Called when one of the filter buttons is pressed
     * @param event
     */
    public void onModifyFilterEvent(ModifyFilterEvent event) {
        Filter newFilter = curState.filter().modify(event, curState.categoryMap());
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .filter(newFilter)
                .build();
        render(newState);
        bus.postSticky(new FilterEvent(newFilter));
    }

    /**
     * Called when the back button is pressed
     */
    public void onBackPressed() {
        ArrayList<FragmentType> fragmentStack = curState.fragmentStack();
        HubState newState;
        if (fragmentStack.isEmpty()) {
            newState = ImmutableHubState.builder()
                    .from(curState)
                    .shutdown(true)
                    .build();
        } else {
            FragmentType poppedFragment = fragmentStack.remove(fragmentStack.size()-1);
            newState = ImmutableHubState.builder()
                    .from(curState)
                    .currentFragment(poppedFragment)
                    .fragmentStack(fragmentStack)
                    .build();
        }
        render(newState);
    }

    /**
     * Sets the fragment stack
     * @return
     */
    public ArrayList<FragmentType> setFragmentStack(FragmentType newCurrentFragment) {
        ArrayList<FragmentType> fragmentStack = new ArrayList<>();
        if (newCurrentFragment != DEST_MAP && curState.currentFragment() != null) {
            fragmentStack = curState.fragmentStack();
            fragmentStack.add(curState.currentFragment());
        }
        return fragmentStack;
    }

    /**
     * Called after we've requested the user's location
     */
    public void onLocationRequested() {
        HubState newState = ImmutableHubState.builder()
                .from(curState)
                .locationRequested(true)
                .build();
        render(newState);
    }
}
