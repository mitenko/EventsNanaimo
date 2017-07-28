package ca.mitenko.evn.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hannesdorfmann.fragmentargs.FragmentArgs;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.ui.hub.HubActivity;

/**
 * Created by mitenko on 2017-04-22.
 */

public abstract class RootFragment<T extends RootPresenter> extends Fragment {
    /**
     * Butterknife unbinders
     */
    protected Unbinder unbinder;

    /**
     * Fragment presenter
     */
    protected T presenter;

    /**
     * The hub activity
     */
    protected HubActivity hub;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hub = (HubActivity) getActivity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
