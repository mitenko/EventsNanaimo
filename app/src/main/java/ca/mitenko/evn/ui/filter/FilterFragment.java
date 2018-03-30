package ca.mitenko.evn.ui.filter;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.presenter.FilterFragPresenter;
import ca.mitenko.evn.state.FilterFragState;
import ca.mitenko.evn.state.ImmutableFilterFragState;
import ca.mitenko.evn.ui.common.RootFragment;

import static ca.mitenko.evn.CategoryConstants.ACCOMMODATION;
import static ca.mitenko.evn.CategoryConstants.ALL;
import static ca.mitenko.evn.CategoryConstants.BEVERAGES;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.INDOOR_ACTIVITY;
import static ca.mitenko.evn.CategoryConstants.OUTDOOR_ACTIVITY;
import static ca.mitenko.evn.CategoryConstants.SERVICE;
import static ca.mitenko.evn.CategoryConstants.SHOPPING;

/**
 * Created by mitenko on 2017-04-22.
 */

@FragmentWithArgs
public class FilterFragment extends RootFragment
    implements FilterView, View.OnClickListener {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.filter";

    /**
     * Event bus
     */
    @Inject
    EventBus bus;

    /**
     * The All Text button
     */
    @BindView(R.id.select_all)
    LinearLayout selectAllButton;

    /**
     * The on the town Text button
     */
    @BindView(R.id.beverages)
    LinearLayout beverages;

    /**
     * The foodText Text button
     */
    @BindView(R.id.food)
    LinearLayout foodButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.shopping)
    LinearLayout shoppingButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.service)
    LinearLayout serviceButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.outdoor_activity)
    LinearLayout outdoorActivityButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.indoor_activity)
    LinearLayout indoorActivityButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.accommodation)
    LinearLayout accomodationButton;

    /**
     * Maps the categories to each button
     */
    private HashMap<String, LinearLayout> categoryButtonMap;

    /**
     * The Actitivy Container
     */
    @BindView(R.id.activity_container)
    FlexboxLayout activityContainer;

    /**
     * Maps the Activities to the (dynamically generated) buttons
     */
    private HashMap<String, TextView> activityButtonMap;


    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EvNApplication) getActivity().getApplication())
                .getApplicationComponent().inject(this);

        categoryButtonMap = new HashMap<>();
        categoryButtonMap.put(ALL, selectAllButton);
        categoryButtonMap.put(BEVERAGES, beverages);
        categoryButtonMap.put(FOOD, foodButton);
        categoryButtonMap.put(SHOPPING, shoppingButton);
        categoryButtonMap.put(SERVICE, serviceButton);
        categoryButtonMap.put(OUTDOOR_ACTIVITY, outdoorActivityButton);
        categoryButtonMap.put(INDOOR_ACTIVITY, indoorActivityButton);
        categoryButtonMap.put(ACCOMMODATION, accomodationButton);
        for (Map.Entry<String, LinearLayout> mapEntry : categoryButtonMap.entrySet()) {
            // Keep the Modified Accomodation Value (Stay)
            if (mapEntry.getKey() != ACCOMMODATION) {
                TextView textView = (TextView) mapEntry.getValue().findViewById(R.id.text);
                textView.setText(mapEntry.getKey());
            }
            mapEntry.getValue().setOnClickListener(this);
        }

        activityButtonMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init presenter
        presenter = new FilterFragPresenter(
                this, (FilterFragState)state, bus);
        setToolbar();
        hub.showDoneButton();
    }

    /**
     * Returns the state key for storing and restoring the state
     * @return
     */
    public String getStateKey() {
        return FilterFragState.TAG;
    }

    /**
     * Returns the default state
     * @return
     */
    public FilterFragState getDefaultState() {
        return ImmutableFilterFragState.builder().build();
    }

    /**
     * {@inheritDoc}
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolbar();
            hub.showDoneButton();
        }
    }

    /**
     * Sets the tool bar to show the proper title
     */
    public void setToolbar() {
        hub.setToolbarTitle(getString(R.string.filter_toolbar_title));
        hub.showCategoryPicker(false);
    }

    /**
     * Button / Event Callbacks
     */
    public void onClick(View view) {
        /**
         * Check the Category Buttons
         */
        for (Map.Entry<String, LinearLayout> mapEntry : categoryButtonMap.entrySet()) {
            if (mapEntry.getValue().equals(view)) {
                if (view == selectAllButton) {
                    bus.post(new ModifyFilterEvent(ModifyFilterEvent.Type.CATEGORY, "",
                            ModifyFilterEvent.Action.CLEAR_ALL));
                } else {
                    bus.post(new ModifyFilterEvent(ModifyFilterEvent.Type.CATEGORY, mapEntry.getKey(),
                            ModifyFilterEvent.Action.TOGGLE));
                }
            }
        }

        /**
         * Check the Activity Buttons
         */
        for (Map.Entry<String, TextView> mapEntry : activityButtonMap.entrySet()) {
            if (mapEntry.getValue().equals(view)) {
                bus.post(new ModifyFilterEvent(ModifyFilterEvent.Type.ACTIVITY,
                        mapEntry.getKey(), ModifyFilterEvent.Action.TOGGLE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void renderCategories(HashMap<String, ArrayList<Activity>> categoryMap) {
        activityButtonMap.clear();
        activityContainer.removeAllViews();
        Integer selText = ContextCompat.getColor(getContext(), R.color.black);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ArrayList<String> sortedActivities = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Activity>> mapEntry : categoryMap.entrySet()) {
            for(Activity activity : mapEntry.getValue()) {
                if (!sortedActivities.contains(activity.getName())) {
                    sortedActivities.add(activity.getName());
                }
            }
        }
        Collections.sort(sortedActivities);

        for(String activityName : sortedActivities) {
            inflater.inflate(
                    R.layout.item_activity_button, activityContainer, true);
            TextView button = (TextView)
                    activityContainer.getChildAt(activityContainer.getChildCount()-1);
            button.setText(activityName);
            button.setOnClickListener(this);
            button.setSelected(true);
            button.setTextColor(selText);
            activityButtonMap.put(activityName, button);
        }
    }

    /**
     * {@inheritDoc}
     * @param filter
     */
    public void applyFilterToView(Filter filter) {
        /**
         * Category Filter Buttons
         */
        ColorStateList darkBackground = ColorStateList.valueOf(
                ContextCompat.getColor(getContext(), R.color.progress_bar_background));
        for (Map.Entry<String, LinearLayout> mapEntry : categoryButtonMap.entrySet()) {
            ColorStateList resetBackground = ColorStateList.valueOf(
                    ContextCompat.getColor(getContext(), CategoryConstants.categoryColorMap.get(mapEntry.getKey())));
            FloatingActionButton fab =
                    (FloatingActionButton) mapEntry.getValue().findViewById(R.id.fab);

            if (filter.getCategories().isEmpty()) {
                fab.setBackgroundTintList(resetBackground);
            } else {
                if (filter.getCategories().contains(mapEntry.getKey())) {
                    fab.setBackgroundTintList(resetBackground);
                } else {
                    fab.setBackgroundTintList(darkBackground);
                }
            }
        }

        /**
         * Activity Buttons
         */
        Integer normText = ContextCompat.getColor(getContext(), R.color.secondaryText);
        Integer selText = ContextCompat.getColor(getContext(), R.color.black);
        for (Map.Entry<String, TextView> mapEntry : activityButtonMap.entrySet()) {

            if (filter.getActivities().isEmpty()) {
                mapEntry.getValue().setSelected(true);
                mapEntry.getValue().setTextColor(normText);
            } else {
                if (filter.getActivities().contains(mapEntry.getKey())) {
                    mapEntry.getValue().setSelected(true);
                    mapEntry.getValue().setTextColor(selText);
                } else {
                    mapEntry.getValue().setSelected(false);
                    mapEntry.getValue().setTextColor(normText);
                }
            }
        }
    }
}
