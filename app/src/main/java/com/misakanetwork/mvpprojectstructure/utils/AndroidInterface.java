package com.misakanetwork.mvpprojectstructure.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.just.agentweb.AgentWeb;
import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.rx.RxBus;

/**
 * Created By：Misaka10085
 * on：2021/8/12
 * package：com.misakanetwork.mvpprojectstructure.utils
 * class name：AndroidInterface
 * desc：AgentWeb interface js调用android方法
 * 不可混淆
 */
public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }

    @JavascriptInterface
    public void callAndroid(final String msg) {

        deliver.post(new Runnable() {
            @Override
            public void run() {
                L.e("Info", "main Thread:" + Thread.currentThread());
                RxBus.getInstance().post(new MessageEvent(MessageEvent.EVENT_TEST_HINT));
            }
        });
        L.e("Info", "Thread:" + Thread.currentThread());
    }

}
