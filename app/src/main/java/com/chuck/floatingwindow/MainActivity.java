package com.chuck.floatingwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.chuck.floating.interfaces.DragType;
import com.chuck.floating.interfaces.SizeType;
import com.chuck.floating.ui.FloatingWindow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        if(view.getId()==R.id.btn_floating){
            if(FloatingWindow.getWindow()!=null){
                return;
            }
             FloatingWindow.with(this)
                     .setResId(R.layout.view_floating)
                     .dragType(DragType.TYPE_BACK)
                     .width(SizeType.TYPE_WIDTH,0.5f)
                     .height(SizeType.TYPE_WIDTH,0.5f)
                     .setXOffset(500)
                     .setYOffset(50)
                     .setDuration(200)
                     .build();
             FloatingWindow.getWindow().show();
        }else if (view.getId()==R.id.btn_test){
            Toast.makeText(this,"悬浮窗",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatingWindow.dismiss();
    }
}
