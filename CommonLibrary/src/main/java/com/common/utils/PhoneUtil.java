package com.common.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by LiesLee on 2017/10/11.
 * Email: LiesLee@foxmail.com
 */

public class PhoneUtil {
    /**
     * 设置当前窗口亮度
     * @param activity
     * @param brightness
     */
    public static void setWindowBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }
}
