package ca.mitenko.evn.interactor;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ca.mitenko.evn.event.CategoryResultEvent;
import ca.mitenko.evn.model.CategoryResult;
import ca.mitenko.evn.network.EventsNanaimoService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mitenko on 2017-04-23.
 */

public class CategoryInteractor {
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
    public CategoryInteractor(Retrofit retrofit, EventBus bus, EventsNanaimoService evnService) {
        this.retrofit = retrofit;
        this.bus = bus;
        this.evnService = evnService;
    }

    /**
     * Fetch the markers for
     */
    public void getCategories() {
        evnService.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCategorySubscriber());
    }

    /**
     * Returns the Subscriber for the Destinations
     * @return
     */
    public Subscriber<CategoryResult> getCategorySubscriber() {
        return new Subscriber<CategoryResult>() {
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
            public void onNext(CategoryResult categoryResult) {
                bus.postSticky(new CategoryResultEvent(categoryResult.categoryMap()));
            }
        };

    }
}
