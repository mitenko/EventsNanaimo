package ca.mitenko.evn.interactor;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ca.mitenko.evn.event.EventResultEvent;
import ca.mitenko.evn.model.EventResult;
import ca.mitenko.evn.network.EventsNanaimoService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mitenko on 2017-04-23.
 */

public class EventListInteractor {
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
    public EventListInteractor(Retrofit retrofit, EventBus bus, EventsNanaimoService evnService) {
        this.retrofit = retrofit;
        this.bus = bus;
        this.evnService = evnService;
    }

    /**
     * Fetch the markers for
     */
    public void getEvents() {
        evnService.getEvents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getEventSubscriber());
    }

    /**
     * Returns the Subscriber for the Destinations
     * @return
     */
    public Subscriber<EventResult> getEventSubscriber() {
        return new Subscriber<EventResult>() {
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
            public void onNext(EventResult events) {
                bus.postSticky(new EventResultEvent(events.getEvents()));
            }
        };

    }
}
