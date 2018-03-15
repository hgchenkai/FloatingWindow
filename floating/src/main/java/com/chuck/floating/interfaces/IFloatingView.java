package com.chuck.floating.interfaces;

import android.view.View;

/**
 * Created by chuck on 2018/3/13.
 */

public interface IFloatingView {
    void setPermissionLsn(IPermission permissionLsn);
    void setSize(int width,int height);
    void setView(View view);
    View getView();
    void setGravity(int gravity,int xOffset,int yOffset);
    void setDragType(@DragType.DRAG_TYPE int dragType);
    void show();
    void hide();
    void dismiss();
    void initTouchEventListener();
    void updateXY(int x,int y);
    void updateX(int x);
    void updateY(int y);
    int getX();
    int getY();
    /**
     * 更新动画
     */
    void addUpdateAnimatorListener(TouchEventCallback callback);

    interface TouchEventCallback{
        void callback(@DragType.DRAG_TYPE int type,int start,int end);
        void cancel();
    }
}
