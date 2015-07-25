package com.parttime.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * 系统帮助类
 * Created by wyw on 2015/7/25.
 */
public class AndroidUtils {
    public static Point getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm == null){
            return new Point();
        }
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }
}
