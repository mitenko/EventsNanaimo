package ca.mitenko.evn.dagger;

import javax.inject.Singleton;

import ca.mitenko.evn.dagger.module.ApiModule;
import ca.mitenko.evn.dagger.module.ApplicationModule;
import ca.mitenko.evn.dagger.module.NetworkModule;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragment;
import ca.mitenko.evn.ui.dest_list.DestListFragment;
import ca.mitenko.evn.ui.dest_map.DestMapFragment;
import ca.mitenko.evn.ui.event_list.EventListFragment;
import ca.mitenko.evn.ui.hub.HubActivity;
import dagger.Component;

/**
 * Created by mitenko on 2017-04-22.
 */

@Component(modules = {ApplicationModule.class, NetworkModule.class, ApiModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(HubActivity activity);
    void inject(DestMapFragment fragment);
    void inject(DestListFragment fragment);
    void inject(EventListFragment fragment);
    void inject(DestDetailFragment fragment);
}
