package com.chuck.floating.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;

import com.chuck.floating.interfaces.DragType;
import com.chuck.floating.interfaces.IFloatingWindow;
import com.chuck.floating.interfaces.IPermission;
import com.chuck.floating.interfaces.SizeType;
import com.chuck.floating.permission.DefaultPermissionRequest;
import com.chuck.floating.utils.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 悬浮窗
 * Created by chuck on 2018/3/13.
 */

public class FloatingWindow {
    private static final String TAG_DEFAULT = "default_tag";
    private static Map<String, IFloatingWindow> sFloatingWindowMap;

    private FloatingWindow() {
    }

    public static IFloatingWindow getWindow() {
        return getWindow(TAG_DEFAULT);
    }

    public static IFloatingWindow getWindow(String tag) {
        return sFloatingWindowMap != null ? sFloatingWindowMap.get(tag) : null;
    }

    public static void dismiss() {
        dismiss(TAG_DEFAULT);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static void dismiss(String tag) {
        if (sFloatingWindowMap == null || !sFloatingWindowMap.containsKey(tag)) {
            return;
        }
        sFloatingWindowMap.get(tag).dismiss();
        sFloatingWindowMap.remove(tag);
    }

    public static class Builder {
        Context mContext;
        View mContentView;
        @LayoutRes
        int mResLayoutId;
        /**
         * 需要显示页面的Activity
         */
        Class[] mActivities;
        /**
         * window的宽度
         */
        int mWidth;
        /**
         * window的高度
         */
        int mHeight;
        int mGravity = Gravity.TOP | Gravity.START;
        /**
         * 拖动的类型
         */
        int mDragType;
        /**
         * x偏移
         */
        int xOffset;
        /**
         * y偏移;
         */
        int yOffset;
        /**
         * Floating Window对应的tag
         */
        String mTag;
        boolean needShow = true;
        Interpolator mInterpolator;
        long mDuration;
        IPermission mPermission;

        Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setContentView(View contentView) {
            mContentView = contentView;
            return this;
        }

        public Builder setResId(@LayoutRes int resId) {
            mResLayoutId = resId;
            return this;
        }

        public Builder setActivityFilter(boolean needShow, Class... mActivities) {
            this.mActivities = mActivities;
            this.needShow = needShow;
            return this;
        }

        /**
         * 自定义window的宽
         *
         * @param width px
         */
        public Builder width(int width) {
            mWidth = width;
            return this;
        }

        public Builder width(@SizeType.Type int type, float ratio) {
            mWidth = (int) (((type == SizeType.TYPE_WIDTH) ? Utils.getScreenWidth(mContext) :
                    Utils.getScreenHeight(mContext)) * ratio);
            return this;
        }

        /**
         * 自定义window的高
         *
         * @param height px
         */
        public Builder height(int height) {
            mHeight = height;
            return this;
        }

        public Builder height(@SizeType.Type int type, float ratio) {
            mHeight = (int) (((type == SizeType.TYPE_WIDTH) ? Utils.getScreenWidth(mContext) :
                    Utils.getScreenHeight(mContext)) * ratio);
            return this;
        }

        public Builder dragType(@DragType.DRAG_TYPE int dragType) {
            this.mDragType = dragType;
            return this;
        }

        public Builder setTag(@NonNull String tag) {
            mTag = tag;
            return this;
        }

        /**
         * x偏移
         *
         * @param xOffset
         * @return
         */
        public Builder setXOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder setXOffset(@SizeType.Type int type, float ratio) {
            xOffset = (int) (((type == SizeType.TYPE_WIDTH) ? Utils.getScreenWidth(mContext) :
                    Utils.getScreenHeight(mContext)) * ratio);
            return this;
        }

        /**
         * y偏移
         *
         * @param yOffset
         * @return
         */
        public Builder setYOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setYOffset(@SizeType.Type int type, float ratio) {
            yOffset = (int) (((type == SizeType.TYPE_WIDTH) ? Utils.getScreenWidth(mContext) :
                    Utils.getScreenHeight(mContext)) * ratio);
            return this;
        }

        public Builder setDuration(long duration) {
            this.mDuration = duration;
            return this;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            this.mInterpolator = interpolator;
            return this;
        }

        /**
         * 判断悬浮窗权限接口
         *
         * @param mPermission
         * @return
         */
        public Builder setPermissionListener(IPermission mPermission) {
            this.mPermission = mPermission;
            return this;
        }

        public void build() {
            if (sFloatingWindowMap == null) {
                sFloatingWindowMap = new ConcurrentHashMap<>();
            }

            if (TextUtils.isEmpty(mTag)) {
                mTag = TAG_DEFAULT;
            }

            if (sFloatingWindowMap.containsKey(mTag)) {
                throw new IllegalArgumentException(String.format("tag:%s is exist,please set a new tag!", mTag));
            }

            if (mContentView == null && mResLayoutId == 0) {
                throw new IllegalArgumentException("you must set contentView!");
            }

            if (mContentView == null) {
                mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
            }

            if (mPermission == null) {
                mPermission = new DefaultPermissionRequest();
            }

            IFloatingWindow floatingWindowImpl = new FloatingWindowImpl(this);
            sFloatingWindowMap.put(mTag, floatingWindowImpl);

        }
    }
}
