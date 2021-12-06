package com.misakanetwork.lib_common.utils;

/**
 * Created By：Misaka10085
 * on：2021/6/18
 * package：com.misakanetwork.lib_common.utils
 * class name：PausedAction
 * desc：Dialog在app处于后台时的关闭控制，防止后台dismiss弹窗闪退
 *
 * public void onResume() {
 * super.onResume();
 * if (mPausedAction != null)
 * mPausedAction.resume();
 * }
 * mPausedAction = new PausedAction();
 * mPausedAction.pause(getSupportFragmentManager().isStateSaved(),
 * () -> aniTuDialog.dismiss());
 * aniTuDialog.dismiss();
 */
public class PausedAction {

    private ActionCallback mCallback;

    public void pause(boolean shouldPause, ActionCallback callback) {
        if (shouldPause) {
            this.mCallback = callback;
        } else {
            callback.call();
        }
    }

    public void resume() {
        if (mCallback != null) {
            mCallback.call();
            mCallback = null;
        }
    }

    public interface ActionCallback {
        void call();
    }
}
