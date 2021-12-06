package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.misakanetwork.lib_common.apis.Api;
import com.misakanetwork.lib_common.utils.L;
import com.misakanetwork.lib_common.utils.SingleToastUtils;
import com.misakanetwork.lib_common.utils.clickcheck.AntiShake;
import com.misakanetwork.lib_common.utils.screen.FullScreenNoBarUtils;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.adapter.ApiSetAdapter;
import com.misakanetwork.mvpprojectstructure.bean.ApiSetBean;
import com.misakanetwork.mvpprojectstructure.helper.AppHelper;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseCenterActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.HttpUrl;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.activity
 * class name：ApiSetActivity
 * desc：ApiSetActivity
 * // 全局 BaseUrl 的优先级低于 Domain-Name header 中单独配置的,其他未配置的接口将受全局 BaseUrl 的影响
 * 当仅有一个baseUrl的情况下，不需要添加Header区分
 * 全局baseUrl的替换：RetrofitUrlManager.getInstance().setGlobalDomain("your BaseUrl");
 * <p>
 * 普通模式 (只能替换域名) < 高级模式 (只能替换 {startAdvancedModel(String)} 中传入的 BaseUrl) < 超级模式 (每个 Url 都可以随意指定可被替换的 BaseUrl, pathSize 随意变换)
 * 替换 BaseUrl 的自由程度 (可扩展性)
 * 普通模式 < 高级模式 < 超级模式
 * 普通模式: 只能替换域名
 * 超级模式: 每个 URL 都可以随意指定可被替换的 BaseUrl 格式
 * 超级模式: 每个需要开启超级模式的 URL 尾部都需要加入 RetrofitUrlManager#IDENTIFICATION_PATH_SIZE (#baseurl_path_size=) + PathSize
 */
public class ApiSetActivity extends BaseCenterActivity implements ApiSetAdapter.OnItemClickedListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    public static void startThis(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), ApiSetActivity.class);
        context.startActivity(intent);
    }

    private ApiSetAdapter mAdapter;
    private List<ApiSetBean> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_api_set;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FullScreenNoBarUtils.setFull(this, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        showLoading(true, 0.5f, true, true, "ani测试");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        }, 5000);
        mData = AppHelper.getDomain();
        mAdapter = new ApiSetAdapter(this, mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(this);
    }

    @OnClick({R.id.certain_tv})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.certain_tv:
                for (int i = 0; i < mData.size(); i++) {
                    String resValue = mData.get(i).getApiContent();
                    if (resValue.isEmpty()) {
                        SingleToastUtils.init(this).showNormal(getString(R.string.string_res_url_input_hint, mData.get(i).getApiName()));
                        return;
                    }
                    HttpUrl httpUrl;
                    if (mData.get(i).getApiOriginName().equals(Api.APP_DEFAULT_DOMAIN)) { // 主api
                        httpUrl = RetrofitUrlManager.getInstance().getGlobalDomain();
                    } else {
                        httpUrl = RetrofitUrlManager.getInstance().fetchDomain(mData.get(i).getApiOriginName());
                    }
                    if (httpUrl == null || !httpUrl.toString().equals(resValue)) { // 可以在 App 运行时随意切换某个接口的 resUrl
                        try {
                            if (mData.get(i).getApiOriginName().equals(Api.APP_DEFAULT_DOMAIN)) { // 主api
                                RetrofitUrlManager.getInstance().setGlobalDomain(String.valueOf(mData.get(i).getApiContent()));
                            } else {
                                RetrofitUrlManager.getInstance().putDomain(mData.get(i).getApiOriginName(), resValue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            L.e("resUrl error:", ">>> " + e.getMessage());
                            SingleToastUtils.init(this).showLong(e.getMessage());
                        }
                    }
                }
                AppHelper.putDomain(mData);
                MainTestActivity.startThis(this);
                finish();
                break;
        }
    }

    @Override
    public void onSelected(ApiSetBean apiSetBean, int position) {
    }

    // Examples
