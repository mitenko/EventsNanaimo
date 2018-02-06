package ca.mitenko.evn;

import java.util.HashMap;

/**
 * Created by mitenko on 2017-05-13.
 */

public class CategoryConstants {
    public static final String ALL = "All";
    public static final String BEVERAGES = "Beverages";
    public static final String FOOD = "Food";
    public static final String SHOPPING = "Shopping";
    public static final String SERVICE = "Service";
    public static final String OUTDOOR_ACTIVITY = "Outdoor Activities";
    public static final String INDOOR_ACTIVITY = "Indoor Activities";
    public static final String ACCOMMODATION = "Accommodation";
    public static final String UNKNOWN = "unknown";

    public static HashMap<String,Integer> categoryColorMap;
    static {
        categoryColorMap = new HashMap<>();
        categoryColorMap.put(ALL, R.color.colorAccent);
        categoryColorMap.put(BEVERAGES, R.color.beverages);
        categoryColorMap.put(FOOD, R.color.food);
        categoryColorMap.put(SHOPPING, R.color.shopping);
        categoryColorMap.put(SERVICE, R.color.service);
        categoryColorMap.put(OUTDOOR_ACTIVITY, R.color.outdoor_activity);
        categoryColorMap.put(INDOOR_ACTIVITY, R.color.indoor_activity);
        categoryColorMap.put(ACCOMMODATION, R.color.accommodation);
        categoryColorMap.put(UNKNOWN, R.color.unknown);
    }

    public static HashMap<String,Integer> categoryIconMap;
    static {
        categoryIconMap = new HashMap<>();
        categoryIconMap.put(BEVERAGES, R.drawable.ic_beverages);
        categoryIconMap.put(FOOD, R.drawable.ic_food);
        categoryIconMap.put(SHOPPING, R.drawable.ic_shopping);
        categoryIconMap.put(SERVICE, R.drawable.ic_service);
        categoryIconMap.put(OUTDOOR_ACTIVITY, R.drawable.ic_outdoor_activity);
        categoryIconMap.put(INDOOR_ACTIVITY, R.drawable.ic_indoor_activity);
        categoryIconMap.put(ACCOMMODATION, R.drawable.ic_accommodation);
        categoryIconMap.put(UNKNOWN, R.drawable.ic_lifestyle);
    }
}
