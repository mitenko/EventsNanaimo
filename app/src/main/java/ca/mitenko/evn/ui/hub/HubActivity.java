package ca.mitenko.evn.ui.hub;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.FilterSearchEvent;
import ca.mitenko.evn.event.ViewEventEvent;
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.interactor.CategoryInteractor;
import ca.mitenko.evn.interactor.EventListInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.HubPresenter;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragment;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragmentBuilder;
import ca.mitenko.evn.ui.dest_list.DestListFragment;
import ca.mitenko.evn.ui.dest_map.DestMapFragment;
import ca.mitenko.evn.ui.event_list.EventListFragment;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;

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
    @BindView(R.id.category_picker)
    HorizontalScrollView categoryPicker;

    /**
     * The destinations page nav button
     */
    @BindView(R.id.on_the_town)
    ImageView onTheTown;

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
     * Hub presenter
     */
    private HubPresenter hubPresenter;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        onTheTown.setOnClickListener(this);

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

        // Attempt to load state
        HubState hubState;
        if (savedInstanceState != null && savedInstanceState.containsKey(HubState.TAG)) {
            hubState = Parcels.unwrap(savedInstanceState.getParcelable(HubState.TAG));
        } else {
            hubState = ImmutableHubState.builder().showDestMap(true).build();
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
        if (view.equals(onTheTown)) {
            bus.post(new FilterSearchEvent(FilterSearchEvent.SearchType.NIGHTLIFE));
            return;
        }
    }

    /**
     * Presenter Callbacks
     */
    /**
     * {@inheritDoc}
     */
    @Override
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void showDestMap() {
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
                .hide(eventListFragment);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
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
                .hide(eventListFragment);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
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
                .hide(destListFragment);

        if (destDetailFragment != null) {
            transaction.hide(destDetailFragment);
        }

        transaction.commitAllowingStateLoss();
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
                .commitAllowingStateLoss();
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
            categoryPicker.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + getResources().getDimensionPixelOffset(R.dimen.category_icon_size);
            toolbar.setLayoutParams(params);
        } else {
            categoryPicker.setVisibility(View.INVISIBLE);
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
     * Permissions Callbacks
     */

    /**
     * Displays the rationale for requesting current location
     * @param request
     */
    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(final PermissionRequest request) {
        AlertDialog show = new AlertDialog.Builder(this)
                .setMessage(R.string.location_rationale)
                .setPositiveButton(R.string.btn_okay, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.btn_deny, (dialog, button) -> request.cancel())
                .show();
    }

    /**
     * Called when the permissions are denied
     */
    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForLocation() {
        finish();
    }

    /**
     * CAlled when the permissions are permanently denied
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void showNeverAskForLocation() {
        finish();
    }
}
