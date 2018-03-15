package com.chuck.floating.ui;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chuck on 2018/3/13.
 */

public class FloatingToast extends AbstractFloatingView {
    private Toast mToast;

    private Object mTN;
    private Method mShow;
    private Method mHide;

    private int mWidth;
    private int mHeight;

    @Override
    void initWindowLayoutParams() {

    }

    public FloatingToast(Context mContext) {
        super(mContext);
        mToast=new Toast(mContext);
    }

    @Override
    public void setView(View view) {
        mToast.setView(view);
        initTN();
    }

    private void initTN() {
        try {
            Field tnField=Toast.class.getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN=tnField.get(mToast);
            mShow=mTN.getClass().getMethod("show");
            mHide=mTN.getClass().getMethod("hide");

            Field tnParams=mTN.getClass().getDeclaredField("mParams");
            tnParams.setAccessible(true);

            WindowManager.LayoutParams wLayoutParams= (WindowManager.LayoutParams) tnParams.get(mTN);
            wLayoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wLayoutParams.width=mWidth;
            wLayoutParams.height=mHeight;
            wLayoutParams.windowAnimations=0;
            Field tnNextView=mTN.getClass().getDeclaredField("mNextView");
            tnNextView.setAccessible(true);
            tnNextView.set(mTN,mToast.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSize(int width, int height) {
        this.mWidth=width;
        this.mHeight=height;
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity,xOffset,yOffset);
    }

    @Override
    public void show() {
        try {
            mShow.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hide() {
        try {
            mHide.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
