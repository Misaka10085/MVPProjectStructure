package com.misakanetwork.lib_common.utils.clickcheck;

import java.util.Calendar;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.clickcheck
 * class name：OneClick
 * desc：OneClick
 */
public class OneClick {
    private String methodName;
    private static final int CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    public OneClick(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean check() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }
}