package com.chuck.floating.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chuck.floating.interfaces.PermissionListener;
import com.chuck.floating.utils.SettingsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuck on 2018/3/15.
 */

public class PermissionActivity extends Activity {
    private static List<PermissionListener> mListeners;
    private static PermissionListener mCurrentLsn;
    private boolean once = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestFloatingWindowPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!once) {
            if (mListeners != null) {
                if (mCurrentLsn != null) {
                    if (SettingsCompat.canDrawOverlays(this)) {
                        mCurrentLsn.onSuccess();
                    } else {
                        mCurrentLsn.onFail();
                    }
                    mListeners.remove(mCurrentLsn);
                }
            }
            finish();
        }else {
            once=false;
        }
    }

    private void requestFloatingWindowPermission() {
        SettingsCompat.manageDrawOverlays(this);
    }


    public static boolean canDrawOverlays(Context context) {
        return SettingsCompat.canDrawOverlays(context);
    }

    public  static void requestPermission(Context context, PermissionListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (listener != null) {
            mCurrentLsn = listener;
            mListeners.add(mCurrentLsn);
        }
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
