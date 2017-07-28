package ca.mitenko.evn.dagger.module;

import javax.inject.Singleton;

import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.network.interceptor.AuthInterceptor;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by mitenko on 2017-04-22.
 */

@Module
public class ApiModule {
    /**
     * Provides API auth interceptor
     * @return
     */
    @Singleton
    @Provides
    AuthInterceptor providesAuthInterceptor() {
        return new AuthInterceptor();
    }

    /**
     * Provides the EvN service
     * @param retrofit
     * @return
     */
    @Provides
    @Singleton
    EventsNanaimoService providesEventsNanaimoService(Retrofit retrofit) {
        return retrofit.create(EventsNanaimoService.class);
    }
}
