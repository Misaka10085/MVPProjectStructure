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
 * Created By???Misaka10085
 * on???2021/7/5
 * package???com.misakanetwork.mvpprojectstructure.ui.base
 * class name???BaseActivity
 * desc???BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseDialog.DismissListener, NetEvent {
    public LoadDialog loadingDialog;
    private CustomToastUtils customToastUtils;
    private boolean finishDoubleCheck = false; // ?????????????????????
    private long mExitTime; // ??????????????????
    private NetBroadcastReceiver netBroadcastReceiver;
    private int netMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ??????????????????????????????????????????????????????app?????????????????????app????????????intent????????????????????????????????????intent????????????app
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//API 23??????
                setTextDark();
                if (isImmerse()) {//??????->??????????????????
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
                } else {//?????????->???????????????????????????
                    getWindow().setStatusBarColor(statusColorBy23());
                }
            } else {
                if (isImmerse()) {//??????->??????????????????
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
                } else {//?????????->???????????????????????????
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
     * ??????6.0???????????????????????????
     */
    public void setTextDark() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);   //??????4.4?????????????????????
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int flag = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flag);
    }

    /**
     * ??????6.0???????????????????????????
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
     * ???????????????
     */
    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * ???????????????
     */
    public void closeKeyBord(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void closeKeyBord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //??????window???view???????????? && view?????????
        if (imm.isActive() && getCurrentFocus() != null) {
            //??????view???token ?????????
            if (getCurrentFocus().getWindowToken() != null) {
                //??????????????????????????????????????????????????????SHOW_FORCED?????????
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
     * ??????RxBus
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
     * ???????????????LoadDialog
     *
     * @param needDefAni  ??????????????????dy????????????
     * @param halfTpValue ???????????????
     * @param bgClickable ???????????????????????????dialog
     * @param clickBack   ???????????????????????????dialog
     * @param txt         ??????????????????????????????
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
                onLoadingStrongBack(); // loadingDialog??????clickBack???false??????????????????????????????
            }
        });
        loadingDialog.showThis(getSupportFragmentManager(), LoadDialog.class.getSimpleName());
    }

    /**
     * loadingDialog??????clickBack???false??????????????????????????????
     */
    public void onLoadingStrongBack() {

    }

    /**
     * ???????????????LoadDialog
     */
    public void hideLoading() {
        synchronized (BaseMVPActivity.class) {
            if (loadingDialog != null) {
                loadingDialog.dismissThis(loadingDialog.isResumed());
            }
        }
    }

    /**
     * ?????????layout_loading.xml??????????????????dispatchTouchEvent()???onKeyDown()??????
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
     * dispatchTouchEvent()??????????????????loading??????
     *
     * @param loadingLayout layout_loading.xml
     */
    public boolean isCustomLoadingDispatchDisabled(ConstraintLayout loadingLayout) {
        return loadingLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * onKeyDown()??????????????????loading??????
     *
     * @param loadingLayout layout_loading.xml
     */
    public boolean isCustomLoadingOnKeyDownDisabled(ConstraintLayout loadingLayout, int keyCode) {
        return loadingLayout.getVisibility() == View.VISIBLE && keyCode != KeyEvent.KEYCODE_HOME;
    }

    /**
     * ??????WabView?????????
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
        webSettings.setAllowContentAccess(true); // ???????????????Content Provider????????????????????? true
        webSettings.setAllowFileAccess(true);    // ??????????????????????????????????????? true
        // ??????????????????file url?????????Javascript?????????????????????????????? false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // ??????????????????file url?????????Javascript??????????????????(????????????,http,https)???????????? false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setBlockNetworkImage(false);//?????????????????????
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        try {
//            mWebView.getSettingsExtension().setContentCacheEnable(true); // ??????X5WebView goBack()????????????url??????
//            mWebView.setVerticalScrollBarEnabled(false);
//            //??????????????????????????????
//            mWebView.getX5WebViewExtension().setVerticalScrollBarEnabled(false);
//        } catch (Exception e) {
//            L.e("setContentCacheEnable", e.getMessage());
//        }
        mWebView.setVerticalScrollBarEnabled(false);
//        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null); // ?????????????????????
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
                // ????????????????????????????????????SSL???????????????????????????
                handler.proceed();
            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

//        mWebView.addJavascriptInterface(jsBridge, "AndroidNative"); // ????????????????????????????????????
    }

    /**
     * ???????????????
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void noticeLogin() {
        // ????????????????????????
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
            // ????????????????????????
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public void setDoubleFinishCheck(boolean need) {
        finishDoubleCheck = need;
    }

    //????????????????????????
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
     * ??????????????????
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
            case 0: // ????????????
                if (MainApplication.isNetBroken()) {
                    showCenterToast(getString(R.string.string_wifi_disconnected));
                    MainApplication.setNetBroken(false);
                }
                break;
            case -1: // ????????????
                if (!MainApplication.isNetBroken()) {
                    showCenterToast(getString(R.string.string_net_disconnected));
                    MainApplication.setNetBroken(true);
                }
                break;
        }
    }

    /**
     * ????????????Id
     */
    protected abstract int getLayoutId();

    /**
     * ????????????
     */
    protected abstract boolean isImmerse();

    /**
     * ?????????????????????(6.0??????)
     */
    protected abstract int statusColor();

    /**
     * ?????????????????????(6.0??????)
     */
    protected abstract int statusColorBy23();

    /**
     * ?????????
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);
}
