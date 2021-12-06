package com.misakanetwork.lib_common.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.lib_common.adapter
 * class name：PagerAdapterHome
 * desc：PagerAdapterHome
 */
public class PagerAdapterHome extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public PagerAdapterHome(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.fragmentList.clear();
        this.fragmentList.addAll(fragmentList);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
