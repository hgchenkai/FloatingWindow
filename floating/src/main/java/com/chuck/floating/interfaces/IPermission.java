package com.chuck.floating.interfaces;

import android.content.Context;

/**
 * Created by chuck on 2018/3/15.
 */

public interface IPermission {
    /**
     * 是否有权限显示悬浮窗
     * @param context
     * @return
     */
    boolean canDrawOverlays(Context context);

    /**
     * 请求权限
     * @param context
     * @param permissionListener 请求权限的回调
     */
    void requestPermission(Context context,PermissionListener permissionListener);
}
