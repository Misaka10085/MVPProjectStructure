package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseCenterActivity;
import com.misakanetwork.mvpprojectstructure.utils.AndroidInterface;

import butterknife.BindView;

/**
 * Created By：Misaka10085
 * on：2021/8/9
 * package：com.misakanetwork.mvpprojectstructure.ui.activity
 * class name：
 * desc：
 */
public class WebActivity extends BaseCenterActivity {
    @BindView(R.id.container_layout)
    LinearLayout containerLayout;
    @BindView(R.id.titleTb)
    Toolbar titleTb;
    @BindView(R.id.title_tv)
    TextView titleTv;

    public static void startThis(Context context) {
        context.startActivity(new Intent(context.getApplicationContext(), WebActivity.class));
    }

    private AgentWeb mAgentWeb;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void handlerMsg(MessageEvent msgEvent) {
        super.handlerMsg(msgEvent);
        switch (msgEvent.getEvent()) {
            case MessageEvent.EVENT_TEST_HINT: // js调用android Test
                showCenterToast("called");
                break;
        }
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        registerRxBus();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(containerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.color_main), 3)
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go("http://www.jd.com");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
        // 加载富文本
//        mAgentWeb.getUrlLoader().loadData("<html><h1>hello world</h1></html>", null, "utf-8");
        // 修改webView颜色
//        FrameLayout frameLayout = mAgentWeb.getWebCreator().getWebParentLayout();
//        frameLayout.setBackgroundColor(Color.BLACK);

        // 调用js方法
        mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroid");
    }

    private final WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress); // 解决进度条不满问题
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    // Fragment
//    @Override
//    public void onDestroyView() {
//        mAgentWeb.getWebLifeCycle().onDestroy();
//        super.onDestroyView();
//    }
}
