package com.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.socks.library.KLog;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 这是一个单例模式的 activity管理器，没创建一个activity就加入到这个栈内，finish就将 它移除，
 * 当应用退出时，遍历栈内activity，并且finish退出
 *
 */
public class MyActivityManager {
    private static MyActivityManager instance = null;
    private LinkedList<Activity> acts;

    private MyActivityManager() {
        acts = new LinkedList<Activity>();
    }

    public static MyActivityManager getInstance() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity act) {
        acts.add(act);
    }

    public void removeActivity(Activity act) {
        if (acts != null && acts.indexOf(act) >= 0) {
            acts.remove(act);
            act.finish();
        }
    }

    public void finishActivity(Class<?> cls){
        for (Activity activity  : acts){
            if(activity !=null && activity.getClass().equals(cls)){
                acts.remove(activity);
                activity.finish();
                activity = null;
                break;
            }
        }
    }

    public void cleanActivityExceptClass(Class<?> cls){
        for (Activity activity  : acts){
            if(activity !=null && !activity.getClass().equals(cls)){
                acts.remove(activity);
                activity.finish();
                activity = null;
                break;
            }
        }
    }

    public void cleanActivity() {
        while (acts.size() != 0) {
            Activity act = acts.poll();
            act.finish();
        }
    }

    public void cleanActivity(Activity exceptActivity) {
        if(acts != null && acts.size() > 0){
            Iterator<Activity> it = acts.iterator();
            while (it.hasNext()){
                Activity activity = it.next();
                if(activity != exceptActivity && activity != null){
                    it.remove();
                    activity.finish();
                    activity = null;
                }
            }
        }
    }

    public void finishActivityType(Class<?> cls){
        Iterator<Activity> it = acts.iterator();
        while (it.hasNext()){
            Activity activity = it.next();
            if(activity != null && activity.getClass().equals(cls)){
                it.remove();
                activity.finish();
                activity = null;
            }
        }
    }

    public Activity getTopActivity() {
        return (acts == null || acts.size() <= 0) ? null : acts.get(acts.size() - 1);
    }

    public void quit(Activity context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        // 清除临时文件 ---

        cleanActivity();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 2.2版本以下的直接使用restartPackage
        if (Build.VERSION.SDK_INT < 8) {
            am.restartPackage(context.getPackageName());
        } else {
            try {
                Method killBackgroundProcesses = am.getClass().getDeclaredMethod("killBackgroundProcesses",
                        String.class);
                killBackgroundProcesses.setAccessible(true);
                killBackgroundProcesses.invoke(am, context.getPackageName());
            } catch (Exception e) {
                KLog.d(e + "");
            }
        }
        System.exit(0);
    }


}
