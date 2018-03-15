package com.chuck.floating.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.chuck.floating.interfaces.DragType;
import com.chuck.floating.interfaces.IFloatingView;
import com.chuck.floating.interfaces.IPermission;
import com.chuck.floating.interfaces.PermissionListener;
import com.chuck.floating.utils.Utils;

/**
 * FloatingView 抽象类
 * Created by chuck on 2018/3/13.
 */

public abstract class AbstractFloatingView implements IFloatingView {
    private Context mContext;
    private View mContentView;
    private int mDragType;
    private int xOffset;
    private int yOffset;
    private TouchEventCallback mCallback;
    private final WindowManager mWindowManager;
    protected final WindowManager.LayoutParams mLayoutParams;
    private IPermission mPermission;

    abstract void initWindowLayoutParams();

    @Override
    public void updateX(int x) {
        mLayoutParams.x = xOffset = x;
        mWindowManager.updateViewLayout(mContentView, mLayoutParams);
    }

    @Override
    public void updateY(int y) {
        mLayoutParams.y = yOffset = y;
        mWindowManager.updateViewLayout(mContentView, mLayoutParams);
    }

    @Override
    public void addUpdateAnimatorListener(TouchEventCallback callback) {
        this.mCallback = callback;
    }

    AbstractFloatingView(Context mContext) {
        this.mContext = mContext;
        this.mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mLayoutParams = new WindowManager.LayoutParams();
    }

    @Override
    public void setPermissionLsn(IPermission permissionLsn) {
        this.mPermission=permissionLsn;
    }

    @Override
    public void setSize(int width, int height) {
        mLayoutParams.width = width;
        mLayoutParams.height = height;
    }

    @Override
    public void setView(View view) {
        initWindowLayoutParams();
        this.mContentView = view;
    }

    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mLayoutParams.x = this.xOffset = xOffset;
        mLayoutParams.y = this.yOffset = yOffset;
        mLayoutParams.gravity = gravity;
    }

    @Override
    public void setDragType(@DragType.DRAG_TYPE int dragType) {
        this.mDragType = dragType;
    }

    @Override
    public void initTouchEventListener() {
        if (mDragType != DragType.TYPE_FIXED) {
            mContentView.setOnTouchListener(new OnTouchListener());
        }
    }

    @Override
    public void show() {
        if (mContentView.getParent()!=null&&mContentView.getVisibility() == View.VISIBLE) {
            return;
        }
        mContentView.setVisibility(View.VISIBLE);
        if(mPermission!=null){
            if (mPermission.canDrawOverlays(mContext)) {
                mWindowManager.addView(mContentView, mLayoutParams);
            } else {
                mPermission.requestPermission(mContext, new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        mWindowManager.addView(mContentView,mLayoutParams);
                    }

                    @Override
                    public void onFail() {
                        FloatingWindow.dismiss();
                    }
                });
            }
        }else {
            FloatingWindow.dismiss();
        }
    }

    @Override
    public void hide() {
        if(mContentView.getVisibility()!=View.GONE){
            mContentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        if(mContentView.getParent()!=null){
            mWindowManager.removeView(mContentView);
        }
    }

    @Override
    public void updateXY(int x, int y) {
        mLayoutParams.x = xOffset = x;
        mLayoutParams.y = yOffset = y;
        mWindowManager.updateViewLayout(mContentView, mLayoutParams);
    }

    @Override
    public int getX() {
        return xOffset;
    }

    @Override
    public int getY() {
        return yOffset;
    }

    private class OnTouchListener implements View.OnTouchListener {
        float lastX, lastY, dx, dy;
        int newX, newY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    mCallback.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
                    dx = event.getRawX() - lastX;
                    dy = event.getRawY() - lastY;
                    newX = (int) (getX() + dx);
                    newY = (int) (getY() + dy);
                    updateXY(newX, newY);
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    startAnimator(v.getWidth());
                    break;
                default:
                    break;
            }
            return false;
        }

        private void startAnimator(int w) {
            switch (mDragType) {
                case DragType.TYPE_SLIDE:
                    int startX = xOffset;
                    int endX = (startX * 2 + w > Utils.getScreenWidth(mContext)) ? (Utils.getScreenWidth(mContext) - w) : 0;
                    mCallback.callback(mDragType, startX, endX);
                    break;
                case DragType.TYPE_BACK:
                    mCallback.callback(mDragType, 0, 0);
                    break;
                default:
                    break;
            }
        }
    }

}
