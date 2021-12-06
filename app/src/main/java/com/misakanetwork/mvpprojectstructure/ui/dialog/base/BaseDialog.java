package com.misakanetwork.mvpprojectstructure.ui.dialog.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.misakanetwork.lib_common.entity.MessageEvent;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.StrUtils;
import com.misakanetwork.lib_common.utils.rx.RxBus;
import com.misakanetwork.lib_common.widget.DYLoadingView;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseMVPActivity;
import com.misakanetwork.mvpprojectstructure.ui.dialog.LoadDialog;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.mvpprojectstructure.ui.base.dialog
 * class name：BaseDialog
 * desc：BaseDialog
 */
@SuppressLint("ValidFragment")
public abstract class BaseDialog extends DialogFragment {
    private DismissListener dismissListener;

    protected Context mContext;

    private LoadDialog loadingDialog;

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
    }

    /**
     * 弹窗消失
     */
    protected void dismissThis(boolean isResume) {
        if (isResume) {
            dismiss();
        } else {
            dismissAllowingStateLoss();
        }
    }

    public void showThis(FragmentManager manager, String tag) {
        if (!this.isAdded()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (setDialogWith() != 0F) {
                if (setHeight() != 0F) {
                    dialog.getWindow().setLayout((int) (dm.widthPixels * setDialogWith()), (int) (dm.heightPixels * setHeight()));
                } else {
                    dialog.getWindow().setLayout((int) (dm.widthPixels * setDialogWith()), setDialogHeight());
                }

            } else {
                if (setHeight() != 0F) {
                    dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), (int) (dm.heightPixels * setHeight()));
                } else {
                    dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), setDialogHeight());
                }
            }

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = setDialogPosition();
            params.dimAmount = halfTpValue();
            dialog.getWindow().setAttributes(params);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseFragmentDialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            initBundle(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setWindowAnimations(setAnimId());
        if (!clickBack()) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        onClickBackStrong();
                    }
                    return i == KeyEvent.KEYCODE_BACK;
                }
            });
        }
        if (setViewId() != 0) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(setViewId(), container);
            ButterKnife.bind(this, view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(setOutSide());
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
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
     * dialog适配自定义layout_loading.xml，其余布局在loading中的点击事件继承该方法后判断
     *
     * @param loadingLayout layout_loading.xml
     */
    public void showCustomLoading(ConstraintLayout loadingLayout, String text, boolean show) {
        loadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        getDialog().setCanceledOnTouchOutside(loadingLayout.getVisibility() == View.GONE);
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
     * dialog适配自定义loading返回键监听
     *
     * @param loadingLayout layout_loading.xml
     */
    public void initCustomLoadingDialogListener(ConstraintLayout loadingLayout) {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        loadingLayout.getVisibility() == View.VISIBLE;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dismissListener != null) {
            dismissListener.onDialogMiss();
        }
    }

    public interface DismissListener {
        void onDialogMiss();
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
                    }
                });

    }

    protected void handlerMsg(MessageEvent msgEvent) {

    }

    /**
     * clickBack为false时仍然点击返回键监听
     */
    protected void onClickBackStrong() {

    }


    /**
     * 点击外部是否消失
     */
    protected abstract boolean setOutSide();

    /**
     * 初始化View
     */
    protected abstract void initView(View view);

    /**
     * 布局id
     */
    protected abstract int setViewId();

    /**
     * 返回键是否屏蔽
     */
    protected abstract boolean clickBack();

    /**
     * 显示动画
     */
    protected abstract int setAnimId();

    /**
     * 传入值
     */
    protected abstract void initBundle(Bundle bundle);

    /**
     * 背景透明度
     *
     * @return
     */
    protected abstract float halfTpValue();

    /**
     * dialog的位置
     */
    protected abstract int setDialogPosition();

    /**
     * 宽所占屏幕的比例
     */
    protected abstract Float setDialogWith();

    /**
     * 设置高度
     * <p>
     * ViewGroup.LayoutParams.WRAP_CONTENT->高度包裹内容
     * <p>
     * ViewGroup.LayoutParams.MATCH_PARENT->高度占满屏幕
     *
     * @return
     */
    protected abstract int setDialogHeight();

    protected abstract float setHeight();
}