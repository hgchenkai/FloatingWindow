package com.chuck.floating.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by chuck on 2018/3/14.
 */

public class SizeType {
    public static final int TYPE_WIDTH = 0x01;
    public static final int TYPE_HEIGHT = 0x02;

    @IntDef({TYPE_WIDTH, TYPE_HEIGHT})
    @Retention(value = RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
