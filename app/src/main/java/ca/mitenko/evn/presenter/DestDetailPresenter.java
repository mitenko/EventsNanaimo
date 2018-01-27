package ca.mitenko.evn.presenter;

import org.greenrobot.eventbus.EventBus;

import ca.mitenko.evn.presenter.common.RootPresenter;
import ca.mitenko.evn.state.DestDetailState;
import ca.mitenko.evn.state.ImmutableDestDetailState;
import ca.mitenko.evn.ui.dest_detail.DestDetailView;

/**
 * Created by mitenko on 2017-04-23.
 */

public class DestDetailPresenter extends RootPresenter<DestDetailView, DestDetailState> {
    /**
     * Constructor
     * @param view
     * @param curState
     */
    public DestDetailPresenter(DestDetailView view, DestDetailState curState, EventBus bus) {
        super(view, ImmutableDestDetailState.builder().build(), curState, bus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderState(DestDetailView view, DestDetailState curState, DestDetailState prevState) {
        if (view != null) {
            if (!curState.destination().equals(prevState.destination())) {
                view.displayDest(curState.destination());
            }
        }
    }
}
