package com.chuck.floating.utils;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.UiThread;
import android.view.WindowManager;

/**
 * Created by chuck on 2018/3/13.
 */

public class Utils {
    private static Point sPoint;

    @UiThread
    public static int getScreenWidth(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.x;
    }

    @UiThread
    public static int getScreenHeight(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.y;
    }
}
