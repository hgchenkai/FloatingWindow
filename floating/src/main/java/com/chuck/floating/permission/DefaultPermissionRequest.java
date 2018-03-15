package com.chuck.floating.permission;

import android.content.Context;

import com.chuck.floating.interfaces.PermissionListener;

/**
 * Created by chuck on 2018/3/15.
 */

public class DefaultPermissionRequest extends AbstractPermission {
    @Override
    public boolean canDrawOverlays(Context context) {
        return PermissionActivity.canDrawOverlays(context);
    }

    @Override
    public void requestPermission(Context context, PermissionListener permissionListener) {
        PermissionActivity.requestPermission(context,permissionListener);
    }
}
