package ca.mitenko.evn.presenter.common;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ca.mitenko.evn.event.DefaultEvent;
import ca.mitenko.evn.state.common.RootState;
import ca.mitenko.evn.ui.common.RootView;

/**
 * Created by mitenko on 2017-04-23.
 */

public abstract class RootPresenter<T extends RootView, S extends RootState> {
    /**
     * Presenter view
     */
    private T view;

    /**
     * Current state
     */
    protected S curState;

    /**
     * Event bus
     */
    protected EventBus bus;

    /**
     * Base class constructor
     * @param view
     * @param curState
     * @param bus
     */
    public RootPresenter(T view, S initState, S curState, EventBus bus) {
        this.view = view;
        this.curState = initState;
        this.bus = bus;
        render(curState);
        bus.register(this);
    }

    /**
     * Returns the presenter view
     * @return
     */
    public T getView() {
        return view;
    }

    /**
     * Returns the presenter current state
     * @return
     */
    public S getCurState() {
        return curState;
    }

    /**
     * Render view
     */
    public void render(S state) {
        renderState(view, state, curState);
        curState = state;
    }

    /**
     * Render view based on current state
     * @param view
     * @param curState
     * @param prevState
     */
    public abstract void renderState(T view, S curState, S prevState);

    /**
     * On Pause
     *
     * Override this method if it is required
     */
    public void onPause() {}

    /**
     * On presenter destroy
     */
    public void onDestroy() {
        view = null;
        if (bus.isRegistered(this)) {
            bus.unregister(this);
        }
    }

    /**
     * See BaseRegisterEvent
     * @param event
     */
    @Subscribe
    public void onDefaultEvent(DefaultEvent event) {}
}
