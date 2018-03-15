package com.chuck.floating.interfaces;

import android.view.View;

/**
 * Created by chuck on 2018/3/13.
 */

public interface IFloatingWindow {
    void show();
    void hide();
    void dismiss();
    View getView();
}
