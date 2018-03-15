package com.chuck.floating.utils;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by chuck on 2018/3/14.
 */

public class SettingsCompat {
    private static final String TAG = "SettingsCompat";

    private static final int OP_SYSTEM_ALERT_WINDOW = 24;
    private static final String ACTION_MIUI="miui.intent.action.APP_PERM_EDITOR";
    private static final String ACTION_FLYME="com.meizu.safe.security.SHOW_APPSEC";
    private static final String ACTION_OPPO="com.coloros.safecenter";
    private static final String ACTION_OPPO_A53="com.oppo.safe";
    private static final String ACTION_OPPO_R="com.color.safecenter";
    private static final String ACTION_VIVO="com.iqoo.secure";
    private static final String ACTION_SMARTISAN="com.smartisanos.security.action.SWITCHED_PERMISSIONS";
    private static final String ACTION_SMARTISAN_NEW="com.smartisanos.security.action.SWITCHED_PERMISSIONS_NEW";
    private final static String HUAWEI_PACKAGE = "com.huawei.systemmanager";

    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return checkOp(context, OP_SYSTEM_ALERT_WINDOW);
        } else {
            return true;
        }
    }

    public static boolean setDrawOverlays(Context context, boolean allowed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return setMode(context, OP_SYSTEM_ALERT_WINDOW, allowed);
        }
        return false;
    }

    public static void manageDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (manageDrawOverlaysForRom(context)) {
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }

    private static boolean manageDrawOverlaysForRom(Context context) {
        if (CompatUtil.isMiui()) {
            return manageDrawOverlaysForMiui(context);
        }
        if (CompatUtil.isEmui()) {
            return manageDrawOverlaysForEmui(context);
        }
        if (CompatUtil.isFlyme()) {
            return manageDrawOverlaysForFlyme(context);
        }
        if (CompatUtil.isOppo()) {
            return manageDrawOverlaysForOppo(context);
        }
        if (CompatUtil.isVivo()) {
            return manageDrawOverlaysForVivo(context);
        }
        if (CompatUtil.isQiku()) {
            return manageDrawOverlaysForQihu(context);
        }
        if (CompatUtil.isSmartisan()) {
            return manageDrawOverlaysForSmartisan(context);
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean setMode(Context context, int op, boolean allowed) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            Method method = AppOpsManager.class.getDeclaredMethod("setMode", int.class, int.class, String.class, int.class);
            method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName(), allowed ? AppOpsManager.MODE_ALLOWED : AppOpsManager
                    .MODE_IGNORED);
            return true;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));

        }
        return false;
    }

    private static boolean startSafely(Context context, Intent intent) {
        if (context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else {
            Log.e(TAG, "Intent is not available! " + intent);
            return false;
        }
    }


    private static boolean manageDrawOverlaysForMiui(Context context) {
        Intent intent = new Intent(ACTION_MIUI);
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        // miui v5 的支持的android版本最高 4.x
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent1.setData(Uri.fromParts("package", context.getPackageName(), null));
            return startSafely(context, intent1);
        }
        return false;
    }

    private static boolean manageDrawOverlaysForEmui(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setClassName(HUAWEI_PACKAGE, "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            if (startSafely(context, intent)) {
                return true;
            }
        }
        // Huawei Honor P6
        intent.setClassName(HUAWEI_PACKAGE, "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
        intent.putExtra("showTabsNumber", 1);
        if (startSafely(context, intent)) {
            return true;
        }
        intent.setClassName(HUAWEI_PACKAGE, "com.huawei.permissionmanager.ui.MainActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        return false;
    }

    private static boolean manageDrawOverlaysForVivo(Context context) {
        Intent intent = new Intent(ACTION_VIVO);
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.MainActivity");
        return startSafely(context, intent);
    }

    private static boolean manageDrawOverlaysForOppo(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        // OPPO A53
        intent.setAction(ACTION_OPPO_A53);
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        // OPPO R7s
        intent.setAction(ACTION_OPPO_R);
        intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        intent.setAction(ACTION_OPPO);
        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
        return startSafely(context, intent);
    }

    private static boolean manageDrawOverlaysForFlyme(Context context) {
        Intent intent = new Intent(ACTION_FLYME);
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        return startSafely(context, intent);
    }

    private static boolean manageDrawOverlaysForQihu(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        if (startSafely(context, intent)) {
            return true;
        }
        intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        return startSafely(context, intent);
    }

    private static boolean manageDrawOverlaysForSmartisan(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 锤子 坚果|5.1.1|2.5.3
            Intent intent = new Intent(ACTION_SMARTISAN_NEW);
            intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions");
            // 不同版本参数不太一样
            intent.putExtra("index", 17);
            return startSafely(context, intent);
        } else {
            // 锤子 坚果|4.4.4|2.1.2
            Intent intent = new Intent(ACTION_SMARTISAN);
            intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions");
            intent.putExtra("permission", new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW});
            return startSafely(context, intent);
        }
    }
}
