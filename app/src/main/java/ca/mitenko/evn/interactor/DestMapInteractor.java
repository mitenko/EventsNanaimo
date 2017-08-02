package ca.mitenko.evn.interactor;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ca.mitenko.evn.event.SearchEvent;
import ca.mitenko.evn.model.search.DestSearch;
import ca.mitenko.evn.model.search.ImmutableDestSearch;
import ca.mitenko.evn.network.EventsNanaimoService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mitenko on 2017-04-23.
 */

public class DestMapInteractor {
    /**
     * Retrofit

     */
    protected Retrofit retrofit;

    /**
     * Event bus
     */
    protected EventBus bus;

    /**
     * The Events Nanaimo Service
     */
    protected EventsNanaimoService evnService;

    /**
     * Constructor
     * @param retrofit
     * @param bus
     * @param evnService
     */
    public DestMapInteractor(Retrofit retrofit, EventBus bus, EventsNanaimoService evnService) {
        this.retrofit = retrofit;
        this.bus = bus;
        this.evnService = evnService;
    }

    /**
     * Fetch the markers for
     */
    public void getDestinations(DestSearch search) {
        LatLng ne = search.searchBounds().northeast;
        LatLng sw = search.searchBounds().southwest;
        evnService.getDestinations(
                ne.latitude, ne.longitude, sw.latitude, sw.longitude)
                .map(destinations -> {
                    DestSearch searchWithResults = ImmutableDestSearch.builder()
                            .from(search)
                            .results(destinations.destinations())
                            .build();
                    return new SearchEvent(searchWithResults);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDestSubscriber());
    }

    /**
     * Returns the Subscriber for the Destinations
     * @return
     */
    public Subscriber<SearchEvent> getDestSubscriber() {
        return new Subscriber<SearchEvent>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                } else if (e instanceof IOException) {
                } else {
                    //@TODO Unexpected exception
                }
            }

            @Override
            public void onNext(SearchEvent event) {
                bus.postSticky(event);
            }
        };

    }
}
