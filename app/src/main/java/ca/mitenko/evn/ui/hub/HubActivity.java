package ca.mitenko.evn.ui.hub;

import android.Manifest;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.event.ViewEventEvent;
import ca.mitenko.evn.event.ViewFilterEvent;
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.interactor.CategoryInteractor;
import ca.mitenko.evn.interactor.EventListInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.HubPresenter;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragment;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragmentBuilder;
import ca.mitenko.evn.ui.dest_list.DestListFragment;
import ca.mitenko.evn.ui.dest_map.DestMapFragment;
import ca.mitenko.evn.ui.event_detail.EventDetailFragment;
import ca.mitenko.evn.ui.event_detail.EventDetailFragmentBuilder;
import ca.mitenko.evn.ui.event_list.EventListFragment;
import ca.mitenko.evn.ui.filter.FilterFragment;
import ca.mitenko.evn.ui.filter.FilterFragmentBuilder;
import ca.mitenko.evn.util.UserLocationUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;

import static ca.mitenko.evn.CategoryConstants.ACCOMMODATION;
import static ca.mitenko.evn.CategoryConstants.BEVERAGES;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.INDOOR_ACTIVITY;
import static ca.mitenko.evn.CategoryConstants.OUTDOOR_ACTIVITY;
import static ca.mitenko.evn.CategoryConstants.SERVICE;
import static ca.mitenko.evn.CategoryConstants.SHOPPING;

