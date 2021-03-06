package ca.mitenko.evn.interactor

import ca.mitenko.evn.event.ErrorEvent
import ca.mitenko.evn.event.SearchEvent
import ca.mitenko.evn.model.search.DestSearch
import ca.mitenko.evn.network.EventsNanaimoService
import ca.mitenko.evn.util.MapUtil
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by mitenko on 2017-04-23.
 */

class DestMapInteractor
/**
 * Constructor
 * @param retrofit
 * @param bus
 * @param evnService
 */
(
        /**
         * Retrofit
         *
         */
        protected var retrofit: Retrofit,
        /**
         * Event bus
         */
        protected var bus: EventBus,
        /**
         * The Events Nanaimo Service
         */
        protected var evnService: EventsNanaimoService) {

    /**
     * Returns the Subscriber for the Destinations
     * @return
     */
    //@TODO Unexpected exception
    val destSubscriber: Subscriber<SearchEvent>
        get() = object : Subscriber<SearchEvent>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                bus.post(ErrorEvent(e))
            }

            override fun onNext(event: SearchEvent) {
                bus.postSticky(event)
            }
        }

    /**
     * Fetch the markers for
     */
    fun getDestinations(search: DestSearch) {
        val extendedBounds = MapUtil.extendBounds(search.searchBounds)
        val extendedSearch = search.copy(searchBounds = extendedBounds)
        val ne = extendedBounds.northeast
        val sw = extendedBounds.southwest
        evnService.getDestinations(
                ne.latitude, ne.longitude, sw.latitude, sw.longitude)
                .map { destinations ->
                    val searchWithResults = extendedSearch.copy(results = destinations.destinations)
                    SearchEvent(searchWithResults)
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(destSubscriber)
    }
}
