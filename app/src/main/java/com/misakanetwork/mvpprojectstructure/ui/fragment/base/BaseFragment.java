package com.misakanetwork.mvpprojectstructure.ui.fragment.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.helper.BaseAppHelper;
import com.misakanetwork.lib_common.utils.CustomToastUtils;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.rx.RxBus;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseMVPActivity;
import com.misakanetwork.mvpprojectstructure.ui.dialog.LoadDialog;
import com.misakanetwork.mvpprojectstructure.ui.dialog.base.BaseDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.fragment
 * class name：BaseFragment
 * desc：BaseFragment，不需要网络请求的Fragment继承该类
 */
public abstract class BaseFragment extends Fragment implements BaseDialog.DismissListener {
    protected Context mContext;
    public Unbinder unbinder;
    private LoadDialog loadingDialog;
    private CustomToastUtils toastUtils;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyBord(View view) {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showCenterToast(String content) {
        if (toastUtils != null) {
            toastUtils.hide();
        }
        toastUtils = new CustomToastUtils(
                mContext,
                R.layout.toast_center_default,
                R.id.toast_message,
                0,
                CustomToastUtils.CENTER,
                content,
                null,
                0
        );
        toastUtils.show();
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
        loadingDialog.showThis(getChildFragmentManager(), LoadDialog.class.getSimpleName());
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
        String appCachePath = mContext.getApplicationContext().getCacheDir().getAbsolutePath();
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

    @Override
    public void onDialogMiss() {
        getActivity().finish();
    }

    /**
     * 布局文件Id
     *
     * @return
     */
    protected abstract int getLayoutId();


    /**
     * 初始化
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 根据登录存储的token是否存在判断登录状态
     *
     * @return
     */
    public boolean isValidAccount() {
        if (TextUtils.isEmpty(BaseAppHelper.getToken())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

