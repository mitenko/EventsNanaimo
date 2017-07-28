package ca.mitenko.evn.ui.dest_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import ca.mitenko.evn.EvNApplication;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.presenter.DestDetailPresenter;
import ca.mitenko.evn.state.ImmutableDestDetailState;
import ca.mitenko.evn.ui.common.RootFragment;

/**
 * Created by mitenko on 2017-04-22.
 */

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
                this, ImmutableDestDetailState.builder().destination(destination).build(), bus);
        setToolbar();
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
        }
    }

    /**
     * Sets the tool bar to show the proper title
     */
    public void setToolbar() {
        if (destination != null) {
            hub.setToolbarTitle(destination.detail().name());
        }
        hub.showCategoryPicker(false);
    }

    /**
     * {@inheritDoc}
     * @param dest
     */
    public void displayDest(Destination dest) {
        this.destination = dest;

        hub.setToolbarTitle(destination.detail().name());

        destImage.setImageURI(dest.detail().imageURL());
        destTitle.setText(dest.detail().name());
        destDescription.setText(dest.detail().longDesc());
    }
}
