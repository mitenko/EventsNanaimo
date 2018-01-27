package ca.mitenko.evn.dagger.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import ca.mitenko.evn.model.GsonAdaptersModel;
import dagger.Module;
import dagger.Provides;

/**
 * Created by mitenko on 2017-04-22.
 */

@Module
public class ApplicationModule {
    /**
     * Application instance
     */
    private Application application;

    /**
     * Creates an application module instance
     * @param application
     */
    public ApplicationModule(Application application) {
        this.application = application;
    }

    /**
     * Provides application instance
     * @return
     */
    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    /**
     * Provide EventBus
     * @return
     */
    @Provides
    @Singleton
    EventBus providesEventBus() {
        return new EventBus();
    }

    /**
     * Provides Gson
     * @return
     */
    @Provides
    @Singleton
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(new GsonAdaptersModel());
        return gsonBuilder.create();
    }
}
