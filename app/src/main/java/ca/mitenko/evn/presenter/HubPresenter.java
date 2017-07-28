package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.CategoryResultEvent;
import ca.mitenko.evn.event.DestItemClickEvent;
import ca.mitenko.evn.event.EventResultEvent;
import ca.mitenko.evn.event.ViewEventEvent;
import ca.mitenko.evn.event.ViewListEvent;
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.interactor.CategoryInteractor;
import ca.mitenko.evn.interactor.EventListInteractor;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.hub.HubView;

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
        if (!curState.hasEvents()) {
            eventInteractor.getEvents();
        }
        if (curState.categories() == null) {
            categoryInteractor.getCategories();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(HubView view, HubState curState, HubState prevState) {
        if (view != null) {
            // Dest Map Fragment
            if (curState.showDestMap()
                    && curState.showDestMap() != prevState.showDestMap()) {
                view.showDestMap();
            }

            // Dest List Fragment
            if (curState.showDestList()
                    && curState.showDestList() != prevState.showDestList()) {
                view.showDestList();
            }

            // Dest List Fragment
            if (curState.showEventList()
                    && curState.showEventList() != prevState.showEventList()) {
                view.showEventList();
            }

            // Dest Detail Fragment
            if (curState.showDestDetail()
                    && curState.showDestDetail() != prevState.showDestDetail()) {
                view.showDestDetail(curState.selectedDest());
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
        HubState newState = ImmutableHubState.builder()
                .showDestList(true)
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
        HubState newState = ImmutableHubState.builder()
                .showDestMap(true)
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
        HubState newState = ImmutableHubState.builder()
                .showEventList(true)
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
                .hasEvents(true)
                .build();
        curState = newState;
    }

    /**
     * When a destination item has been clicked
     * @param event
     */
    @Subscribe
    public void onDestItemClickEvent(DestItemClickEvent event) {
        HubState newState = ImmutableHubState.builder()
                .showDestDetail(true)
                .selectedDest(event.getDestination())
                .build();
        render(newState);
    }

    /**
     * When the categories have been loaded
     * @param event
     */
    @Subscribe(sticky = true)
    public void onCategoryResultEvent(CategoryResultEvent event) {
        HubState newState = ImmutableHubState.builder()
                .categories(event.getCategoryResult())
                .build();
        render(newState);
    }
}