@RuntimePermissions
public class HubActivity extends AppCompatActivity
        implements HubView, View.OnClickListener {
    /**
     * Hub Fragment container
     */
    @BindView(R.id.fragment_container)
    FrameLayout hubFragmentContainer;

    /**
     * The toolbar
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * The toolbar
     */
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    /**
     * The destinations page nav button
     */
    @BindView(R.id.explore_button)
    ImageView exploreButton;

    /**
     * The destinations page nav button
     */
    @BindView(R.id.event_button)
    ImageView eventButton;

    /**
     * The destinations page nav button
     */
    @BindView(R.id.filter_button)
    ImageView filterButton;

    /**
     * The done nav button
     */
    @BindView(R.id.done_button)
    ImageView doneButton;

    /**
     * The destinations page nav button
     */
    @BindView(R.id.category_picker)
    HorizontalScrollView categoryPicker;

    /**
     * The on the town filter button
     */
    @BindView(R.id.beverages)
    FloatingActionButton beveragesFilter;

    /**
     * The foodFilter filter button
     */
    @BindView(R.id.food)
    FloatingActionButton foodFilter;

    /**
     * The shopping filter button
     */
    @BindView(R.id.shopping)
    FloatingActionButton shoppingFilter;

    /**
     * The shopping filter button
     */
    @BindView(R.id.service)
    FloatingActionButton serviceFilter;

    /**
     * The shopping filter button
     */
    @BindView(R.id.outdoor_activity)
    FloatingActionButton outdoorActivityFilter;

    /**
     * The shopping filter button
     */
    @BindView(R.id.indoor_activity)
    FloatingActionButton indoorActivityFilter;

    /**
     * The shopping filter button
     */
    @BindView(R.id.accomodation)
    FloatingActionButton accomodationFilter;

    /**
     * Maps the categories to each button
     */
    private HashMap<String, FloatingActionButton> categoryButtonMap;

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
     * Fragment manager
     */
    private FragmentManager fragmentManager;

    /**
     * The Default / Destination Map fragment
     */
    private DestMapFragment destMapFragment;

    /**
     * The Destination List fragment
     */
    private DestListFragment destListFragment;

    /**
     * The Event List fragment
     */
    private EventListFragment eventListFragment;

    /**
     * The Event List fragment
     */
    private DestDetailFragment destDetailFragment = null;

    /**
     * The Event List fragment
     */
    private EventDetailFragment eventDetailFragment = null;

    /**
     * The Event List fragment
     */
    private FilterFragment filterFragment;

    /**
     * Hub presenter
     */
    private HubPresenter hubPresenter;

    /**
     * Flag indicating the toolbar is showing
     */
    boolean toolbarShowing = true;


    /**
     * Util for requesting the user location
     */
    private UserLocationUtil userLocationUtil = null;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ((EvNApplication) getApplication())
                .getApplicationComponent().inject(this);

        // Init Toolbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        // Init buttons
        exploreButton.setOnClickListener(this);
        eventButton.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        categoryButtonMap = new HashMap<>();
        categoryButtonMap.put(BEVERAGES, beveragesFilter);
        categoryButtonMap.put(FOOD, foodFilter);
        categoryButtonMap.put(SHOPPING, shoppingFilter);
        categoryButtonMap.put(SERVICE, serviceFilter);
        categoryButtonMap.put(OUTDOOR_ACTIVITY, outdoorActivityFilter);
        categoryButtonMap.put(INDOOR_ACTIVITY, indoorActivityFilter);
        categoryButtonMap.put(ACCOMMODATION, accomodationFilter);
        for (Map.Entry<String, FloatingActionButton> mapEntry : categoryButtonMap.entrySet()) {
            mapEntry.getValue().setOnClickListener(this);
        }

        // Get fragment manager
        fragmentManager = getSupportFragmentManager();

        // Init fragments
        destMapFragment = (DestMapFragment) fragmentManager.findFragmentByTag(DestMapFragment.TAG);
        if (destMapFragment == null) {
            destMapFragment = new DestMapFragment();
        }

        destListFragment = (DestListFragment) fragmentManager.findFragmentByTag(DestListFragment.TAG);
        if (destListFragment == null) {
            destListFragment = new DestListFragment();
        }

        eventListFragment = (EventListFragment) fragmentManager.findFragmentByTag(EventListFragment.TAG);
        if (eventListFragment == null) {
            eventListFragment = new EventListFragment();
        }

        filterFragment = (FilterFragment) fragmentManager.findFragmentByTag(FilterFragment.TAG);
        if (filterFragment == null) {
            filterFragment = new FilterFragment();
        }

        // Attempt to load state
        HubState hubState;
        if (savedInstanceState != null && savedInstanceState.containsKey(HubState.TAG)) {
            hubState = Parcels.unwrap(savedInstanceState.getParcelable(HubState.TAG));
        } else {
            hubState = ImmutableHubState.builder()
                    .currentFragment(HubState.FragmentType.DEST_MAP)
                    .build();
        }

        EventListInteractor eventInteractor =
                new EventListInteractor(retrofit, bus, evnService);
        CategoryInteractor categoryInteractor =
                new CategoryInteractor(retrofit, bus, evnService);

        // Init presenter
        hubPresenter = new HubPresenter(
                this, hubState, bus, eventInteractor, categoryInteractor);
    }

    /**
     * Override to use permissions request
     * {@inheritDoc}
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    @SuppressWarnings("all")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        HubActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * Button / Event Callbacks
     */
    public void onClick(View view) {
        if (view.equals(exploreButton)) {
            bus.post(new ViewMapEvent());
            return;
        }
        if (view.equals(eventButton)) {
            bus.post(new ViewEventEvent());
            return;
        }
        if (view.equals(filterButton)) {
            bus.post(new ViewFilterEvent());
            return;
        }
        if (view.equals(doneButton)) {
            onBackPressed();
            return;
        }

        for (Map.Entry<String, FloatingActionButton> mapEntry : categoryButtonMap.entrySet()) {
            if (mapEntry.getValue().equals(view)) {
                hubPresenter.onModifyFilterEvent(
                        new ModifyFilterEvent(ModifyFilterEvent.Type.CATEGORY, mapEntry.getKey(),
                                ModifyFilterEvent.Action.TOGGLE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        hubPresenter.onBackPressed();
    }

    /**
     * Shutdown
     */
    @Override
    public void shutdown() {
        userLocationUtil = null;
        this.finish();
    }

    /**
     * Presenter Callbacks
     */
    /**
     * Will fetch the user's location
     */
    public void getUserLocation() {
        userLocationUtil = new UserLocationUtil(this, bus);
        userLocationUtil.getLocation();
    }

    /**
     * Called once we've requests location permissions
     */
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void getLocationPermission() {
        // Post as runnable so hubpresenter can be assigned something
        new Handler().post(() -> hubPresenter.onLocationPermissionGranted(true));
        showDestMapAfterPermissions();
    }

    /**
     * Called if the permission request is denied
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void locationPermissionDenied() {
        new Handler().post(() -> hubPresenter.onLocationPermissionGranted(false));
        showDestMapAfterPermissions();
    }

    /**
     * {@inheritDoc}
     */
    /**
     * Don't show the dest map until we've requested location permission
     */
    @Override
    public void showDestMap() {
        HubActivityPermissionsDispatcher.getLocationPermissionWithCheck(this);
    }

    /**
     * Called after we've made a permission check
     */
    public void showDestMapAfterPermissions() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Fragment not added. Add fragment
        if (!destMapFragment.isAdded()) {
            transaction.add(R.id.fragment_container
                    , destMapFragment
                    , destMapFragment.TAG
            );
        }

        transaction.show(destMapFragment)
                .hide(destListFragment)
                .hide(eventListFragment)
                .hide(filterFragment);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        if (eventDetailFragment != null) {
            transaction.hide(eventDetailFragment);
        }

        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Shows the Destination List
     */
    @Override
    public void showDestList() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Fragment not added. Add fragment
        if (!destListFragment.isAdded()) {
            transaction.add(R.id.fragment_container
                    , destListFragment
                    , destListFragment.TAG
            );
        }

        transaction.show(destListFragment)
                .hide(destMapFragment)
                .hide(eventListFragment)
                .hide(filterFragment)
                .addToBackStack(null);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        if (eventDetailFragment != null) {
            transaction.hide(eventDetailFragment);
        }

        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Shows the Event List
     */
    @Override
    public void showEventList() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Fragment not added. Add fragment
        if (!eventListFragment.isAdded()) {
            transaction.add(R.id.fragment_container
                    , eventListFragment
                    , eventListFragment.TAG
            );
        }

        transaction.show(eventListFragment)
            .hide(destMapFragment)
            .hide(destListFragment)
            .hide(filterFragment)
            .addToBackStack(null);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        if (eventDetailFragment != null) {
            transaction.hide(eventDetailFragment);
        }

        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Shows the Destination Detail Fragment
     */
    @Override
    public void showDestDetail(Destination selectedDest) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        destDetailFragment = new DestDetailFragmentBuilder(selectedDest).build();

        transaction.add(R.id.fragment_container
                , destDetailFragment
                , destDetailFragment.TAG
        );

        transaction.show(destDetailFragment)
                .hide(eventListFragment)
                .hide(destMapFragment)
                .hide(destListFragment)
                .hide(filterFragment)
                .addToBackStack(null);

        if (eventDetailFragment != null) {
            transaction.hide(eventDetailFragment);
        }

        transaction.commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Shows the Destination Detail Fragment
     */
    @Override
    public void showEventDetail(Event selectedEvent) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        eventDetailFragment = new EventDetailFragmentBuilder(selectedEvent).build();

        transaction.add(R.id.fragment_container
                , eventDetailFragment
                , eventDetailFragment.TAG
        );

        transaction.show(eventDetailFragment)
                .hide(eventListFragment)
                .hide(destMapFragment)
                .hide(destListFragment)
                .hide(filterFragment)
                .addToBackStack(null);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        transaction.commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Shows the Destination Detail Fragment
     */
    public void showFilterView() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        filterFragment = new FilterFragmentBuilder().build();

        transaction.add(R.id.fragment_container
                , filterFragment
                , filterFragment.TAG
        );

        transaction.show(filterFragment)
                .hide(eventListFragment)
                .hide(destMapFragment)
                .hide(destListFragment)
                .addToBackStack(null);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        if (eventDetailFragment != null) {
            transaction.hide(eventDetailFragment);
        }

        transaction.commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Exposed methods for the fragments
     */
    /**
     * Returns the toolbar
     * @return
     */
    public Toolbar getToolbar() {
        return getToolbar();
    }

    /**
     * Sets the Toolbar text
     */
    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    /**
     * Sets the Toolbar text
     */
    public void showCategoryPicker(boolean show) {
        if (show) {
            if (toolbarShowing) {
                return;
            }
            toolbarShowing = true;
            categoryPicker.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + getResources().getDimensionPixelOffset(R.dimen.category_icon_size);
            toolbar.setLayoutParams(params);
        } else {
            if (!toolbarShowing) {
                return;
            }
            toolbarShowing = false;
            categoryPicker.setVisibility(View.GONE);
            // Calculate Default ActionBar height
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
                params.height = actionBarHeight;
                toolbar.setLayoutParams(params);
            }
        }
    }

    /**
     * Displays the 'Done' button
     */
    public void showDoneButton() {
        doneButton.setVisibility(View.VISIBLE);
        filterButton.setVisibility(View.GONE);
    }

    /**
     * Displays the 'Filter' button
     */
    public void showFilterButton() {
        doneButton.setVisibility(View.GONE);
        filterButton.setVisibility(View.VISIBLE);
    }

    /**
     * Applies the filter state to the filter buttons
     */
    public void applyFilterToView(Filter filter) {
        ColorStateList darkBackground = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.progress_bar_background));
        for (Map.Entry<String, FloatingActionButton> mapEntry : categoryButtonMap.entrySet()) {
            ColorStateList resetBackground = ColorStateList.valueOf(
                    ContextCompat.getColor(this, CategoryConstants.categoryColorMap.get(mapEntry.getKey())));
            if (filter.categories().isEmpty()) {
                mapEntry.getValue().setBackgroundTintList(resetBackground);
            } else {
                if (filter.categories().contains(mapEntry.getKey())) {
                    mapEntry.getValue().setBackgroundTintList(resetBackground);
                } else {
                    mapEntry.getValue().setBackgroundTintList(darkBackground);
                }
            }
        }
    }
}
