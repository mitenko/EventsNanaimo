package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.state.common.RootState;

/**
 * Created by mitenko on 2017-04-23.
 */
@Parcel(value = Parcel.Serialization.VALUE, implementations = ImmutableHubState.class)
@Value.Immutable
public class HubState extends RootState {
    /**
     * Set of possible fragments that can currently be displayed
     */
    public enum FragmentType {
        DEST_MAP,
        DEST_LIST,
        DEST_DETAIL,
        EVENT_DETAIL,
        EVENT_LIST,
        FILTER
    }

    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.hub";

    /**
     * Events flag
     */
    @Nullable
    @Value.Default
    public ArrayList<Event> events() {
        return null;
    }

    /**
     * Events flag
     */
    @NonNull
    @Value.Default
    public boolean locationRequested() {
        return false;
    }

    /**
     * Events flag
     */
    @NonNull
    @Value.Default
    public boolean haveLocationPermission() {
        return false;
    }

    /**
     * The filter
     */
    @NonNull
    @Value.Default
    public Filter filter() {
        return new Filter();
    }

    /**
     * The set of category data
     */
    @Nullable
    @Value.Default
    public HashMap<String, ArrayList<Activity>> categoryMap() {
        return null;
    }

    /**
     * The selected destination for viewing
     */
    @Nullable
    @Value.Default
    public Destination selectedDest() {
        return null;
    }

    /**
     * The selected event for viewing
     */
    @Nullable
    @Value.Default
    public Event selectedEvent() {
        return null;
    }

    /**
     * The current fragment
     */
    @Nullable
    @Value.Default
    public FragmentType currentFragment() {
        return null;
    }

    /**
     * The fragment back stack
     */
    @NonNull
    @Value.Default
    public ArrayList<FragmentType> fragmentStack() {
        return new ArrayList<>();
    }

    /**
     * The state is shutting down
     * @return
     */
    @Value.Default
    public boolean shutdown() {
        return false;
    }

    /**
     * Parcel factory to allow parcelling immutables
     * @return
     */
    @ParcelFactory
    public static HubState build(Destination selectedDest, Filter filter, boolean locationRequested,
                                 ArrayList<Event> events, HashMap<String, ArrayList<Activity>> categoryMap, FragmentType currentFragment,
                                 ArrayList<FragmentType> fragmentStack, Event selectedEvent, boolean haveLocationPermission) {
        return ImmutableHubState.builder()
                .events(events)
                .selectedDest(selectedDest)
                .categoryMap(categoryMap)
                .currentFragment(currentFragment)
                .fragmentStack(fragmentStack)
                .filter(filter)
                .locationRequested(locationRequested)
                .selectedEvent(selectedEvent)
                .haveLocationPermission(haveLocationPermission)
                .build();
    }
}
