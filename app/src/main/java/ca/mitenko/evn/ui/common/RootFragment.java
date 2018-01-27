package ca.mitenko.evn.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hannesdorfmann.fragmentargs.FragmentArgs;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.common.RootState;
import ca.mitenko.evn.ui.hub.HubActivity;

/**
 * Created by mitenko on 2017-04-22.
 */

public abstract class RootFragment<T extends RootPresenter, S extends RootState> extends Fragment {
    /**
     * Butterknife unbinders
     */
    protected Unbinder unbinder;

    /**
     * Fragment presenter
     */
    protected T presenter;

    /**
     * Fragment state
     */
    protected S state;

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
        if (savedInstanceState != null && savedInstanceState.containsKey(getStateKey())) {
            state = Parcels.unwrap(savedInstanceState.getParcelable(getStateKey()));
        } else {
            state = getDefaultState();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            RootState curState = presenter.getCurState();
            outState.putParcelable(getStateKey(), Parcels.wrap(curState));
        }
    }

    /**
     * Returns the state key for storing and restoring the state
     * @return
     */
    public abstract String getStateKey();

    /**
     * Returns the default state
     * @return
     */
    public abstract S getDefaultState();

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
