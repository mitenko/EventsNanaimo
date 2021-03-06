package ca.mitenko.evn.ui.event_detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.Manifest;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.presenter.EventDetailPresenter;
import ca.mitenko.evn.state.EventDetailState;
import ca.mitenko.evn.state.ImmutableEventDetailState;
import ca.mitenko.evn.ui.common.CategoryView;
import ca.mitenko.evn.ui.common.RootFragment;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by mitenko on 2017-04-22.
 */

@RuntimePermissions
@FragmentWithArgs
public class EventDetailFragment extends RootFragment
    implements EventDetailView {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.event_detail";

    /**
     * The Destination Image
     */
    @BindView(R.id.event_image)
    SimpleDraweeView eventImage;

    /**
     * The eventination Title
     */
    @BindView(R.id.event_title)
    TextView eventTitle;

    /**
     * The eventination Title
     */
    @BindView(R.id.event_desc)
    TextView eventDescription;

    /**
     * The phone Container
     */
    @BindView(R.id.phone_container)
    LinearLayout phoneContainer;

    /**
     * The phone / Address
     */
    @BindView(R.id.phone_icon)
    ImageView phoneIcon;

    /**
     * The phone / Address
     */
    @BindView(R.id.phone_desc)
    TextView phoneDescription;

    /**
     * The link Container
     */
    @BindView(R.id.link_container)
    LinearLayout linkContainer;

    /**
     * The link / Address
     */
    @BindView(R.id.link_icon)
    ImageView linkIcon;

    /**
     * The link / Address
     */
    @BindView(R.id.link_desc)
    TextView linkDescription;

    /**
     * The link Container
     */
    @BindView(R.id.email_container)
    LinearLayout emailContainer;

    /**
     * The link / Address
     */
    @BindView(R.id.email_icon)
    ImageView emailIcon;

    /**
     * The link / Address
     */
    @BindView(R.id.email_desc)
    TextView emailDescription;

    /**
     * Event bus
     */
    @Inject
    EventBus bus;

    /**
     * The Destination
     */
    @Arg(bundler = ParcelerArgsBundler.class)
    Event event;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EvNApplication) getActivity().getApplication())
                .getApplicationComponent().inject(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Init presenter
        presenter = new EventDetailPresenter(
                this, (EventDetailState)state, bus);

        linkContainer.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(event.getDetail().getWebsite()));
            startActivity(browserIntent);
        });

        phoneContainer.setOnClickListener(view -> {
            EventDetailFragmentPermissionsDispatcher.makePhoneCallWithCheck(this);
        });

        emailContainer.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + event.getDetail().getEmail()));
            startActivity(emailIntent);
        });

        setToolbar();
        hub.showDoneButton();
    }

    /**
     * Returns the state key for storing and restoring the state
     * @return
     */
    public String getStateKey() {
        return EventDetailState.TAG;
    }

    /**
     * Returns the default state
     * @return
     */
    public EventDetailState getDefaultState() {
        return ImmutableEventDetailState.builder().event(event).build();
    }

    /**
     * Makes the phone call once permissions have been granted
     */
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + event.getDetail().getPhone()));
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressWarnings("all")
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions,
                                            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EventDetailFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * {@inheritDoc}
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolbar();
            hub.showDoneButton();
        }
    }

    /**
     * Sets the tool bar to show the proper title
     */
    public void setToolbar() {
        if (event != null) {
            hub.setToolbarTitle(event.getDetail().getName());
        }
        hub.showCategoryPicker(false);
    }

    /**
     * {@inheritDoc}
     * @param event
     */
    public void displayEvent(Event event) {
        this.event = event;

        hub.setToolbarTitle(event.getDetail().getName());

        eventImage.setImageURI(event.getDetail().getImageURL());
        eventTitle.setText(event.getDetail().getName());
        eventDescription.setText(event.getDetail().getLongDesc());

        // phone number
        if (!event.getDetail().getPhone().isEmpty()) {
            phoneDescription.setText(event.getDetail().getPhone());
        } else {
            phoneContainer.setVisibility(View.GONE);
        }

        // website
        if (!event.getDetail().getWebsite().isEmpty()) {
            linkDescription.setText(event.getDetail().getWebsite());
        } else {
            linkContainer.setVisibility(View.GONE);
        }

        // email
        if (!event.getDetail().getEmail().isEmpty()) {
            emailDescription.setText(event.getDetail().getEmail());
        } else {
            emailContainer.setVisibility(View.GONE);
        }
    }
}
