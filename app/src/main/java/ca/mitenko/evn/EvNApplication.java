package ca.mitenko.evn;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

import ca.mitenko.evn.dagger.ApplicationComponent;
import ca.mitenko.evn.dagger.DaggerApplicationComponent;
import ca.mitenko.evn.dagger.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mitenko on 2017-04-22.
 */

public class EvNApplication extends Application {
    /**
     * Application component
     */
    protected ApplicationComponent applicationComponent;

    /**
     * {@inheritDoc}
     */
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        // Init Crashlytics
        Crashlytics crashlytics = new Crashlytics.Builder().build();
//        Fabric.with(this, crashlytics);

        Fresco.initialize(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    /**
     * Getters
     */
    /**
     * Returns the ApplicationComponent
     * @return
     */
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
