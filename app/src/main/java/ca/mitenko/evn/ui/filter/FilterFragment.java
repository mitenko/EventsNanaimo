package ca.mitenko.evn.ui.filter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Category;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.presenter.FilterFragPresenter;
import ca.mitenko.evn.state.EventListState;
import ca.mitenko.evn.state.FilterFragState;
import ca.mitenko.evn.state.ImmutableEventListState;
import ca.mitenko.evn.state.ImmutableFilterFragState;
import ca.mitenko.evn.ui.common.RootFragment;

import static ca.mitenko.evn.CategoryConstants.ACCOMMODATION;
import static ca.mitenko.evn.CategoryConstants.ADVENTURE;
import static ca.mitenko.evn.CategoryConstants.ALL;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.LIFESTYLE;
import static ca.mitenko.evn.CategoryConstants.ON_THE_TOWN;
import static ca.mitenko.evn.CategoryConstants.SERVICE;
import static ca.mitenko.evn.CategoryConstants.SHOPPING;
import static ca.mitenko.evn.CategoryConstants.SIGHT_SEEING;

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
    @BindView(R.id.on_the_town)
    LinearLayout onTheTownButton;

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
     * The sight seeing Text button
     */
    @BindView(R.id.sight_seeing)
    LinearLayout sightSeeingButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.service)
    LinearLayout serviceButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.adventure)
    LinearLayout adventureButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.accommodation)
    LinearLayout accomodationButton;

    /**
     * The shopping Text button
     */
    @BindView(R.id.lifestyle)
    LinearLayout lifestyleButton;

    /**
     * Maps the categories to each button
     */
    private HashMap<String, LinearLayout> categoryButtonMap;

    /**
     * The Free Button
     */
    @BindView(R.id.btn_free_cost)
    TextView freeCostButton;

    /**
     * The $ Button
     */
    @BindView(R.id.btn_one_cost)
    TextView oneCostButton;

    /**
     * The $$ Button
     */
    @BindView(R.id.btn_two_cost)
    TextView twoCostButton;

    /**
     * The $$$ Button
     */
    @BindView(R.id.btn_three_cost)
    TextView threeCostButton;

    /**
     * Maps the categories to each button
     */
    private HashMap<Integer, TextView> costButtonMap;

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
        categoryButtonMap.put(ON_THE_TOWN, onTheTownButton);
        categoryButtonMap.put(FOOD, foodButton);
        categoryButtonMap.put(SHOPPING, shoppingButton);
        categoryButtonMap.put(SIGHT_SEEING, sightSeeingButton);
        categoryButtonMap.put(SERVICE, serviceButton);
        categoryButtonMap.put(ADVENTURE, adventureButton);
        categoryButtonMap.put(ACCOMMODATION, accomodationButton);
        categoryButtonMap.put(LIFESTYLE, lifestyleButton);
        for (Map.Entry<String, LinearLayout> mapEntry : categoryButtonMap.entrySet()) {
            TextView textView = (TextView) mapEntry.getValue().findViewById(R.id.text);
            textView.setText(mapEntry.getKey());
            mapEntry.getValue().setOnClickListener(this);
        }

        costButtonMap = new HashMap<>();
        costButtonMap.put(getContext().getResources().getInteger(R.integer.cost_free), freeCostButton);
        costButtonMap.put(getContext().getResources().getInteger(R.integer.cost_one), oneCostButton);
        costButtonMap.put(getContext().getResources().getInteger(R.integer.cost_two), twoCostButton);
        costButtonMap.put(getContext().getResources().getInteger(R.integer.cost_three), threeCostButton);
        for (Map.Entry<Integer, TextView> mapEntry : costButtonMap.entrySet()) {
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
         * Check the Cost Buttons
         */
        for (Map.Entry<Integer, TextView> mapEntry : costButtonMap.entrySet()) {
            if (mapEntry.getValue().equals(view)) {
                bus.post(new ModifyFilterEvent(ModifyFilterEvent.Type.COST, mapEntry.getKey().toString(),
                        ModifyFilterEvent.Action.TOGGLE));
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
        for (Map.Entry<String, ArrayList<Activity>> mapEntry : categoryMap.entrySet()) {
            for(Activity activity : mapEntry.getValue()) {
                if (!activityButtonMap.containsKey(activity.name())) {
                    inflater.inflate(
                            R.layout.item_activity_button, activityContainer, true);
                    TextView button = (TextView)
                            activityContainer.getChildAt(activityContainer.getChildCount()-1);
                    button.setText(activity.name());
                    button.setOnClickListener(this);
                    button.setSelected(true);
                    button.setTextColor(selText);
                    activityButtonMap.put(activity.name(), button);
                }
            }
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

            if (filter.categories().isEmpty()) {
                fab.setBackgroundTintList(resetBackground);
            } else {
                if (filter.categories().contains(mapEntry.getKey())) {
                    fab.setBackgroundTintList(resetBackground);
                } else {
                    fab.setBackgroundTintList(darkBackground);
                }
            }
        }

        /**
         * Cost Buttons
         */
        Drawable normBg = ContextCompat.getDrawable(getContext(), R.drawable.cost_button);
        Drawable selBg = ContextCompat.getDrawable(getContext(), R.drawable.cost_button_selected);
        Integer selText = ContextCompat.getColor(getContext(), R.color.white);
        Integer normText = ContextCompat.getColor(getContext(), R.color.appGreen);
        for (Map.Entry<Integer, TextView> mapEntry : costButtonMap.entrySet()) {
            if (filter.cost().isEmpty()) {
                mapEntry.getValue().setBackground(normBg);
                mapEntry.getValue().setTextColor(normText);
            } else {
                if (filter.cost().contains(mapEntry.getKey())) {
                    mapEntry.getValue().setBackground(selBg);
                    mapEntry.getValue().setTextColor(selText);
                } else {
                    mapEntry.getValue().setBackground(normBg);
                    mapEntry.getValue().setTextColor(normText);
                }
            }
        }

        /**
         * Activity Buttons
         */
        normText = ContextCompat.getColor(getContext(), R.color.secondaryText);
        selText = ContextCompat.getColor(getContext(), R.color.black);
        for (Map.Entry<String, TextView> mapEntry : activityButtonMap.entrySet()) {

            if (filter.activities().isEmpty()) {
                mapEntry.getValue().setSelected(true);
                mapEntry.getValue().setTextColor(normText);
            } else {
                if (filter.activities().contains(mapEntry.getKey())) {
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
