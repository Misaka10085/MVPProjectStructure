package com.misakanetwork.mvpprojectstructure.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.misakanetwork.lib_common.utils.clickcheck.AntiShake;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.MainTestActivity;
import com.misakanetwork.mvpprojectstructure.ui.fragment.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.fragment
 * class name：GuideFragment
 * desc：GuideFragment
 */
public class GuideFragment extends BaseFragment {
    @BindView(R.id.bg_iv)
    ImageView bgIv;
    @BindView(R.id.enter_tv)
    TextView enterTv;

    public static final String GUIDE_DATA_POS = "GUIDE_DATA_POS";
    private int pos = 0;

    public static GuideFragment newInstance(int pos) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(GUIDE_DATA_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pos = bundle.getInt(GUIDE_DATA_POS);
        }
        enterTv.setVisibility(pos == 3 ? View.VISIBLE : View.GONE);
        switch (pos) {
            case 0:
                bgIv.setImageResource(R.mipmap.ic_launcher);
                break;
            case 1:
                bgIv.setImageResource(R.mipmap.ic_launcher);
                break;
            case 2:
                bgIv.setImageResource(R.mipmap.ic_launcher);
                break;
            case 3:
                bgIv.setImageResource(R.mipmap.ic_launcher);
                break;
        }
    }

    @OnClick(value = {R.id.enter_tv})
    protected void onClick(View view) {
        if (AntiShake.check(view.getId())) return;
        switch (view.getId()) {
            case R.id.enter_tv:
                MainTestActivity.startThis(mContext);
                getActivity().finish();
                break;
        }
    }
}
