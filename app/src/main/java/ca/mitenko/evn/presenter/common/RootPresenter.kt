package ca.mitenko.evn.presenter.common

import ca.mitenko.evn.event.DefaultEvent
import ca.mitenko.evn.state.common.RootState
import ca.mitenko.evn.ui.common.RootView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by mitenko on 2017-04-23.
 */

abstract class RootPresenter<T : RootView, S : RootState>
/**
 * Base class constructor
 * @param view
 * @param curState
 * @param bus
 */
(view: T, initState: S, curState: S,
 /**
  * Event bus
  */
 protected var bus: EventBus) {
    /**
     * Presenter view
     */
    /**
     * Returns the presenter view
     * @return
     */
    var view: T? = null
        private set

    /**
     * Current state
     */
    /**
     * Returns the presenter current state
     * @return
     */
    var curState: S
        protected set

    init {
        this.view = view
        this.curState = initState
        render(curState)
        bus.register(this)
    }

    /**
     * Render view
     */
    fun render(state: S) {
        renderState(view, state, curState)
        curState = state
    }

    /**
     * Render view based on current state
     * @param view
     * @param curState
     * @param prevState
     */
    abstract fun renderState(view: T?, curState: S, prevState: S)

    /**
     * On Pause
     *
     * Override this method if it is required
     */
    fun onPause() {}

    /**
     * On presenter destroy
     */
    fun onDestroy() {
        view = null
        if (bus.isRegistered(this)) {
            bus.unregister(this)
        }
    }

    /**
     * See BaseRegisterEvent
     * @param event
     */
    @Subscribe
    fun onDefaultEvent(event: DefaultEvent) {
    }
}
