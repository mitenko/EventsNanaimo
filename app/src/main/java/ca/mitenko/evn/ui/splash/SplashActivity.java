package ca.mitenko.evn.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.ModifyFilterEvent;
import ca.mitenko.evn.event.ViewEventEvent;
import ca.mitenko.evn.event.ViewFilterEvent;
import ca.mitenko.evn.event.ViewMapEvent;
import ca.mitenko.evn.interactor.CategoryInteractor;
import ca.mitenko.evn.interactor.EventListInteractor;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.model.search.Filter;
import ca.mitenko.evn.network.EventsNanaimoService;
import ca.mitenko.evn.presenter.HubPresenter;
import ca.mitenko.evn.state.HubState;
import ca.mitenko.evn.state.ImmutableHubState;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragment;
import ca.mitenko.evn.ui.dest_detail.DestDetailFragmentBuilder;
import ca.mitenko.evn.ui.dest_list.DestListFragment;
import ca.mitenko.evn.ui.dest_map.DestMapFragment;
import ca.mitenko.evn.ui.event_list.EventListFragment;
import ca.mitenko.evn.ui.filter.FilterFragment;
import ca.mitenko.evn.ui.filter.FilterFragmentBuilder;
import ca.mitenko.evn.ui.hub.HubActivity;
import ca.mitenko.evn.ui.hub.HubView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;

import static ca.mitenko.evn.CategoryConstants.ACCOMMODATION;
import static ca.mitenko.evn.CategoryConstants.ADVENTURE;
import static ca.mitenko.evn.CategoryConstants.FOOD;
import static ca.mitenko.evn.CategoryConstants.LIFESTYLE;
import static ca.mitenko.evn.CategoryConstants.ON_THE_TOWN;
import static ca.mitenko.evn.CategoryConstants.SERVICE;
import static ca.mitenko.evn.CategoryConstants.SHOPPING;
import static ca.mitenko.evn.CategoryConstants.SIGHT_SEEING;

public class SplashActivity extends AppCompatActivity
    implements View.OnClickListener {
    /**
     * Hub Fragment container
     */
    @BindView(R.id.splash_btn)
    TextView enterButton;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        enterButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     * @param view
     */
    @Override
    public void onClick(View view) {
        enterButton.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }
}
