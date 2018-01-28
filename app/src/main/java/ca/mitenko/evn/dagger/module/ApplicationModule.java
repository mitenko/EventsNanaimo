package ca.mitenko.evn.dagger.module;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

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
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }
}
