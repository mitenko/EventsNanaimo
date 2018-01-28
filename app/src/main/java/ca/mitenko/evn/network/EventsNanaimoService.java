package ca.mitenko.evn.network;

import ca.mitenko.evn.model.CategoryResult;
import ca.mitenko.evn.model.DestinationResult;
import ca.mitenko.evn.model.EventResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mitenko on 2017-04-20.
 */

public interface EventsNanaimoService {
    /**
     * Returns the desinations
     * @param ne_lat
     * @param ne_lng
     * @param se_lat
     * @param se_lng
     * @return
     */
    @GET("getDestinations")
    Observable<DestinationResult> getDestinations(
            @Query("northeast_latitude") double ne_lat
            , @Query("northeast_longitude") double ne_lng
            , @Query("southwest_latitude") double se_lat
            , @Query("southwest_longitude") double se_lng
    );

    /**
     * Returns the events
     * @return
     */
    @GET("getEvents")
    Observable<EventResult> getEvents();

    /**
     * Returns the categories
     * @return
     */
    @GET("getCategoryData")
    Observable<CategoryResult> getCategories();
}
