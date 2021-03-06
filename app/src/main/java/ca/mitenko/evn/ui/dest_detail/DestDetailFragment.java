package ca.mitenko.evn.ui.dest_detail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.Manifest;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.presenter.DestDetailPresenter;
import ca.mitenko.evn.state.DestDetailState;
import ca.mitenko.evn.state.ImmutableDestDetailState;
import ca.mitenko.evn.ui.common.CategoryView;
import ca.mitenko.evn.ui.common.RootFragment;
import ca.mitenko.evn.util.MapUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by mitenko on 2017-04-22.
 */

@RuntimePermissions
@FragmentWithArgs
public class DestDetailFragment extends RootFragment
    implements DestDetailView {
    /**
     * Fragment Tag
     */
    public final static String TAG = "fragment.dest_detail";

    /**
     * The Destination Image
     */
    @BindView(R.id.dest_image)
    SimpleDraweeView destImage;

    /**
     * The Destination Title
     */
    @BindView(R.id.dest_title)
    TextView destTitle;

    /**
     * The Destination Title
     */
    @BindView(R.id.dest_desc)
    TextView destDescription;

    /**
     * the category
     */
    @BindView(R.id.dest_category_view)
    CategoryView categoryView;

    /**
     * The Location / Address
     */
    @BindView(R.id.location_icon)
    ImageView locationIcon;

    /**
     * The Location / Address
     */
    @BindView(R.id.location_desc)
    TextView locationDescription;

    /**
     * The Destination Static Map
     */
    @BindView(R.id.map_image)
    SimpleDraweeView mapImage;

    /**
     * The Static Map Icon
     */
    @BindView(R.id.category_icon)
    ImageView categoryIcon;

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
    @BindView(R.id.link_container)
    LinearLayout linkContainer;

    /**
     * The phone Container
     */
    @BindView(R.id.phone_container)
    LinearLayout phoneContainer;

    /**
     * The location Container
     */
    @BindView(R.id.location_container)
    LinearLayout locationContainer;

    /**
     * Event bus
     */
    @Inject
    EventBus bus;

    /**
     * The Destination
     */
    @Arg(bundler = ParcelerArgsBundler.class)
    Destination destination;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_dest_detail, container, false);
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
        presenter = new DestDetailPresenter(
                this, (DestDetailState) state, bus);

        linkContainer.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(destination.getDetail().getWebsite()));
            startActivity(browserIntent);
        });
        phoneContainer.setOnClickListener(view -> {
            DestDetailFragmentPermissionsDispatcher.makePhoneCallWithCheck(this);
        });
        locationContainer.setOnClickListener(view -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=" + destination.getAddress().toString()));
            startActivity(intent);
        });

        setToolbar();
        hub.showDoneButton();
    }

    /**
     * Returns the state key for storing and restoring the state
     * @return
     */
    public String getStateKey() {
        return DestDetailState.TAG;
    }

    /**
     * Returns the default state
     * @return
     */
    public DestDetailState getDefaultState() {
        return ImmutableDestDetailState.builder().destination(destination).build();
    }

    /**
     * Makes the phone call once permissions have been granted
     */
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + destination.getDetail().getPhone()));
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
        DestDetailFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        if (destination != null) {
            hub.setToolbarTitle(destination.getDetail().getName());
        }
        hub.showCategoryPicker(false);
    }

    /**
     * {@inheritDoc}
     * @param dest
     */
    public void displayDest(Destination dest) {
        this.destination = dest;

        hub.setToolbarTitle(destination.getDetail().getName());

        destImage.setImageURI(dest.getDetail().getImageURL());
        destTitle.setText(dest.getDetail().getName());
        destDescription.setText(dest.getDetail().getLongDesc());
        categoryView.setCategories(destination.getDetail().getActivities(), true);
        locationDescription.setText(destination.getAddress().toString());

        // the map
        mapImage.setImageURI(MapUtil.toStaticMapURL(dest));
        Drawable icon = ContextCompat.getDrawable(getContext(),
                CategoryConstants.categoryIconMap.get(dest.getDisplayIcon()));
        categoryIcon.setImageDrawable(icon);

        // phone number
        if (!dest.getDetail().getPhone().isEmpty()) {
            phoneDescription.setText(dest.getDetail().getPhone());
        } else {
            phoneIcon.setVisibility(View.INVISIBLE);
            phoneDescription.setVisibility(View.INVISIBLE);
        }

        // website
        if (!dest.getDetail().getWebsite().isEmpty()) {
            linkDescription.setText(dest.getDetail().getWebsite());
        } else {
            linkIcon.setVisibility(View.INVISIBLE);
            linkDescription.setVisibility(View.INVISIBLE);
        }
    }
}
