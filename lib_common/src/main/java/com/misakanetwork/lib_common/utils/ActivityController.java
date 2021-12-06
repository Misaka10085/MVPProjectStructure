package com.misakanetwork.lib_common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By：Misaka10085
 * on：2021/7/5
 * package：com.misakanetwork.lib_common.utils
 * class name：ActivityController
 * desc：ActivityController
 */
public class ActivityController {
    public static List<Activity> activityList = new ArrayList<Activity>();
    private static ActivityController instance = null;

    public ActivityController() {
    }

    public static ActivityController getInstance() {
        synchronized (ActivityController.class) {
            if (instance == null) {
                instance = new ActivityController();
            }
        }
        return instance;
    }

    /**
     * 添加 Activity 到列表
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    /**
     * 获取界面数量
     *
     * @return activity size
     */
    public int getActivitySize() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

    /**
     * 获取当前 Activity - 堆栈中最后一个压入的
     *
     * @return current Activity
     */
    public AppCompatActivity getCurrentActivity() {
        if (activityList != null && activityList.size() > 0) {
            Activity activity = activityList.get(activityList.size() - 1);
            return (AppCompatActivity) activity;
        }
        return null;
    }

    /**
     * 获取指定类名的 Activity
     *
     * @param cls 指定的类
     * @return Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityList == null) {
            return null;
        }
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束指定的 Activity
     *
     * @param activity Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    /**
     * 结束指定类名的 Activity
     *
     * @param cls 指定的类
     */
    public void removeActivity(Class<?> cls) {
        if (activityList == null) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                activityList.remove(activity);
            }
        }
    }

    /**
     * 结束所有界面
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所以我们先记住要删除的对象，遍历之后才去删除。
     */
    public void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }

    public void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 关闭除目标Class以外的所有界面
     */
    public void finishOtherActivityByClassExcept(List<Class<?>> classList) {
        if (classList == null) {
            return;
        }
        List<Activity> activities = new ArrayList<>();
        for (int i = 0; i < activityList.size(); i++) {
            for (int j = 0; j < classList.size(); j++) {
                if (activityList.get(i).getClass().equals(classList.get(j))) {
                    activities.add(activityList.get(i));
                }
            }
        }
        List<Activity> copyList = new ArrayList<>(activityList);
        copyList.removeAll(activities);
        for (int i = 0; i < copyList.size(); i++) {
            if (activityList.contains(copyList.get(i))) {
                copyList.get(i).finish();
            }
        }
        activityList.clear();
        activityList.addAll(activities);
    }


    /**
     * 判断的某个Activity是否在前台显示
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            Log.e("isForeground", "isForeground: " + cpn.getClassName());
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }

    //if (MyActivityManager.getActivitySize() == 0)判断当前应用是否存在
}
