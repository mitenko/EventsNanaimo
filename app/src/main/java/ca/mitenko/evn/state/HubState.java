package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.model.Category;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.ImmutableDestSearch;
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
        EVENT_LIST
    }

    /**
     * State Identifying Tag
     */
    public static final String TAG = "state.hub";

    /**
     * Flag indicating the events have been loaded
     * @return
     */
    @Value.Default
    public boolean hasEvents() {
        return false;
    }

    /**
     * Map loaded state flag
     */
    @NonNull
    @Value.Default
    public DestSearch search() {
        return ImmutableDestSearch.builder().build();
    }

    /**
     * The set of category data
     */
    @Nullable
    @Value.Default
    public ArrayList<Category> categories() {
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
    public static HubState build(Destination selectedDest,
                     boolean hasEvents, ArrayList<Category> categories, FragmentType currentFragment,
                                 ArrayList<FragmentType> fragmentStack) {
        return ImmutableHubState.builder()
                .hasEvents(hasEvents)
                .selectedDest(selectedDest)
                .categories(categories)
                .currentFragment(currentFragment)
                .fragmentStack(fragmentStack)
                .build();
    }
}
