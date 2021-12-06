package com.misakanetwork.lib_common.utils.clickcheck;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils.clickcheck
 * class name：AntiShake
 * desc：防抖
 */
public class AntiShake {
    private static LimitQueue<OneClick> queue = new LimitQueue<>(20);

    public static boolean check(Object o) {
        String flag;
        if (o == null) {
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        } else {
            flag = o.toString();
        }
        for (OneClick util : queue.getArrayList()) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClick clickUtil = new OneClick(flag);
        queue.offer(clickUtil);
        return clickUtil.check();
    }
}
