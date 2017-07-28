package ca.mitenko.evn.interactor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ca.mitenko.evn.event.DestinationResultEvent;
import ca.mitenko.evn.model.ImmutableDestinationResult;
import ca.mitenko.evn.model.Search;
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
    public void getDestinations(Search search) {
        LatLng ne = search.getSearchBounds().northeast;
        LatLng sw = search.getSearchBounds().southwest;
        evnService.getDestinations(
                ne.latitude, ne.longitude, sw.latitude, sw.longitude)
                .map(destinations ->
                        new DestinationResultEvent(search, destinations.destinations()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDestSubscriber());
    }

    /**
     * Returns the Subscriber for the Destinations
     * @return
     */
    public Subscriber<DestinationResultEvent> getDestSubscriber() {
        return new Subscriber<DestinationResultEvent>() {
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
            public void onNext(DestinationResultEvent event) {
                bus.postSticky(event);
            }
        };

    }
}