//    private boolean setChoice() {
//        // res
//        String resValue = String.valueOf(resGlobalUrlEdt.getText()).trim();
//        if (resValue.isEmpty()) {
//            ToastUtil.getInstance().showNormal(this, "请输入资源服务器地址");
//            return true;
//        }
//        HttpUrl httpUrl = RetrofitUrlManager.getInstance().fetchDomain(AREA_FILE_NAME);
//        if (httpUrl == null || !httpUrl.toString().equals(resValue)) { // 可以在 App 运行时随意切换某个接口的 resUrl
//            try {
//                RetrofitUrlManager.getInstance().putDomain(AREA_FILE_NAME, resValue);
//            } catch (Exception e) {
//                e.printStackTrace();
//                L.e("resUrl error:", e.getMessage());
//                ToastUtil.getInstance().showLong(this, e.getMessage());
//            }
//        }
//        // baseUrl
//        // 当不存在聚合情况时的模式，不需要添加任何标签（标签作用即区分不同的baseUrl的切换），只需要在修改时setGlobalDomain
//        String baseUrl0 = String.valueOf(urlZeroGlobalUrlEdt.getText()).trim();
//        if (baseUrl0.isEmpty()) {
//            ToastUtil.getInstance().showNormal(this, "请输入基础服务器地址");
//            return true;
//        }
//        HttpUrl httpUrl0 = RetrofitUrlManager.getInstance().getGlobalDomain();
//        if (null == httpUrl0 || !httpUrl0.toString().equals(baseUrl0)) {
//            try {
//                RetrofitUrlManager.getInstance().setGlobalDomain(String.valueOf(urlZeroGlobalUrlEdt.getText()));
//            } catch (Exception e) {
//                e.printStackTrace();
//                L.e("baseUrl0 error:", e.getMessage());
//                ToastUtil.getInstance().showLong(this, e.getMessage());
//            }
//        }
//
//        // 普通模式切换，凡添加了“@Headers({DOMAIN_NAME_HEADER + your baseUrl})”的将受影响，优先级低于高级模式和超级模式
//        String baseUrl1 = String.valueOf(urlFirstGlobalUrlEdt.getText()).trim();
//        if (baseUrl1.isEmpty()) {
//            ToastUtil.getInstance().showNormal(this, "请输入服务器地址1");
//            return true;
//        }
//        HttpUrl httpUrl1 = RetrofitUrlManager.getInstance().fetchDomain(GITHUB_DOMAIN_NAME);
//        if (httpUrl1 == null || !httpUrl1.toString().equals(baseUrl1)) { // 可以在 App 运行时随意切换某个接口的 BaseUrl
//            try {
//                RetrofitUrlManager.getInstance().putDomain(GITHUB_DOMAIN_NAME, baseUrl1);
//            } catch (Exception e) {
//                e.printStackTrace();
//                L.e("baseUrl1 error:", e.getMessage());
//                ToastUtil.getInstance().showLong(this, e.getMessage());
//            }
//        }
//
//        // 超级模式切换，在接口处添加了"+ RetrofitUrlManager.IDENTIFICATION_PATH_SIZE + 2"的将受影响，优先级最高
//        // 超级模式针对整个url进行修改，必须在对应接口设定具体的IDENTIFICATION_PATH_SIZE
//        String baseUrl2 = String.valueOf(urlSecondGlobalUrlEdt.getText()).trim();
//        if (baseUrl2.isEmpty()) {
//            ToastUtil.getInstance().showNormal(this, "请输入服务器地址2");
//            return true;
//        }
//        HttpUrl httpUrl2 = RetrofitUrlManager.getInstance().fetchDomain(GANK_DOMAIN_NAME);
//        if (httpUrl2 == null || !httpUrl2.toString().equals(baseUrl2)) { // 可以在 App 运行时随意切换某个接口的 BaseUrl
//            try {
//                RetrofitUrlManager.getInstance().putDomain(GANK_DOMAIN_NAME, baseUrl2);
//            } catch (Exception e) {
//                e.printStackTrace();
//                L.e("baseUrl2 error:", e.getMessage());
//                ToastUtil.getInstance().showLong(this, e.getMessage());
//            }
//        }
//        return false;
//    }
}

