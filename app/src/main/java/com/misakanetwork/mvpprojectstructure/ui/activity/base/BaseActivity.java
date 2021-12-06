package com.misakanetwork.mvpprojectstructure.ui.activity.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.ActivityController;
import com.misakanetwork.lib_common.utils.CustomToastUtils;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.StrUtils;
import com.misakanetwork.lib_common.utils.netcheck.NetBroadcastReceiver;
import com.misakanetwork.lib_common.utils.netcheck.NetEvent;
import com.misakanetwork.lib_common.utils.rx.RxBus;
import com.misakanetwork.lib_common.widget.DYLoadingView;
import com.misakanetwork.mvpprojectstructure.MainApplication;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.MainTestActivity;
import com.misakanetwork.mvpprojectstructure.ui.dialog.LoadDialog;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base
 * class name：BaseActivity
 * desc：BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseDialog.DismissListener, NetEvent {
    public LoadDialog loadingDialog;
    private CustomToastUtils customToastUtils;
    private boolean finishDoubleCheck = false; // 双击返回键判断
    private long mExitTime; // 退出时的时间
    private NetBroadcastReceiver netBroadcastReceiver;
    private int netMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 处理部分手机回到后台后，点击图标重启app问题，会影响到app之间使用intent跳转，导致只唤醒而不根据intent重新启动app
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        // End
        ActivityController.getInstance().addActivity(this);
        initStatusBar();
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId);
        }
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.titleTb);
        initToolbar(toolbar, 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView(savedInstanceState);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isImmerse()) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//API 23以上
                setTextDark();
                if (isImmerse()) {//沉浸->状态栏为透明
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
                } else {//非沉浸->状态栏颜色动态设置
                    getWindow().setStatusBarColor(statusColorBy23());
                }
            } else {
                if (isImmerse()) {//沉浸->状态栏为透明
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
                } else {//非沉浸->状态栏颜色动态设置
                    getWindow().setStatusBarColor(statusColor());
                }
            }
        }
    }

    protected void initToolbar(Toolbar mToolbar, int iconResId, View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            this.setSupportActionBar(mToolbar);
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                if (iconResId > 0) {
                    actionBar.setHomeAsUpIndicator(iconResId);
                } else {
                    actionBar.setHomeAsUpIndicator(R.mipmap.ic_return);
                }
            }
            if (listener != null) {
                mToolbar.setNavigationOnClickListener(listener);
            }

        }
    }

    /**
     * 设置6.0以上状态栏深色文字
     */
    public void setTextDark() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   //清除4.4设置的透明样式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int flag = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flag);
    }

    /**
     * 设置6.0以上状态栏白色文字
     */
    public void setTextWhite() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyBord(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void closeKeyBord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void showCenterToast(String content) {
        if (customToastUtils != null) {
            customToastUtils.hide();
        }
        customToastUtils = new CustomToastUtils(
                this,
                R.layout.toast_center_default,
                R.id.toast_message,
                0,
                CustomToastUtils.CENTER,
                content,
                null,
                0
        );
        customToastUtils.show();
    }

    public void showImageCenterToast(String content, int res) {
        if (customToastUtils != null) {
            customToastUtils.hide();
        }
        customToastUtils = new CustomToastUtils(
                this,
                R.layout.toast_center_img_default,
                R.id.toast_message,
                R.id.toast_img,
                CustomToastUtils.CENTER,
                content,
                null,
                res
        );
        customToastUtils.show();
    }

    /**
     * 订阅RxBus
     */
    @SuppressLint("CheckResult")
    protected void registerRxBus() {
        RxBus.getInstance().toObservable(this, MessageEvent.class)
                .subscribe(new Consumer<MessageEvent>() {
                    @Override
                    public void accept(MessageEvent msgEvent) throws Exception {
                        handlerMsg(msgEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.e("RxBust Error:", throwable.getMessage());
                        registerRxBus();
                    }
                });

    }

    protected void handlerMsg(MessageEvent msgEvent) {

    }

    /**
     * 显示自定义LoadDialog
     *
     * @param needDefAni  是否需要使用dy动画效果
     * @param halfTpValue 背景透明度
     * @param bgClickable 是否可点击背景关闭dialog
     * @param clickBack   是否允许返回键关闭dialog
     * @param txt         加载动画下的文本提示
     */
    public void showLoading(boolean needDefAni, float halfTpValue, boolean bgClickable, boolean clickBack, final String txt) {
        if (loadingDialog != null) {
            loadingDialog.dismissThis(true);
        }
        loadingDialog = new LoadDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoadDialog.NEED_ANI, needDefAni);
        bundle.putFloat(LoadDialog.HALF_TP_VALUE, halfTpValue);
        bundle.putBoolean(LoadDialog.BG_CLICKABLE, bgClickable);
        bundle.putBoolean(LoadDialog.CLICK_BACK, clickBack);
        loadingDialog.setArguments(bundle);
        loadingDialog.setLoadingListener(new LoadDialog.LoadingListener() {
            @Override
            public void txtShow(TextView loadingTxt) {
                loadingTxt.setVisibility(txt.isEmpty() ? View.GONE : View.VISIBLE);
                loadingTxt.setText(txt);
            }

            @Override
            public void onClickBackStrong() {
                onLoadingStrongBack(); // loadingDialog设定clickBack为false但仍然点击返回键处理
            }
        });
        loadingDialog.showThis(getSupportFragmentManager(), LoadDialog.class.getSimpleName());
    }

    /**
     * loadingDialog设定clickBack为false但仍然点击返回键处理
     */
    public void onLoadingStrongBack() {

    }

    /**
     * 关闭自定义LoadDialog
     */
    public void hideLoading() {
        synchronized (BaseMVPActivity.class) {
            if (loadingDialog != null) {
                loadingDialog.dismissThis(loadingDialog.isResumed());
            }
        }
    }

    /**
     * 自定义layout_loading.xml，点击事件在dispatchTouchEvent()和onKeyDown()判断
     *
     * @param loadingLayout layout_loading.xml
     */
    public void showCustomLoading(ConstraintLayout loadingLayout, String text, boolean show) {
        loadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        DYLoadingView loadingView = loadingLayout.findViewById(R.id.loading_view);
        TextView loadingTv = loadingLayout.findViewById(R.id.loading_txt);
        loadingTv.setVisibility(!StrUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
        if (!StrUtils.isEmpty(text)) {
            loadingTv.setText(text);
        }
        if (show) {
            loadingView.start();
        } else {
            loadingView.stop();
        }
    }

    /**
     * dispatchTouchEvent()是否被自定义loading拦截
     *
     * @param loadingLayout layout_loading.xml
     */
    public boolean isCustomLoadingDispatchDisabled(ConstraintLayout loadingLayout) {
        return loadingLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * onKeyDown()是否被自定义loading拦截
     *
     * @param loadingLayout layout_loading.xml
     */
    public boolean isCustomLoadingOnKeyDownDisabled(ConstraintLayout loadingLayout, int keyCode) {
        return loadingLayout.getVisibility() == View.VISIBLE && keyCode != KeyEvent.KEYCODE_HOME;
    }

    /**
     * 泛用WabView初始化
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView(final WebView mWebView, String url, String content) {
        if (content != null) {
            if (!content.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                content = sb.append(getHtmlData(content)).toString();
                content = content.replace("<p>", "<p style=\"word-break:break-all\">");
            }
        } else {
            content = "";
        }
//        showLoading(...);
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        try {
//            mWebView.getSettingsExtension().setContentCacheEnable(true); // 解决X5WebView goBack()重新加载url问题
//            mWebView.setVerticalScrollBarEnabled(false);
//            //启用或禁用竖直滚动条
//            mWebView.getX5WebViewExtension().setVerticalScrollBarEnabled(false);
//        } catch (Exception e) {
//            L.e("setContentCacheEnable", e.getMessage());
//        }
        mWebView.setVerticalScrollBarEnabled(false);
//        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null); // 富文本加载参考
        if (content.isEmpty()) {
            Map<String, String> webViewHead = new HashMap<>();
            webViewHead.put("Referer", "TULEREFERER");
            mWebView.loadUrl(url);
        } else {
            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
                result.confirm();
                return false;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

//        mWebView.addJavascriptInterface(jsBridge, "AndroidNative"); // 在调用方法处选择是否设置
    }

    /**
     * 富文本适配
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void noticeLogin() {
        // 实现提醒登录逻辑
    }

    @Override
    public void onDialogMiss() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.getInstance().removeActivity(this);
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (netBroadcastReceiver == null &&
                ActivityController.getInstance().getCurrentActivity().getClass().getSimpleName().equals(MainTestActivity.class.getSimpleName())) {
            // 实例化网络接收器
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    /**
     * 是否需要双击返回键提示拦截
     */
    public void setDoubleFinishCheck(boolean need) {
        finishDoubleCheck = need;
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && finishDoubleCheck) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            SingleToastUtils.init(this).showNormal(getString(R.string.string_exit_notice));
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityController.getInstance().exit();
            super.onBackPressed();
        }
    }

    /**
     * 网络状态变化
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    private void isNetConnect() {
        switch (netMobile) {
            case 1: // wifi
                if (MainApplication.isNetBroken()) {
                    showCenterToast(getString(R.string.string_wifi_connected));
                    MainApplication.setNetBroken(false);
                }
                break;
            case 0: // 移动数据
                if (MainApplication.isNetBroken()) {
                    showCenterToast(getString(R.string.string_wifi_disconnected));
                    MainApplication.setNetBroken(false);
                }
                break;
            case -1: // 没有网络
                if (!MainApplication.isNetBroken()) {
                    showCenterToast(getString(R.string.string_net_disconnected));
                    MainApplication.setNetBroken(true);
                }
                break;
        }
    }

    /**
     * 布局文件Id
     */
    protected abstract int getLayoutId();

    /**
     * 是否沉浸
     */
    protected abstract boolean isImmerse();

    /**
     * 设置状态栏颜色(6.0以下)
     */
    protected abstract int statusColor();

    /**
     * 设置状态栏颜色(6.0以上)
     */
    protected abstract int statusColorBy23();

    /**
     * 初始化
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);
}
