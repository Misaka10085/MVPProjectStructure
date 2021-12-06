package com.misakanetwork.lib_common.utils;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created By：Misaka10085
 * on：2021/7/7
 * package：com.misakanetwork.lib_common.utils
 * class name：AppBarStateChangeListener
 * desc：AppBarStateChangeListener
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
        onChanging(appBarLayout, mCurrentState, i);
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    public abstract void onChanging(AppBarLayout appBarLayout, State state, int i);
}
