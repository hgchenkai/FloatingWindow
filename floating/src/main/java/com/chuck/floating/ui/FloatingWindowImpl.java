package com.chuck.floating.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.chuck.floating.interfaces.DragType;
import com.chuck.floating.interfaces.IFloatingView;
import com.chuck.floating.interfaces.IFloatingWindow;

/**
 * Created by chuck on 2018/3/13.
 */

class FloatingWindowImpl implements IFloatingWindow {
    private FloatingWindow.Builder mBuilder;
    private IFloatingView mFloatingView;
    protected ValueAnimator mAnimator;

    public FloatingWindowImpl(FloatingWindow.Builder builder) {
        this.mBuilder = builder;
        initView();
    }

    private void initView() {
        if (mBuilder.mDragType == DragType.TYPE_FIXED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                mFloatingView = new FloatingWindowView(mBuilder.mContext);
            } else {
                mFloatingView = new FloatingToast(mBuilder.mContext);
            }
        } else {
            mFloatingView = new FloatingWindowView(mBuilder.mContext);
        }
        mFloatingView.setSize(mBuilder.mWidth, mBuilder.mHeight);
        mFloatingView.setGravity(mBuilder.mGravity, mBuilder.xOffset, mBuilder.yOffset);
        mFloatingView.setView(mBuilder.mContentView);
        mFloatingView.setDragType(mBuilder.mDragType);
        mFloatingView.addUpdateAnimatorListener(new TouchEventCallback());
        mFloatingView.setPermissionLsn(mBuilder.mPermission);
        if(mBuilder.mDragType!=DragType.TYPE_FIXED){
            initTouchEventListener();
        }
    }

    @Override
    public void show() {
        mFloatingView.show();
    }

    @Override
    public void hide() {
        mFloatingView.hide();
    }

    @Override
    public void dismiss() {
        mFloatingView.dismiss();
    }

    @Override
    public View getView() {
        return mFloatingView.getView();
    }

    private void initTouchEventListener() {
        mFloatingView.initTouchEventListener();
    }


    private class TouchEventCallback implements IFloatingView.TouchEventCallback {

        @Override
        public void callback(int type, int start, int end) {
            switch (type) {
                case DragType.TYPE_SLIDE:
                    mAnimator = ValueAnimator.ofInt(start, end);
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int x = (int) animation.getAnimatedValue();
                            mFloatingView.updateX(x);
                        }
                    });
                    startAnimator();
                    break;
                case DragType.TYPE_BACK:
                    PropertyValuesHolder pX = PropertyValuesHolder.ofInt("x", mFloatingView.getX(), mBuilder.xOffset);
                    PropertyValuesHolder pY = PropertyValuesHolder.ofInt("y", mFloatingView.getY(), mBuilder.yOffset);
                    mAnimator = ObjectAnimator.ofPropertyValuesHolder(pX, pY);
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int x = (int) animation.getAnimatedValue("x");
                            int y = (int) animation.getAnimatedValue("y");
                            mFloatingView.updateXY(x, y);
                        }
                    });
                    startAnimator();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void cancel() {
            cancelAnimator();
        }

        void startAnimator() {
            if (mBuilder.mInterpolator == null) {
                mBuilder.mInterpolator = new DecelerateInterpolator();
            }
            mAnimator.setInterpolator(mBuilder.mInterpolator);
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimator.removeAllUpdateListeners();
                    mAnimator.removeAllListeners();
                    mAnimator = null;
                }
            });
            mAnimator.setDuration(mBuilder.mDuration).start();
        }

        private void cancelAnimator() {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
        }

    }
}
