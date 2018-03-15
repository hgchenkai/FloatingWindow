package com.chuck.floating.ui;

import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

/**
 * Created by chuck on 2018/3/13.
 */

public class FloatingWindowView extends AbstractFloatingView {

    @Override
    void initWindowLayoutParams() {
        mLayoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N_MR1){
            mLayoutParams.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            mLayoutParams.type=WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.windowAnimations=0;
    }

    public FloatingWindowView(Context mContext) {
        super(mContext);
    }
}
