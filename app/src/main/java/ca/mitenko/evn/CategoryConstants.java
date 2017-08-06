package ca.mitenko.evn;

import java.util.HashMap;

/**
 * Created by mitenko on 2017-05-13.
 */

public class CategoryConstants {
    public static final String ON_THE_TOWN = "On The Town";
    public static final String FOOD = "Food";
    public static final String SHOPPING = "Shopping";
    public static final String SIGHT_SEEING = "Sight Seeing";
    public static final String SERVICE = "Service";
    public static final String ADVENTURE = "Activity / Adventure";
    public static final String ACCOMMODATION = "Accommodation";
    public static final String LIFESTYLE = "Lifestyle";

    public static HashMap<String,Integer> categoryColorMap;
    static {
        categoryColorMap = new HashMap<>();
        categoryColorMap.put(ON_THE_TOWN, R.color.onTheTown);
        categoryColorMap.put(FOOD, R.color.food);
        categoryColorMap.put(SHOPPING, R.color.shopping);
        categoryColorMap.put(SIGHT_SEEING, R.color.sightSeeing);
        categoryColorMap.put(SERVICE, R.color.service);
        categoryColorMap.put(ADVENTURE, R.color.adventure);
        categoryColorMap.put(ACCOMMODATION, R.color.accommodation);
        categoryColorMap.put(LIFESTYLE, R.color.lifestyle);
    }
}
