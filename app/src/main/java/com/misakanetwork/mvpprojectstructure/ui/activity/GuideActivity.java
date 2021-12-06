package com.misakanetwork.mvpprojectstructure.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.misakanetwork.lib_common.adapter.Pager2AdapterHome;
import com.misakanetwork.mvpprojectstructure.R;
import com.misakanetwork.mvpprojectstructure.ui.activity.base.BaseCenterActivity;
import com.misakanetwork.mvpprojectstructure.ui.fragment.GuideFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.ui.activity
 * class name：GuideActivity
 * desc：引导页
 */
public class GuideActivity extends BaseCenterActivity {
    @BindView(R.id.mViewPager)
    ViewPager2 mViewPager;
    @BindView(R.id.dot_layout)
    LinearLayout dotLayout;

    public static void startThis(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), GuideActivity.class);
        context.startActivity(intent);
    }

    private Pager2AdapterHome pager2AdapterHome;
    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private ImageView[] dotViews; // 创建存放图片集合

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected boolean isImmerse() {
        return true;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setTextWhite();
        pager2AdapterHome = new Pager2AdapterHome(this, fragments);
        mViewPager.setAdapter(pager2AdapterHome);
        for (int i = 0; i < 4; i++) {
            fragments.add(GuideFragment.newInstance(i));
        }
        mViewPager.setOffscreenPageLimit(100);
        mViewPager.registerOnPageChangeCallback(changeCallback);
        // 生成相应数量的导航小圆点
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置小圆点左右之间的间隔
        params.setMargins(10, 0, 10, 0);
        // 得到页面个数
//        int pageSize = (int) Math.ceil((double) fragments.size() / 8);
        int pageSize = fragments.size();
        dotViews = new ImageView[pageSize]; // 我这里是固定的六页  也可以根据自己需要设置圆点个数
        dotLayout.removeAllViews();
        for (int i = 0; i < pageSize; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.dot_indicator_e3e7eb); // 初始化六个灰色Img
            // 默认启动时，选中第一个小圆点，其他的设置不选择
            imageView.setSelected(i == 0);
            // 得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            dotViews[i] = imageView;
            dotViews[0].setImageResource(R.drawable.dot_indicator_bdc2c7); // 设置第一个页面选择
            // 添加到布局里面显示
            dotLayout.addView(imageView); // 这里的img_layout就是我在布局中写的一个Linear Layout用来存放这些圆点img
        }
    }

    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (dotViews.length < position + 1) return;
            for (int i = 0; i < dotLayout.getChildCount(); i++) {
                if (i != position) {
                    dotViews[i].setImageResource(R.drawable.dot_indicator_e3e7eb);
                }
            }
            dotViews[position].setImageResource(R.drawable.dot_indicator_bdc2c7);
        }
    };
}
