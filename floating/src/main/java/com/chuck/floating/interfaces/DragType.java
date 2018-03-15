package com.chuck.floating.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by chuck on 2018/3/13.
 */

public class DragType {
    /**
     * free
     */
    public static final int TYPE_FREE=0x01;
    /**
     * fixed
     */
    public static final int TYPE_FIXED=0x02;
    /**
     * slide
     */
    public static final int TYPE_SLIDE=0x03;
    /**
     * back
     */
    public static final int TYPE_BACK=0x04;

    @IntDef({TYPE_FREE,TYPE_FIXED,TYPE_SLIDE,TYPE_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DRAG_TYPE{}
}
