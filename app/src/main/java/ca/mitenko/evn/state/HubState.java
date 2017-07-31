package ca.mitenko.evn.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.ArrayList;

import ca.mitenko.evn.model.Category;
import ca.mitenko.evn.model.Destination;
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
     * Dest Map Fragment showing
     * @return
     */
    @Value.Default
    public boolean showDestDetail() {
        return false;
    }

    /**
     * Dest Map Fragment showing
     * @return
     */
    @Value.Default
    public boolean showDestMap() {
        return false;
    }

    /**
     * Dest List Fragment showing
     * @return
     */
    @Value.Default
    public boolean showDestList() {
        return false;
    }

    /**
     * Event List Fragment showing
     * @return
     */
    @Value.Default
    public boolean showEventList() {
        return false;
    }

    /**
     * Detail Fragment showing
     * @return
     */
    @Value.Default
    public boolean showDetail() {
        return false;
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
     * @param showDestMap
     * @param showDestList
     * @param showEventList
     * @param showDetail
     * @return
     */
    @ParcelFactory
    public static HubState build(boolean showDestMap,
                     boolean showDestList, boolean showEventList, boolean showDetail,
                     boolean showDestDetail, Destination selectedDest,
                     boolean hasEvents) {
        return ImmutableHubState.builder()
                .showDestMap(showDestMap)
                .showDestList(showDestList)
                .showEventList(showEventList)
                .showDetail(showDetail)
                .hasEvents(hasEvents)
                .showDestDetail(showDestDetail)
                .selectedDest(selectedDest)
                .build();
    }
}
